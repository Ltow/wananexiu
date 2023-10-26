package com.bossed.waej.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants.PaymentUrl
import com.bossed.waej.javebean.PaymentOrderResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_collection_reduction.*

class PaymentReductionActivity : BaseActivity(), View.OnClickListener {
    private var isCheck = true
    private var supplierId = ""
    private var money = 0.0
    private var searchContent = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_collection_reduction
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_collection_reduction)
        tb_collection_reduction.title = "付款减免"
        tv_sel_obj.text = "选择供应商"
        et_sel_khu.hint = "请输入供应商名称、联系人、电话"
        tv_amount.text = "当前应付"
        tv_date.text = "付款日期"
        tv_date_sel.text = DateFormatUtils.get().formatDate(TimeUtils.getNowMills())
    }

    override fun initListener() {
        tb_collection_reduction.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        et_sel_khu.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_sel_khu.isFocused)
                    if (isCheck) {
                        if (searchContent != s) {
                            tv_lxr.text = ""
                            tv_phone.text = ""
                            supplierId = ""
                        }
                        PopupWindowUtils.get()
                            .checkGongYingShangPop(et_sel_khu, this@PaymentReductionActivity) {
                                isCheck = false
                                searchContent = it.name
                                et_sel_khu.setText(it.name)
                                et_sel_khu.setSelection(it.name.length)
                                tv_lxr.text = it.contacts
                                tv_phone.text = it.contactsPhone
                                supplierId = it.id
                                getOrderList(it.id.toInt())
                            }
                    } else
                        isCheck = true
            }
        })
        et_sel_khu.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                if (isCheck) {
                    if (searchContent != et_sel_khu.text.toString()) {
                        tv_lxr.text = ""
                        tv_phone.text = ""
                        supplierId = ""
                    }
                    PopupWindowUtils.get()
                        .checkGongYingShangPop(et_sel_khu, this@PaymentReductionActivity) {
                            isCheck = false
                            searchContent = it.name
                            et_sel_khu.setText(it.name)
                            et_sel_khu.setSelection(it.name.length)
                            tv_lxr.text = it.contacts
                            tv_phone.text = it.contactsPhone
                            supplierId = it.id
                            getOrderList(it.id.toInt())
                        }
                } else
                    isCheck = true
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_date_sel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_date_sel.text = DateFormatUtils.get().formatDate(it)
                }
            }
            R.id.tv_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(supplierId) -> ToastUtils.showShort("请选择供应商")
                    TextUtils.isEmpty(tv_date_sel.text.toString()) -> ToastUtils.showShort("请选择减免日期")
                    TextUtils.isEmpty(et_discount.text.toString()) -> ToastUtils.showShort("请输入减免金额")
                    et_discount.text.toString()
                        .toDouble() > money -> ToastUtils.showShort("减免金额不能大于应付金额")
                    else -> payment()
                }
            }
        }
    }

    private fun payment() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["supplierId"] = supplierId
        params["money"] = money
        params["billTime"] = tv_date_sel.text.toString()
        params["summary"] = et_remark.text.toString()
        params["discount"] = et_discount.text.toString()
        params["type"] = "2"
        RetrofitUtils.get()
            .postJson(PaymentUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    if (t.code == 200)
                        finish()
                    ToastUtils.showShort(t.msg)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getOrderList(supplierId: Int) {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService().getPaymentOrder(supplierId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, PaymentOrderResponse::class.java)
                    tv_total_money.text = "￥${t.data.moneyOwe}"
                    money = t.data.moneyOwe.toDouble()
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        PopupWindowUtils.get().dismiss()
    }
}