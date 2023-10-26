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
import com.bossed.waej.adapter.PaymentInfoAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.Detail
import com.bossed.waej.javebean.Journal
import com.bossed.waej.javebean.PaymentSearchInfoResponse
import com.bossed.waej.util.LoadingUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_collection_info.*

class PaymentInfoActivity : BaseActivity() {
    private lateinit var accountAdapter: CollectionInfoAccountAdapter
    private val accountList = ArrayList<Journal>()
    private lateinit var orderAdapter: PaymentInfoAdapter
    private val orderList = ArrayList<Detail>()

    override fun getLayoutId(): Int {
        return R.layout.activity_collection_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_collection_info)
        tb_collection_info.title = "付款单详情"
        tv_obj.text = "供应商名称"
        tv_order_id.text = "付款单号"
        tv_date.text = "付款时间"
        tv_type_title.text = "付款详情"
        tv_ys.text = "应付"
        tv_sj_sk.text = "实际付款"
        rv_collection_info_order.layoutManager = LinearLayoutManager(this)
        orderAdapter = PaymentInfoAdapter(orderList)
        orderAdapter.bindToRecyclerView(rv_collection_info_order)
        rv_collection_info_account.layoutManager = LinearLayoutManager(this)
        accountAdapter = CollectionInfoAccountAdapter(accountList, 2)
        accountAdapter.bindToRecyclerView(rv_collection_info_account)
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
                    val prefix = orderList[position].businessOrderSn.substring(0, 4)
                    when (prefix) {
                        "SJSC" -> {
                            val intent = Intent(this, PickBySupplierInfoActivity::class.java)
                            intent.putExtra("type", "2")
                            intent.putExtra("id", orderList[position].businessOrderSn)
                            startActivity(intent)
                        }

                        "JHTH" -> {
                            val intent = Intent(this, PurchaseBackHistoryInfoActivity::class.java)
                            intent.putExtra("id", orderList[position].businessOrderSn)
                            intent.putExtra("type", "sn")
                            startActivity(intent)
                        }

                        else -> {
                            val intent = Intent(this, PurchaseHistoryInfoActivity::class.java)
                            intent.putExtra("id", orderList[position].businessOrderSn)
                            intent.putExtra("type", "sn")
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun getInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getPaymentOrderInfo(intent.getStringExtra("id").toInt())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, PaymentSearchInfoResponse::class.java)
                    tv_name.text = t.data.order.supplierName
                    tv_lxr.text = t.data.order.contacts
                    tv_phone.text = t.data.order.contactsPhone
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