package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.PurchaseHistoryInfoAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.customview.BottomDialog
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.PartListRow
import com.bossed.waej.javebean.PurchaseDetail
import com.bossed.waej.javebean.PurchaseResponse
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_purchase_history_info.*

class PurchaseHistoryInfoActivity : BaseActivity(), OnClickListener {
    private lateinit var adapter: PurchaseHistoryInfoAdapter
    private val list = ArrayList<PartListRow>()
    private var discount = ""
    private var payment = ""
    private var moneyOwe = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_purchase_history_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_purchase)
        rv_purchase.layoutManager = LinearLayoutManager(this)
        adapter = PurchaseHistoryInfoAdapter(list)
        adapter.bindToRecyclerView(rv_purchase)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        when (intent.getStringExtra("type")) {
            "id" -> getInfoForId()
            "sn" -> getInfoForSn()
        }
    }

    override fun initListener() {
        tb_purchase.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    private fun getInfoForId() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService().getPurchaseInfo(intent.getStringExtra("id").toInt())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, PurchaseResponse::class.java)
                    when (t.code) {
                        200 -> {
                            tv_number.text = t.data.orderSn
                            et_name.text = t.data.supplierName
                            et_contacts.text = t.data.contacts
                            tv_phone.text = t.data.contactsPhone
                            tv_total.text = "￥${t.data.amount}"
                            discount = t.data.discount
                            payment = t.data.payment
                            moneyOwe = t.data.moneyOwe
                            for (detail: PurchaseDetail in t.data.detailList) {
                                val row = PartListRow()
                                row.num = detail.quantity.toDouble()
                                row.purchasePrice = detail.cost
                                row.unit = detail.part.unit
                                row.name = detail.part.name
                                row.model = detail.part.model
                                row.brand = detail.part.brand
                                row.code = detail.part.code
                                row.id = detail.partId
                                row.oe = detail.part.oe
                                row.remark = detail.part.remark
                                row.status = detail.part.status
                                list.add(row)
                            }
                            adapter.setNewData(list)
                            tv_total_item.text = "共${list.size}项"
                        }

                        401 -> {
                            PopupWindowUtils.get()
                                .showLoginOutTimePop(this@PurchaseHistoryInfoActivity)
                        }

                        else -> ToastUtils.showShort(t.msg)
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    private fun getInfoForSn() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService().getPurchaseInfoFrSn(intent.getStringExtra("id"))
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, PurchaseResponse::class.java)
                    when (t.code) {
                        200 -> {
                            tv_number.text = t.data.orderSn
                            et_name.text = t.data.supplierName
                            et_contacts.text = t.data.contacts
                            tv_phone.text = t.data.contactsPhone
                            tv_total.text = "￥${t.data.amount}"
                            discount = t.data.discount
                            payment = t.data.payment
                            moneyOwe = t.data.moneyOwe
                            for (detail: PurchaseDetail in t.data.detailList) {
                                val row = PartListRow()
                                row.num = detail.quantity.toDouble()
                                row.purchasePrice = detail.cost
                                row.unit = detail.part.unit
                                row.name = detail.part.name
                                row.model = detail.part.model
                                row.brand = detail.part.brand
                                row.code = detail.part.code
                                row.id = detail.partId
                                row.oe = detail.part.oe
                                row.remark = detail.part.remark
                                row.status = detail.part.status
                                list.add(row)
                            }
                            adapter.setNewData(list)
                            tv_total_item.text = "共${list.size}项"
                        }

                        401 -> {
                            PopupWindowUtils.get()
                                .showLoginOutTimePop(this@PurchaseHistoryInfoActivity)
                        }

                        else -> ToastUtils.showShort(t.msg)
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_finished -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }

            R.id.tv_total_item -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                BottomDialog(this).create(R.layout.layout_pop_purchase_info)
                    .setCanceledOnTouchOutside(true).setViewInterface { view, dialog ->
                        view.findViewById<TextView>(R.id.tv_amount).text = tv_total.text.toString()
                        view.findViewById<TextView>(R.id.tv_discount).text = "￥$discount"
                        view.findViewById<TextView>(R.id.tv_payment).text = "￥$payment"
                        view.findViewById<TextView>(R.id.tv_moneyOwe).text = "￥$moneyOwe"
                        view.findViewById<TextView>(R.id.tv_total_num).text =
                            tv_total_item.text.toString()
                        view.findViewById<TextView>(R.id.tv_total_num).setOnClickListener {
                            dialog.dismiss()
                        }
                    }.show()
            }
        }
    }
}