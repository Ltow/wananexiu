package com.bossed.waej.presenter

import android.app.Activity
import com.bossed.waej.base.BasePresenter
import com.bossed.waej.contract.RegisterContract
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.util.RetrofitUtils

class RegisterPresenter : BasePresenter<RegisterContract.RegisterView>(),
    RegisterContract.RegisterModel {
    override fun register(activity: Activity, map: HashMap<String, Any>) {
        RetrofitUtils.get()
            .postJson(UrlConstants.RegisterUrl, map, activity,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        mView!!.onRegisterSuccess(s)
                    }

                    override fun onFailed(e: String) {
                        mView!!.onRegisterError(e)
                    }
                })
    }

    override fun getVerificationCode(activity: Activity, phone: String) {
        val params = HashMap<String, String>()
        params["phone"] = phone
        params["codeType"] = "1"
        RetrofitUtils.get().getJson(UrlConstants.VerificationUrl, params, activity,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    mView!!.onGetVerificationSuccess(s)
                }

                override fun onFailed(e: String) {
                    mView!!.showError(e)
                }
            })
    }

}