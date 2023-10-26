package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.ItemTypeAdapter
import com.bossed.waej.adapter.ItemUpholdAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants.UpDateItemStatusUrl
import com.bossed.waej.javebean.ItemRow
import com.bossed.waej.javebean.SelectItemBean
import com.bossed.waej.javebean.SelectItemTypeBean
import com.bossed.waej.javebean.TypeData
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_item_uphold.*

class ItemUpholdActivity : BaseActivity(), OnRefreshLoadMoreListener, OnNoRepeatClickListener {
    private lateinit var itemTypeAdapter: ItemTypeAdapter
    private val itemTypeBean = ArrayList<TypeData>()
    private lateinit var upholdAdapter: ItemUpholdAdapter
    private val itemBean = ArrayList<ItemRow>()
    private var pageNum = 1
    private var cateId = 0
    private var name = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_item_uphold
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_item_uphold)
        rv_item_type.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        itemTypeAdapter = ItemTypeAdapter(itemTypeBean)
        itemTypeAdapter.bindToRecyclerView(rv_item_type)
        rv_item_uphold.layoutManager = LinearLayoutManager(this)
        upholdAdapter = ItemUpholdAdapter(itemBean)
        upholdAdapter.bindToRecyclerView(rv_item_uphold)
        val footer = LayoutInflater.from(this).inflate(R.layout.layout_footer_view_item, null)
        footer.findViewById<TextView>(R.id.tv_add_item).setOnClickListener(this)
        upholdAdapter.setFooterView(footer)
        getTypeList()
    }

    override fun initListener() {
        srl_uphold_item.setOnRefreshLoadMoreListener(this)
        tb_item_uphold.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {

            }

            override fun onRightClick(view: View?) {
            }
        })
        itemTypeAdapter.setOnItemClickListener { adapter, view, position ->
            for (bean: TypeData in itemTypeBean) {
                bean.isSelect = bean == itemTypeBean[position]
            }
            itemTypeAdapter.setNewData(itemTypeBean)
            cateId = itemTypeBean[position].id
            name = itemTypeBean[position].name
            onRefresh(srl_uphold_item)
        }
        upholdAdapter.setOnItemClickListener { adapter, view, position ->
            for (bean: ItemRow in itemBean) {
                if (bean == itemBean[position])
                    bean.isSelect = !bean.isSelect
            }
            upholdAdapter.setNewData(itemBean)
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.ctv_item_uphold -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_item_uphold.isChecked = !ctv_item_uphold.isChecked
                for (bean: ItemRow in itemBean) {
                    bean.isSelect = ctv_item_uphold.isChecked
                }
                upholdAdapter.setNewData(itemBean)
            }
            R.id.tv_add_item -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, SelectAppletItemActivity::class.java)
                intent.putExtra("type", "applet")
                startActivity(intent)
            }
            R.id.tv_off -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                LoadingUtils.showLoading(this, "加载中...")
                val params = ArrayList<HashMap<String, Any>>()
                for (bean: ItemRow in itemBean) {
                    if (bean.isSelect) {
                        val hashMap = HashMap<String, Any>()
                        hashMap["cateId"] = bean.cateId
                        hashMap["costPrice"] = bean.costPrice
                        hashMap["createBy"] = bean.createBy
                        hashMap["createTime"] = bean.createTime
                        hashMap["id"] = bean.id
                        hashMap["madeFee"] = bean.madeFee
                        hashMap["madeMoney"] = bean.madeMoney
                        hashMap["marketPrice"] = bean.marketPrice
                        hashMap["name"] = bean.name
                        hashMap["shopId"] = bean.shopId
                        hashMap["status"] = 0
                        params.add(hashMap)
                    }
                }
                RetrofitUtils.get().putJson(
                    UpDateItemStatusUrl,
                    params,this,
                    object : RetrofitUtils.OnCallBackListener {
                        override fun onSuccess(s: String) {
                            onRefresh(srl_uphold_item)
                        }

                        override fun onFailed(e: String) {
                            ToastUtils.showShort(e)
                        }

                    })
            }
        }
    }

    private fun getTypeList() {
        Api.getInstance().getApiService()
            .getItemTypeList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<SelectItemTypeBean>() {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: SelectItemTypeBean) {
                    when (t.code) {
                        200 -> {
                            itemTypeBean.addAll(t.data)
                            if (itemTypeBean.isNotEmpty()) {
                                itemTypeBean[0].isSelect = true
                                cateId = itemTypeBean[0].id
                                name = itemTypeBean[0].name
                                getItemList()
                            }
                            itemTypeAdapter.setNewData(itemTypeBean)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@ItemUpholdActivity)
                        }
                        703 -> {
                            PopupWindowUtils.get()
                                .showSetConfirmAlertDialog(mContext, t.msg, "去购买", "") {
                                    mContext.startActivity(
                                        Intent(
                                            mContext,
                                            BuyProductActivity::class.java
                                        )
                                    )
                                }
                        }
                        else -> {
                            ToastUtils.showShort(t.msg)
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                }
            })
    }

    private fun getItemList() {
        Api.getInstance().getApiService()
            .getItemList(cateId, 2, "", pageNum, "10")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<SelectItemBean>() {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: SelectItemBean) {
                    when (t.code) {
                        200 -> {
                            itemBean.addAll(t.rows)
                            upholdAdapter.setNewData(itemBean)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@ItemUpholdActivity)
                        }
                        else -> {
                            ToastUtils.showShort(t.msg)
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                }
            })
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        itemBean.clear()
        upholdAdapter.setNewData(itemBean)
        pageNum = 1
        getItemList()
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getItemList()
        refreshLayout.finishLoadMore()
    }

    override fun onRestart() {
        onRefresh(srl_uphold_item)
        super.onRestart()
    }

}