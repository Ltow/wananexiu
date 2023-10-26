package com.bossed.waej.util

import android.content.Context
import android.os.Build
import android.os.Environment
import com.blankj.utilcode.util.LogUtils
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception

class DownLoadUtils private constructor() {
    companion object {
        private var instance: DownLoadUtils? = null
            get() {
                if (field == null)
                    instance = DownLoadUtils()
                return field
            }

        @Synchronized
        fun get(): DownLoadUtils {
            return instance!!
        }
    }

    private var okHttpClient: OkHttpClient? = null

    init {
        okHttpClient = OkHttpClient()
    }

    /**
     * 使用okHttp3 下载
     */
    val download: (String, Context, OnDownLoadListener) -> Unit = { url, context, listener ->
        okHttpClient!!.newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                listener.onFailed(e)
            }

            override fun onResponse(call: Call, response: Response) {
                var inputStream: InputStream? = null
                val buf = ByteArray(2048)
                var len = 0
                var fos: FileOutputStream? = null
                try {
                    inputStream = response.body()!!.byteStream()
                    val total = response.body()!!.contentLength()
                    val file =
                        File(makeDir(context), url.substring(url.lastIndexOf("/") + 1))
                    fos = FileOutputStream(file)
                    var sum: Long = 0
                    while (inputStream.read(buf).also { len = it } != -1) {
                        fos.write(buf, 0, len)
                        sum += len.toLong()
                        val progress = (sum * 1.0f / total * 100).toInt()
                        listener.onLoading(progress)
                    }
                    fos.flush()
                    listener.onSuccess(file.absolutePath)
                } catch (e: Exception) {
                    listener.onFailed(e)
                } finally {
                    try {
                        inputStream!!.close()
                    } catch (e: IOException) {
                        LogUtils.e(e.message)
                    }
                    try {
                        fos!!.close()
                    } catch (e: IOException) {
                        LogUtils.e(e.message)
                    }
                }
            }
        })
    }

    /**
     * 创建文件路径
     * @return 保存路径
     */
    private val makeDir: (Context) -> String = {
        val file =
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
//                File(it.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath)
//            else
                File(Environment.getExternalStorageDirectory().absolutePath)
        if (!file.exists())
            file.mkdir()
        file.absolutePath
    }

    interface OnDownLoadListener {
        /**
         * @param path 保存apk的路径
         */
        fun onSuccess(path: String)

        /**
         * @param progress 当前进度
         */
        fun onLoading(progress: Int)

        /**
         * @param e 异常
         */
        fun onFailed(e: Exception)
    }
}