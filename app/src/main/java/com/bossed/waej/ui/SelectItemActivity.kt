package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.SelectItemAdapter
import com.bossed.waej.adapter.ItemTypeAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBSelItem
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.ItemRow
import com.bossed.waej.javebean.SelectItemBean
import com.bossed.waej.javebean.SelectItemTypeBean
import com.bossed.waej.javebean.TypeData
import com.bossed.waej.util.OnNoRepeatClickListener
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.DoubleClicksUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_select_item.*
import org.greenrobot.eventbus.EventBus

class SelectItemActivity : BaseActivity(), OnNoRepeatClickListener, OnRefreshLoadMoreListener {
    private lateinit var itemTypeAdapter: ItemTypeAdapter
    private val itemTypeBean = ArrayList<TypeData>()
    private lateinit var selectItemAdapter: SelectItemAdapter
    private val itemBean = ArrayList<ItemRow>()
    private var pageNum = 1
    private var cateId = 0
    private var name = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_select_item
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_add_item)
        rv_item_type.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        itemTypeAdapter = ItemTypeAdapter(itemTypeBean)
        itemTypeAdapter.bindToRecyclerView(rv_item_type)
        itemTypeAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        rv_select_content.layoutManager = LinearLayoutManager(this)
        selectItemAdapter = SelectItemAdapter(itemBean)
        selectItemAdapter.bindToRecyclerView(rv_select_content)
        selectItemAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        getTypeList()
    }

    override fun initListener() {
        srl_add_item.setOnRefreshLoadMoreListener(this)
        tb_add_item.setOnTitleBarListener(object : OnTitleBarListener {
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
            adapter.setNewData(itemTypeBean)
            cateId = itemTypeBean[position].id
            name = itemTypeBean[position].name
            onRefresh(srl_add_item)
        }
        selectItemAdapter.setOnItemClickListener { adapter, view, position ->
            for (bean: ItemRow in itemBean) {
                if (bean == itemBean[position])
                    bean.isSelect = !bean.isSelect
            }
            adapter.setNewData(itemBean)
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_sure -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val selItems = ArrayList<ItemRow>()
                for (item: ItemRow in itemBean) {
                    if (item.isSelect)
                        selItems.add(item)
                }
                EventBus.getDefault().post(EBSelItem(selItems))
                finish()
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
                            PopupWindowUtils.get().showLoginOutTimePop(this@SelectItemActivity)
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
                            selectItemAdapter.setNewData(itemBean)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@SelectItemActivity)
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
        pageNum = 1
        itemBean.clear()
        getItemList()
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getItemList()
        refreshLayout.finishLoadMore()
    }

}
