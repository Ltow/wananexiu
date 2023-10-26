package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.UpDateBVDCResponse
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_update_pwd.*

class UpDatePwdActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_update_pwd
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        BarUtils.setStatusBarLightMode(window, true)
        setMarginTop(tb_update_pwd)
    }

    override fun initListener() {
        tb_update_pwd.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                if (et_new.text.toString() != et_again.text.toString()) {
                    ToastUtils.showShort("两次输入不一致")
                    return
                }
                val params = HashMap<String, String>()
                params["oldPassword"] = et_old.text.toString()
                params["newPassword"] = et_again.text.toString()
                RetrofitUtils.get().postJson(
                    UrlConstants.UpdatePassUrl, params, this@UpDatePwdActivity,
                    object : RetrofitUtils.OnCallBackListener {
                        override fun onSuccess(s: String) {
                            val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                            if (t.code == 200) {
                                SPUtils.getInstance().put("password", et_again.text.toString())
                                val hashMap = HashMap<String, String>()
                                hashMap["method"] = "GetBVData"
                                hashMap["bvdcUserID"] =
                                    SPUtils.getInstance().getString("bvdcUserID")
                                hashMap["inKind"] = "5201"
                                hashMap["inStr1"] = et_old.text.toString()
                                hashMap["inStr2"] = et_again.text.toString()
                                hashMap["setName"] = getIMEI(this@UpDatePwdActivity)
                                hashMap["Czy"] = SPUtils.getInstance().getString("username")
                                RetrofitUtils.get().getBVDCJson(UrlConstants.BvdcUrl, hashMap,
                                    object : RetrofitUtils.OnCallBackListener {
                                        override fun onSuccess(s: String) {
                                            val t2 = GsonUtils.fromJson(
                                                s,
                                                UpDateBVDCResponse::class.java
                                            )
                                            if (t2.code == "1")
                                                finish()
                                            ToastUtils.showShort(t2.message)
                                        }

                                        override fun onFailed(e: String) {
                                            ToastUtils.showShort("BVDC$e")
                                        }
                                    })
                            } else ToastUtils.showShort(t.msg)
                        }

                        override fun onFailed(e: String) {
                            ToastUtils.showShort(e)
                        }
                    })
            }
        })
    }
}