package com.bossed.waej.util

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.alibaba.sdk.android.oss.*
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.OSSLog
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest
import com.alibaba.sdk.android.oss.model.OSSRequest
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.service.OssManager
import java.io.File
import java.util.HashMap

class OssUtils private constructor() {
    private val endpoint = "http://oss-cn-beijing.aliyuncs.com"
    private val bucket = "hdoss001"
    private var mCallbackAddress = ""

    companion object {
        private var instance: OssUtils? = null
            get() {
                if (field == null)
                    instance = OssUtils()
                return field
            }

        @Synchronized
        fun get(): OssUtils {
            return instance!!
        }
    }

    private fun initOSS(context: Context): OSS {
        //使用自己的获取STSToken的类
        val credentialProvider: OSSCredentialProvider
        credentialProvider =
            OSSAuthCredentialsProvider(UrlConstants.BASE_URL + UrlConstants.OssTokenUrl)
        val conf = ClientConfiguration()
        conf.connectionTimeout = 15 * 1000 // 连接超时，默认15秒
        conf.socketTimeout = 15 * 1000 // socket超时，默认15秒
        conf.maxConcurrentRequest = 5 // 最大并发请求书，默认5个
        conf.maxErrorRetry = 2 // 失败后最大重试次数，默认2次
        val oss = OSSClient(
            context.applicationContext,
            endpoint,
            credentialProvider,
            conf
        )
        OSSLog.enableLog()
        return oss
    }

    fun asyncPutImage(
        `object`: String,
        localFile: String,
        context: Context,
        listener: OnOssCallBackListener
    ) {
        val upload_start = System.currentTimeMillis()
        OSSLog.logDebug("upload start")
        if (`object` == "") {
            Log.w("AsyncPutImage", "ObjectNull")
            return
        }
        val file = File(localFile)
        if (!file.exists()) {
            Log.w("AsyncPutImage", "FileNotExist")
            Log.w("LocalFile", localFile)
            return
        }
        // 构造上传请求
        OSSLog.logDebug("create PutObjectRequest ")
        val put = PutObjectRequest(bucket, `object`, localFile)
        put.crC64 = OSSRequest.CRC64Config.YES
        if (mCallbackAddress != null) {
            // 传入对应的上传回调参数，这里默认使用OSS提供的公共测试回调服务器地址
            put.callbackParam = object : HashMap<String?, String?>() {
                init {
                    put("callbackUrl", mCallbackAddress)
                    //callbackBody可以自定义传入的信息
                    put("callbackBody", "filename=\${object}")
                }
            }
        }

        // 异步上传时可以设置进度回调
        put.progressCallback =
            OSSProgressCallback { request: PutObjectRequest?, currentSize: Long, totalSize: Long ->
                Log.d("PutObject", "currentSize: $currentSize totalSize: $totalSize")
                val progress = (100 * currentSize / totalSize).toInt()
                listener.onProgress(progress)
            }
        OSSLog.logDebug(" asyncPutObject ")
        val task: OSSAsyncTask<*> = initOSS(context).asyncPutObject(put,
            object : OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
                override fun onSuccess(request: PutObjectRequest, result: PutObjectResult) {
                    Log.d("PutObject", "UploadSuccess")
                    Log.d("ETag", result.eTag)
                    Log.d("RequestId", result.requestId)
                    val upload_end = System.currentTimeMillis()
                    OSSLog.logDebug("upload cost: " + (upload_end - upload_start) / 1000f)
                    listener.onSuccess((upload_end - upload_start) / 1000f)
                }

                override fun onFailure(
                    request: PutObjectRequest,
                    clientExcepion: ClientException,
                    serviceException: ServiceException
                ) {
                    var info = ""
                    // 请求异常
                    if (clientExcepion != null) {
                        // 本地异常如网络异常等
                        clientExcepion.printStackTrace()
                        info = clientExcepion.toString()
                    }
                    if (serviceException != null) {
                        // 服务异常
                        Log.e("ErrorCode", serviceException.errorCode)
                        Log.e("RequestId", serviceException.requestId)
                        Log.e("HostId", serviceException.hostId)
                        Log.e("RawMessage", serviceException.rawMessage)
                        info = serviceException.toString()
                        listener.onFailed(info)
                    }
                }
            })
    }


    interface OnOssCallBackListener {
        /**
         * 成功
         */
        fun onSuccess(float: Float)

        /**
         * 失败
         */
        fun onFailed(e: String)

        /**
         * 进度
         */
        fun onProgress(progress: Int)
    }
}