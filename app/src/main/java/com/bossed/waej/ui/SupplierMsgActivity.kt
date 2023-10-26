package com.bossed.waej.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants.NewSupplierUrl
import com.bossed.waej.javebean.SupplierMsgBean
import com.bossed.waej.ui.amap.AMapActivity
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_new_supplier.*
import kotlinx.android.synthetic.main.activity_new_supplier.tv_address
import java.util.ArrayList

class SupplierMsgActivity : BaseActivity(), OnNoRepeatClickListener {
    private val checkBoxes = ArrayList<CheckBox>()
    private var latitude = "0.0"//纬度
    private var longitude = "0.0"//经度
    private var province = ""
    private var city = ""
    private var county = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_new_supplier
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_new_supplier)
        when (intent.getStringExtra("openType")) {
            "msg" -> {
                tb_new_supplier.title = "供应商详情"
                ll_total.visibility = View.VISIBLE
                rl_qk_zt.visibility = View.GONE
                rl_qk_je.visibility = View.GONE
                getSupplier()
            }
            "new" -> {
                tb_new_supplier.title = "新增供应商"
                ll_total.visibility = View.GONE
                rl_qk_zt.visibility = View.VISIBLE
                rl_qk_je.visibility = View.GONE
            }
        }
    }

    override fun initListener() {
        for (checkBox: CheckBox in checkBoxes) {
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                buttonView.setTextColor(
                    if (isChecked) Color.parseColor("#FC8A25") else Color.parseColor(
                        "#666666"
                    )
                )
            }
        }
        tb_new_supplier.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    override fun onRepeatClick(v: View?) {
        when (v!!.id) {
            R.id.ctv_yes -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_yes.isChecked = !ctv_yes.isChecked
                ctv_no.isChecked = !ctv_yes.isChecked
                rl_qk_je.visibility = if (ctv_yes.isChecked)
                    View.VISIBLE
                else
                    View.GONE
            }
            R.id.ctv_no -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_no.isChecked = !ctv_no.isChecked
                ctv_yes.isChecked = !ctv_no.isChecked
                rl_qk_je.visibility = if (ctv_no.isChecked)
                    View.GONE
                else
                    View.VISIBLE
            }
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(et_name.text.toString()) -> {
                        ToastUtils.showShort("名称不能为空")
                    }
                    else -> {
                        when (intent.getStringExtra("openType")) {
                            "msg" -> {
                                if (ctv_yes.isChecked && TextUtils.isEmpty(et_qk_je.text.toString())) {
                                    ToastUtils.showShort("请输入欠款金额")
                                    return
                                }
                                updateSupplier()
                            }
                            "new" -> {
                                if (ctv_yes.isChecked && TextUtils.isEmpty(et_qk_je.text.toString())) {
                                    ToastUtils.showShort("请输入欠款金额")
                                    return
                                }
                                newSupplier()
                            }
                        }
                    }
                }
            }
            R.id.tv_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                finish()
            }
            R.id.tv_address -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PermissionUtils.permission(PermissionConstants.LOCATION)
                    .callback(object : PermissionUtils.SimpleCallback {
                        override fun onGranted() {
                            val intent = Intent(this@SupplierMsgActivity, AMapActivity::class.java)
                            intent.putExtra("longitude", longitude.toDouble())
                            intent.putExtra("latitude", latitude.toDouble())
                            startActivityForResult(intent, 102)
                        }

                        override fun onDenied() {
                            ToastUtils.showShort("请手动开启定位权限，否则将无法正常获取定位信息！")
                        }
                    }).request()
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK) {
//            when (requestCode) {
//                102 -> {
//                    val bundle = data?.extras
//                    tv_address.text = bundle!!.getString("address")
//                    latitude = bundle.getString("latitude")!!
//                    longitude = bundle.getString("latitude")!!
//                    city = bundle.getString("city")!!
//                    county = bundle.getString("county")!!
//                    province = bundle.getString("province")!!
//                }
//            }
//        }
//    }

    private fun getSupplier() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getSupplierMsg(intent.getIntExtra("id", -1))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<SupplierMsgBean>() {

                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: SupplierMsgBean) {
                    when (t.code) {
                        200 -> {
                            et_name.setText(t.data.name)
                            et_lxr.setText(t.data.contacts)
                            et_phone.setText(t.data.contactsPhone)
                            if (t.data.supplierPayment != null) {
                                tv_moneyOwe.text = "￥${t.data.supplierPayment.moneyOwe}"
                                tv_moneyPay.text = "￥${t.data.supplierPayment.moneyPay}"
                                tv_discount.text = "￥${t.data.supplierPayment.discount}"
                                tv_balance.text = "￥${t.data.supplierPayment.balance}"
                            }
//                            et_lxr_2.setText(t.data.contacts2)
//                            et_phone_2.setText(t.data.contacts2Phone)
                            et_bz.setText(t.data.remark)
                            tv_address.setText(t.data.address)
                            if (!TextUtils.isEmpty(t.data.longitude))
                                longitude = t.data.longitude
                            if (!TextUtils.isEmpty(t.data.latitude))
                                latitude = t.data.latitude
                            var temp = arrayOf<String>()
                            if (!TextUtils.isEmpty(t.data.tag)) {
                                temp = t.data.tag.split(",").toTypedArray()
                            }
                            for (checkBox: CheckBox in checkBoxes) {
                                if (temp.isNotEmpty()) {
                                    for (str: String in temp) {
                                        if (checkBox.text.toString() == str) {
                                            checkBox.isChecked = true
                                        }
                                    }
                                }
                            }
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@SupplierMsgActivity)
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

    private fun updateSupplier() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["name"] = et_name.text.toString()
        params["contacts"] = et_lxr.text.toString()
        params["contactsPhone"] = et_phone.text.toString()
        params["moneyOwe"] = et_qk_je.text.toString()
//        params["contacts2"] = et_lxr_2.text.toString()
//        params["contacts2Phone"] = et_phone_2.text.toString()
        params["address"] = tv_address.text.toString()
//        params["latitude"] = latitude
//        params["longitude"] = longitude
        params["id"] = intent.getIntExtra("id", -1)
        params["remark"] = et_bz.text.toString()
//        val sb = StringBuilder()
//        for (checkbox: CheckBox in checkBoxes) {
//            if (checkbox.isChecked) {
//                if (sb.isNotEmpty())
//                    sb.append(",")
//                sb.append(checkbox.text)
//            }
//        }
//        if (sb.isEmpty()) {
//            ToastUtils.showShort("标签不能为空")
//            return
//        }
//        params["tag"] = sb
        RetrofitUtils.get()
            .putJson(NewSupplierUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    ToastUtils.showShort("保存成功")
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun newSupplier() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["name"] = et_name.text.toString()
        params["contacts"] = et_lxr.text.toString()
        params["contactsPhone"] = et_phone.text.toString()
        params["moneyOwe"] = et_qk_je.text.toString()
//        params["contacts2"] = et_lxr_2.text.toString()
//        params["contacts2Phone"] = et_phone_2.text.toString()
        params["address"] = tv_address.text.toString()
//        params["latitude"] = latitude
//        params["longitude"] = longitude
        params["remark"] = et_bz.text.toString()
//        val sb = StringBuilder()
//        for (checkbox: CheckBox in checkBoxes) {
//            if (checkbox.isChecked) {
//                if (sb.isNotEmpty())
//                    sb.append(",")
//                sb.append(checkbox.text)
//            }
//        }
//        if (sb.isEmpty()) {
//            ToastUtils.showShort("标签不能为空")
//            return
//        }
//        params["tag"] = sb
        RetrofitUtils.get()
            .postJson(NewSupplierUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}