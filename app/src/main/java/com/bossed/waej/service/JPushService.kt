package com.bossed.waej.service

import android.content.Context
import cn.jpush.android.api.JPushMessage
import cn.jpush.android.service.JPushMessageReceiver

class JPushService : JPushMessageReceiver() {
    override fun onAliasOperatorResult(p0: Context?, p1: JPushMessage?) {
        super.onAliasOperatorResult(p0, p1)
    }
}