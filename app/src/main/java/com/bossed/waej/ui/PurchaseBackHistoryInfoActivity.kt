package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.PurchaseHistoryInfoAdapter2
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.*
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_purchase_back.*
import java.math.BigDecimal

class PurchaseBackHistoryInfoActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: PurchaseHistoryInfoAdapter2
    private val list = ArrayList<PartListRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_purchase_back
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_purchase_back)
        tb_purchase_back.title = "进货退回明细"
        tv_sel_pur.visibility = View.GONE
        rl_no.visibility = View.VISIBLE
        tv_confirm.setBackgroundResource(R.drawable.shape_blue_gradient_bg)
        tv_confirm.text = "返回"
        rv_purchase.layoutManager = LinearLayoutManager(this)
        adapter = PurchaseHistoryInfoAdapter2(list)
        adapter.bindToRecyclerView(rv_purchase)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        et_name.hint = ""
        et_name.isEnabled = false
        when (intent.getStringExtra("type")) {
            "id" -> getInfoForId()
            "sn" -> getInfoForSn()
        }
    }

    override fun initListener() {
        tb_purchase_back.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }
        }
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
                    et_name.setText(t.data.supplierName)
                    et_contacts.text = t.data.contacts
                    et_phone.text = t.data.contactsPhone
                    tv_pur_no.text = t.data.businessOrderNo
                    var total = BigDecimal(0.0)
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
                        total =
                            total.add(BigDecimal(detail.quantity).multiply(BigDecimal(detail.cost)))
                                .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                        list.add(row)
                    }
                    adapter.setNewData(list)
                    tv_total.text = "￥${total}"
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
                    et_name.setText(t.data.supplierName)
                    et_contacts.text = t.data.contacts
                    et_phone.text = t.data.contactsPhone
                    tv_pur_no.text = t.data.businessOrderNo
                    var total = BigDecimal(0.0)
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
                        total =
                            total.add(BigDecimal(detail.quantity).multiply(BigDecimal(detail.cost)))
                                .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                        list.add(row)
                    }
                    adapter.setNewData(list)
                    tv_total.text = "￥${total}"
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }
}