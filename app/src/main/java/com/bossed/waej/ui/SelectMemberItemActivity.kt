package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.SelectMemberItemAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBSelItem
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.ItemRow
import com.bossed.waej.javebean.SelectItemBean
import com.bossed.waej.util.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.immersionbar.ImmersionBar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_select_member_item.*
import kotlinx.android.synthetic.main.layout_special_title.*
import org.greenrobot.eventbus.EventBus

class SelectMemberItemActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var itemAdapter: SelectMemberItemAdapter
    private val itemBean = ArrayList<ItemRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_select_member_item
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(rl_special_title)
        rv_member_item.layoutManager = LinearLayoutManager(this)
        itemAdapter = SelectMemberItemAdapter(itemBean)
        itemAdapter.bindToRecyclerView(rv_member_item)
        itemAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        val footView =
            LayoutInflater.from(this).inflate(R.layout.layout_footer_select_member_item, null)
        footView.findViewById<TextView>(R.id.tv_add).setOnClickListener(this)
        itemAdapter.addFooterView(footView)
        getItemList()
    }

    override fun initListener() {
        itemAdapter.onItemClickListener = object : OnNoRepeatItemClickListener {
            override fun onNoRepeatItemItemClick(
                adapter: BaseQuickAdapter<*, *>?,
                view: View?,
                position: Int
            ) {
                for (bean: ItemRow in itemBean) {
                    if (bean == itemBean[position])
                        bean.isSelect = !bean.isSelect
                }
                itemAdapter.setNewData(itemBean)
            }
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }
            R.id.tv_add -> {

            }
            R.id.tv_confirm -> {
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
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                itemBean.clear()
                getItemList()
            }
        }
    }

    private fun getItemList() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getVipCardItemList(et_search_special.text.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<SelectItemBean>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: SelectItemBean) {
                    when (t.code) {
                        200 -> {
                            itemBean.addAll(t.rows)
                            itemAdapter.setNewData(itemBean)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@SelectMemberItemActivity)
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

}