package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.PurchaseBackAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBSelPurchaseOrder
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants.PurchaseFinishUrl
import com.bossed.waej.javebean.PurchaseBackResponse
import com.bossed.waej.javebean.PurchaseDetail
import com.bossed.waej.javebean.PurchaseResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_purchase_back.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.math.BigDecimal

class PurchaseBackActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: PurchaseBackAdapter
    private val list = ArrayList<PurchaseDetail>()
    private var supplierId = ""
    private var isCheck = true
    private var searchContent = ""
    private var orderSn = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_purchase_back
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_purchase_back)
        rv_purchase.layoutManager = LinearLayoutManager(this)
        adapter = PurchaseBackAdapter(list)
        adapter.bindToRecyclerView(rv_purchase)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        tb_purchase_back.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        et_name.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_name.isFocused)
                    if (isCheck) {
                        if (searchContent != s) {
                            et_contacts.text = ""
                            et_phone.text = ""
                            supplierId = ""
                        }
                        PopupWindowUtils.get()
                            .checkGongYingShangPop(et_name, this@PurchaseBackActivity) {
                                isCheck = false
                                searchContent = it.name
                                et_name.setText(it.name)
                                et_name.setSelection(it.name.length)
                                et_contacts.text = it.contacts
                                et_phone.text = it.contactsPhone
                                supplierId = it.id
                            }
                    } else {
                        isCheck = true
                        rl_no.visibility = View.GONE
                    }
            }
        })
        et_name.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                if (isCheck) {
                    if (searchContent != et_name.text.toString()) {
                        et_contacts.text = ""
                        et_phone.text = ""
                        supplierId = ""
                    }
                    PopupWindowUtils.get()
                        .checkGongYingShangPop(et_name, this@PurchaseBackActivity) {
                            isCheck = false
                            searchContent = it.name
                            et_name.setText(it.name)
                            et_name.setSelection(it.name.length)
                            et_contacts.text = it.contacts
                            et_phone.text = it.contactsPhone
                            supplierId = it.id
                        }
                } else {
                    isCheck = true
                    rl_no.visibility = View.GONE
                }
        }
        adapter.setOnChangeListener { s, i ->
            list[i].num = s
            if (TextUtils.isEmpty(s))
                return@setOnChangeListener
            if (s.toDouble() > list[i].balanceQuantity.toDouble()) {
                ToastUtils.showShort("退货数量不能大于可退数量")
                return@setOnChangeListener
            }
            count()
        }
    }

    private fun count() {
        var total = BigDecimal(0.0)
        list.forEach {
            total = total.add(
                BigDecimal(if (TextUtils.isEmpty(it.num)) "0.0" else it.num)
                    .multiply(BigDecimal(it.cost))
            )
        }
        tv_total.text = "￥${total.setScale(2, BigDecimal.ROUND_HALF_DOWN)}"
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_sel_pur -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, PurchaseHistoryActivity::class.java)
                intent.putExtra("type", "2")
                intent.putExtra("listType", "1")
                intent.putExtra("gys", et_name.text.toString())
                startActivity(intent)
            }

            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (TextUtils.isEmpty(supplierId)) {
                    ToastUtils.showShort("请选择需要退货的单据")
                    return
                }
                finished()
            }
        }
    }

    @Subscribe
    fun onMessageEvent(eb: EBSelPurchaseOrder) {
        getOrderInfo(eb.id)
        orderSn = eb.sn
    }

    private fun getOrderInfo(id: String) {
        Api.getInstance().getApiService().getPurchaseInfo(id.toInt())
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
                            et_name.setText(t.data.supplierName)
                            et_contacts.text = t.data.contacts
                            et_phone.text = t.data.contactsPhone
                            tv_pur_no.text = t.data.businessOrderNo
                            rl_no.visibility = View.VISIBLE
                            supplierId = t.data.supplierId
                            list.clear()
                            list.addAll(t.data.detailList)
                            list.forEach {
                                if (it.balanceQuantity.toDouble() == 0.0)
                                    list.remove(it)
                            }
                            adapter.setNewData(list)
                        }

                        401 -> {
                            PopupWindowUtils.get()
                                .showLoginOutTimePop(this@PurchaseBackActivity)
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

    private fun finished() {
        val params = HashMap<String, Any>()
        params["type"] = 2// 1-进货 2-退回
        params["businessOrderNo"] = orderSn
        params["status"] = 1//0-草稿 1-结算 2-作废
        params["supplierId"] = supplierId
        params["supplierName"] = et_name.text.toString()
        params["contacts"] = et_contacts.text.toString()
        params["contactsPhone"] = et_phone.text.toString()
        val detailList = ArrayList<HashMap<String, Any>>()
        var quantity = BigDecimal(0.0)
        var amount = BigDecimal(0.0)
        for (row: PurchaseDetail in list) {
            row.num = if (TextUtils.isEmpty(row.num)) "0" else row.num
            if (row.num!!.toDouble() > row.balanceQuantity.toDouble()) {
                ToastUtils.showShort("配件：${row.part.name} 的退货数量不能大于可退数量")
                return
            }
            quantity = quantity.add(BigDecimal(row.num))
            amount = amount.add(BigDecimal(row.num).multiply(BigDecimal(row.cost)))
            val hashMap = HashMap<String, Any>()
            hashMap["businessDetailId"] = row.id
            hashMap["partId"] = row.partId
            hashMap["quantity"] = row.num!!
            hashMap["cost"] = row.cost
            hashMap["amount"] =
                BigDecimal(row.num).multiply(BigDecimal(row.cost)).toString()
            detailList.add(hashMap)
        }
        params["detailList"] = detailList
        params["quantity"] = quantity.toString()
        params["amount"] = amount.toString()
        LoadingUtils.showLoading(this, "加载中...")
        RetrofitUtils.get()
            .putJson(
                PurchaseFinishUrl, params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        LogUtils.d("tag", s)
                        val t = GsonUtils.fromJson(s, PurchaseBackResponse::class.java)
                        ToastUtils.showShort(t.msg)
                        if (t.code == 200) {
                            if (t.data.moneyOwe.toDouble() != 0.0) {
                                val intent = Intent(
                                    this@PurchaseBackActivity,
                                    PurchaseBackSettleActivity::class.java
                                )
                                intent.putExtra("amount", t.data.amount)
                                intent.putExtra("orderId", t.data.id)
                                startActivity(intent)
                                finish()
                            } else {
                                PopupWindowUtils.get().showOnlyConfirmPop(
                                    this@PurchaseBackActivity,
                                    "提示",
                                    "检测该进货单还没有付款，本次退货退款自动冲减往来账，如已付款，请先做付款单后再进行退货。"
                                ) {
                                    finish()
                                }
                            }
                        }
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
//        PopupWindowUtils.get().dismissGYSPop()
        PopupWindowUtils.get().dismiss()
    }
}