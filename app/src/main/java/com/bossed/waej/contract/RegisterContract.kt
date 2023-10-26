package com.bossed.waej.contract

import android.app.Activity
import com.bossed.waej.base.BaseContract


interface RegisterContract {
    interface RegisterView : BaseContract.BaseView {
        fun onRegisterSuccess(data: String)
        fun onRegisterError(e: String)
        fun onGetVerificationSuccess(s: String)
    }

    interface RegisterModel {
        fun register(activity: Activity,map: HashMap<String, Any>)
        fun getVerificationCode(activity: Activity,phone: String)
    }
}