package com.bossed.waej.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller

class InstallApkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //获取安装结果
        val EXTRA_STATUS =
            intent.getIntExtra(PackageInstaller.EXTRA_STATUS, PackageInstaller.STATUS_FAILURE)
        //获取安装结果对应的安装信息
        val EXTRA_STATUS_MESSAGE = intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE)
        //获取正在安装的 包名
        intent.getStringExtra(PackageInstaller.EXTRA_PACKAGE_NAME);
        if (EXTRA_STATUS == 1)
            onReceiverCallBack!!.onCallBack()
    }

    var onReceiverCallBack: OnReceiverCallBack? = null

    interface OnReceiverCallBack {
        fun onCallBack()
    }

    fun setOnReceiverCallback(onReceiverCallBack: OnReceiverCallBack) {
        this.onReceiverCallBack = onReceiverCallBack
    }
}