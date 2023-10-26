package com.bossed.waej.base

import android.app.Application
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.SPUtils

class ExampleApplication : Application() {
    private var registrationId = ""
    private var authorization = ""

    override fun onCreate() {
        super.onCreate()
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
    }

    fun getAuthorization(): String {
        authorization = SPUtils.getInstance().getString("authorization", "")
        return authorization
    }

    fun setAuthorization(authorization: String) {
        SPUtils.getInstance().put("authorization", authorization)
    }
}