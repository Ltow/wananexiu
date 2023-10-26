package com.bossed.waej.activityresultcontract

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract

/**
 * android R及以上系统申请外部存储权限
 */
class StartFilesDirContract : ActivityResultContract<Unit, Int>() {
    override fun createIntent(context: Context, input: Unit?): Intent {
        val intent = Intent()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
//        }
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Int {
        return resultCode
    }
}