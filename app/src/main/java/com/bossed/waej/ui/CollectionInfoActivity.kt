package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.CollectionInfoAccountAdapter
import com.bossed.waej.adapter.CollectionInfoOrderAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.*
import com.bossed.waej.util.LoadingUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_collection_info.*

class CollectionInfoActivity : BaseActivity() {
    private lateinit var accountAdapter: CollectionInfoAccountAdapter
    private val accountList = ArrayList<Journal>()
    private lateinit var orderAdapter: CollectionInfoOrderAdapter
    private val orderList = ArrayList<CollectionDetail>()

    override fun getLayoutId(): Int {
        return R.layout.activity_collection_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_collection_info)
        rv_collection_info_order.layoutManager = LinearLayoutManager(this)
        orderAdapter = CollectionInfoOrderAdapter(orderList)
        orderAdapter.bindToRecyclerView(rv_collection_info_order)
        orderAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        rv_collection_info_account.layoutManager = LinearLayoutManager(this)
        accountAdapter = CollectionInfoAccountAdapter(accountList, 1)
        accountAdapter.bindToRecyclerView(rv_collection_info_account)
        accountAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        getInfo()
    }

    override fun initListener() {
        tb_collection_info.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        orderAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_see -> {
                    val intent = Intent(this, OrderHistoryMsgActivity::class.java)
                    intent.putExtra("type", "2")
                    intent.putExtra("id", orderList[position].businessOrderSn)
                    startActivity(intent)
                }
            }
        }
    }

    private fun getInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getReceivableOrderInfo(intent.getStringExtra("id").toInt())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, CollectionInfoResponse::class.java)
                    tv_name.text = t.data.order.customerName
                    tv_lxr.text = t.data.order.customerName
                    tv_phone.text = t.data.order.customerPhone
                    tv_czy.text = t.data.order.updateBy
                    tv_moneyOwe.text = "￥${t.data.order.payable}"
                    tv_discount.text = "￥${t.data.order.discount}"
                    tv_moneyPay.text = "￥${t.data.order.moneyPay}"
                    tv_sk_order.text =
                        if (t.data.detailList.isEmpty()) "" else t.data.detailList[0].paymentOrderSn
                    tv_billTime.text =
                        if (t.data.journalList.isEmpty()) "" else t.data.journalList[0].billTime
                    accountList.addAll(t.data.journalList)
                    accountAdapter.setNewData(accountList)
                    orderList.addAll(t.data.detailList)
                    orderAdapter.setNewData(orderList)
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }
}