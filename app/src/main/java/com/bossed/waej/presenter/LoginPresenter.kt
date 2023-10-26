package com.bossed.waej.presenter

import android.app.Activity
import com.blankj.utilcode.util.GsonUtils
import com.bossed.waej.base.BasePresenter
import com.bossed.waej.contract.LoginContract
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.LoginResponse
import com.bossed.waej.util.RetrofitUtils

class LoginPresenter : BasePresenter<LoginContract.LoginView>(), LoginContract.LoginModel {
    override fun login(activity: Activity, map: Map<String, String>) {
        RetrofitUtils.get().postJson(
            UrlConstants.LoginUrl,
            map,
            activity,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, LoginResponse::class.java)
                    mView!!.onSuccess(t)
                }

                override fun onFailed(e: String) {
                    mView!!.showError(e)
                }
            })
    }
}