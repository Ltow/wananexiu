package com.bossed.waej.activityresultcontract

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract

/**
 * android O及以上系统申请安装权限
 */
class StartInstallsPermissionContract : ActivityResultContract<String, Int>() {
    override fun createIntent(context: Context, input: String): Intent {
        val packageURI = Uri.parse("package:${input}")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Int {
        return resultCode
    }
}