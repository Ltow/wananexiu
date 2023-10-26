package com.bossed.waej.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.*
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.CheckVersionResponse
import java.io.File
import java.lang.Exception
import android.content.pm.PackageManager

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE

import androidx.core.content.ContextCompat

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Dialog
import android.os.Build.VERSION.SDK_INT

import android.os.Environment
import android.view.View
import android.widget.ProgressBar
import com.bossed.waej.R

class CheckVersionUtils private constructor() {
    companion object {
        private var instance: CheckVersionUtils? = null
            get() {
                if (field == null)
                    instance = CheckVersionUtils()
                return field
            }

        @Synchronized
        fun get(): CheckVersionUtils {
            return instance!!
        }
    }

    private var downloadUrl = ""//下载地址
    private var apkPath = ""//apk保存路径

    /**
     * 检测版本
     */
    val checkVersion = { activity: Activity, listener: OnCheckVersionListener ->
        val params = HashMap<String, String>()
        params["type"] = "1"
        RetrofitUtils.get().getJson(
            UrlConstants.CheckVersionUrl, params, activity,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, CheckVersionResponse::class.java)
                    if (AppUtils.getAppVersionCode() < t.data.versionCode) {
                        PopupWindowUtils.get().showNewVersionPop(
                            t.data.remark, t.data.forcedToup,
                            activity
                        ) {
                            when {
                                havePermission(activity) -> listener.downLoad(t.data.forcedToup)
                                else -> listener.onRequest(t.data.forcedToup)
                            }
                        }
                        downloadUrl = t.data.versionUrl
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    /**
     * 检测是否有外部文件权限
     */
    private val havePermission: (Activity) -> Boolean = {
//        if (SDK_INT >= Build.VERSION_CODES.R) {
//            Environment.isExternalStorageManager()
//        } else {
        val result =
            ContextCompat.checkSelfPermission(it, READ_EXTERNAL_STORAGE)
        val result1 =
            ContextCompat.checkSelfPermission(it, WRITE_EXTERNAL_STORAGE)
        result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
//        }
    }

    /**
     * 下载安装包
     */
    val download = { activity: Activity, listener: OnDownloadListener ->
        val dialog = Dialog(activity, R.style.Dialog)
        val contentView = View.inflate(activity, R.layout.layout_progress_dialog, null)
        dialog.setContentView(contentView)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val progressBar = contentView.findViewById<ProgressBar>(R.id.pb_progress)
        dialog.show()
        DownLoadUtils.get()
            .download(downloadUrl, activity, object : DownLoadUtils.OnDownLoadListener {
                override fun onSuccess(path: String) {
                    apkPath = path
                    dialog.dismiss()
                    if (SDK_INT >= Build.VERSION_CODES.O)
                        if (activity.packageManager.canRequestPackageInstalls())//有安装未知应用权限
                            listener.onSuccess(path)
                        else //没有就去跳转申请
                            listener.onRequest()
                    else
                        installApk(activity)
                }

                override fun onLoading(progress: Int) {
                    progressBar.progress = progress
                }

                override fun onFailed(e: Exception) {
                    ToastUtils.showShort("下载失败：${e.message}")
                }
            })
    }

    /**
     * 安装apk
     */
    val installApk = { it: Activity ->
        val intent = Intent(Intent.ACTION_VIEW)
        val apkUri: Uri
        //判断版本是否是 7.0 及 7.0 以上
        if (SDK_INT >= Build.VERSION_CODES.N) {
            apkUri = FileProvider.getUriForFile(
                it, "${it.packageName}.fileProvider", File(apkPath)
            )
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            apkUri = Uri.fromFile(File(apkPath))
        }
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        it.startActivity(intent)
    }

    interface OnCheckVersionListener {
        fun onRequest(forcedToup: Int)
        fun downLoad(forcedToup: Int)
    }

    interface OnDownloadListener {
        fun onRequest()
        fun onSuccess(path: String)
    }
}