package com.bossed.waej.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants.ReceivableUrl
import com.bossed.waej.javebean.CollectionResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_collection_reduction.*

class CollectionReductionActivity : BaseActivity(), View.OnClickListener {
    private var isCheck = true
    private var customerId = -1
    private var money = 0.0
    private var searchContent = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_collection_reduction
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_collection_reduction)
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
                        if (searchContent!=s){
                            tv_lxr.text = ""
                            tv_phone.text = ""
                            customerId = -1
                        }
                        PopupWindowUtils.get()
                            .checkKeHuList(et_sel_khu, this@CollectionReductionActivity) {
                                isCheck = false
                                searchContent=it.customerName
                                et_sel_khu.setText(it.customerName)
                                et_sel_khu.setSelection(it.customerName.length)
                                tv_lxr.text = it.customerName
                                tv_phone.text = it.customerPhone
                                customerId = it.customerId
                                getOrderList(it.customerId)
                            }
                    } else
                        isCheck = true
            }
        })
        et_sel_khu.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                if (isCheck) {
                    if (searchContent!=et_sel_khu.text.toString()){
                        tv_lxr.text = ""
                        tv_phone.text = ""
                        customerId = -1
                    }
                    PopupWindowUtils.get()
                        .checkKeHuList(et_sel_khu, this@CollectionReductionActivity) {
                            isCheck = false
                            searchContent=it.customerName
                            et_sel_khu.setText(it.customerName)
                            et_sel_khu.setSelection(it.customerName.length)
                            tv_lxr.text = it.customerName
                            tv_phone.text = it.customerPhone
                            customerId = it.customerId
                            getOrderList(it.customerId)
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
            }
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(tv_date_sel.text.toString()) -> ToastUtils.showShort("请选择减免日期")
                    TextUtils.isEmpty(et_discount.text.toString()) -> ToastUtils.showShort("请输入减免金额")
                    et_discount.text.toString()
                        .toDouble() > money -> ToastUtils.showShort("减免金额不能大于应收金额")
                    else -> collection()
                }
            }
        }
    }

    private fun getOrderList(id: Int) {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getReceivableOrder(id)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    val t = GsonUtils.fromJson(s, CollectionResponse::class.java)
                    tv_total_money.text = "￥${t.data.moneyOwe}"
                    money = t.data.moneyOwe.toDouble()
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    private fun collection() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["customerId"] = customerId
        params["billTime"] = tv_date_sel.text.toString()
        params["summary"] = et_remark.text.toString()
        params["discount"] = et_discount.text.toString()
        params["type"] = "2"
        RetrofitUtils.get()
            .postJson(ReceivableUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    if (t.code == 200) {
                        finish()
                    }
                    ToastUtils.showShort(t.msg)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        PopupWindowUtils.get().dismiss()
    }
}