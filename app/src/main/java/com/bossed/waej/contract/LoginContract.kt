package com.bossed.waej.contract

import android.app.Activity
import com.bossed.waej.base.BaseContract
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.javebean.CheckVersionResponse
import com.bossed.waej.javebean.LoginResponse

interface LoginContract : BaseContract {
    interface LoginView : BaseContract.BaseView {
        fun onSuccess(response: LoginResponse)
    }

    interface LoginModel {
        fun login(activity: Activity,map: Map<String, String>)
    }
}