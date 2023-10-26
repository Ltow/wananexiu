package com.bossed.waej.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.javebean.ChePaiBack
import com.bossed.waej.eventbus.EBOCRCallBack
import com.bossed.waej.javebean.OcrBackBean
import com.bossed.waej.javebean.VinBean
import com.bossed.waej.util.*
import com.google.gson.Gson
import com.gyf.immersionbar.ImmersionBar
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.android.synthetic.main.activity_ocr_scan.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.greenrobot.eventbus.EventBus
import java.lang.Exception
import java.util.concurrent.TimeUnit

class OCRScanActivity : AppCompatActivity(), OnNoRepeatClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocr_scan)
        initView()
    }

    private fun initView() {
        ImmersionBar.with(this).init()
        BarUtils.setStatusBarLightMode(this, true)
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
       if (BarUtils.isNavBarVisible(window)){
           val layoutParams = tv_cancel.layoutParams as LinearLayout.LayoutParams
           layoutParams.bottomMargin = BarUtils.getNavBarHeight()
           tv_cancel.layoutParams = layoutParams
       }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_open_camera -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (PermissionUtils.isGranted(PermissionConstants.CAMERA))
                    PictureSelector.create(this@OCRScanActivity)
                        .openCamera(PictureMimeType.ofImage()) //                        .compress(true)
                        .enableCrop(true)
                        .withAspectRatio(348, 200)
                        .freeStyleCropEnabled(true) // 裁剪框是否可拖拽 true or false
                        .showCropFrame(true) // 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                        .showCropGrid(false) //                        .compress(true)
                        //                        .setOutputCameraPath()
                        .isOriginalImageControl(true)
                        .minimumCompressSize(200)
                        .forResult(PictureConfig.CHOOSE_REQUEST)
                else
                    PermissionUtils.permission(PermissionConstants.CAMERA)
                        .callback(object : PermissionUtils.SimpleCallback {
                            override fun onGranted() {
                                PictureSelector.create(this@OCRScanActivity)
                                    .openCamera(PictureMimeType.ofImage()) //                        .compress(true)
                                    .enableCrop(true)
                                    .withAspectRatio(348, 200)
                                    .freeStyleCropEnabled(true) // 裁剪框是否可拖拽 true or false
                                    .showCropFrame(true) // 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                                    .showCropGrid(false) //                        .compress(true)
                                    //                        .setOutputCameraPath()
                                    .isOriginalImageControl(true)
                                    .minimumCompressSize(200)
                                    .forResult(PictureConfig.CHOOSE_REQUEST)
                            }

                            override fun onDenied() {
                                ToastUtils.showShort("请手动开启相机权限后使用此功能!")
                            }
                        }).request()
            }
            R.id.tv_open_album -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (PermissionUtils.isGranted(PermissionConstants.CAMERA))
                    PictureSelector.create(this@OCRScanActivity)
                        .openGallery(PictureMimeType.ofImage()) // 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                        .imageEngine(GlideEngine.createGlideEngine())
                        //                                        .theme(themeId)// 主题样式设置 具体参考 values/styles
                        .selectionMode(PictureConfig.SINGLE) // 多选 or 单选
                        .previewImage(true) // 是否可预览图片
                        .enablePreviewAudio(false) // 是否可播放音频
                        .isCamera(false) // 是否显示拍照按钮
                        .enableCrop(true) // 是否裁剪
                        .withAspectRatio(348, 200)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .freeStyleCropEnabled(true) // 裁剪框是否可拖拽
                        .showCropFrame(true) // 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(false) // 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .compress(true) // 是否压缩
                        .minimumCompressSize(200) // 小于100kb的图片不压缩
                        .forResult(PictureConfig.CHOOSE_REQUEST) //结果回调onActivityResult code
                else
                    PermissionUtils.permission(PermissionConstants.CAMERA)
                        .callback(object : PermissionUtils.SimpleCallback {
                            override fun onGranted() {
                                PictureSelector.create(this@OCRScanActivity)
                                    .openGallery(PictureMimeType.ofImage()) // 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                                    .imageEngine(GlideEngine.createGlideEngine())
                                    //                                        .theme(themeId)// 主题样式设置 具体参考 values/styles
                                    .selectionMode(PictureConfig.SINGLE) // 多选 or 单选
                                    .previewImage(true) // 是否可预览图片
                                    .enablePreviewAudio(false) // 是否可播放音频
                                    .isCamera(false) // 是否显示拍照按钮
                                    .enableCrop(true) // 是否裁剪
                                    .withAspectRatio(348, 200)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                                    .freeStyleCropEnabled(true) // 裁剪框是否可拖拽
                                    .showCropFrame(true) // 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                                    .showCropGrid(false) // 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                                    .compress(true) // 是否压缩
                                    .minimumCompressSize(200) // 小于100kb的图片不压缩
                                    .forResult(PictureConfig.CHOOSE_REQUEST)
                            }

                            override fun onDenied() {
                                ToastUtils.showShort("请手动开启相机权限后使用此功能!")
                            }
                        }).request()
            }
            R.id.tv_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                val selectList = PictureSelector.obtainMultipleResult(data)
                val bitmap: Bitmap =
                    ImageUtils.WeChatBitmapTo20K(BitmapFactory.decodeFile(selectList[0].cutPath)) //新版本改用getCutPath()
                val bitmapToBase64: String = ImageUtils.bitmapToBase64(bitmap)
                LoadingUtils.showLoading(this, "识别中...")
                Thread {
                    when (intent.getIntExtra("ocrType", 0)) {
                        0 -> {//车牌
                            getRequest("6", bitmapToBase64)
                        }
                        1 -> {//行车本
                            getRequest("5", bitmapToBase64)
                        }
                        2 -> {//vin
                            getRequest("1", bitmapToBase64)
                        }
                    }
                }.start()
            }
        } else {
            finish()
        }
    }

    private fun getRequest(pid: String, strFile: String) {
        val key = "RdJWsN78hpQ2PLZxWGRkge" //用户的key,登录之后能够查看到
        val secret = "32285bc78db84fbcbd4caf0103c6c537" //用户的secret
        val url = "http://etoplive.com/ocr/recogInterface.srvc"
        try {
            val client = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS) // 连接超时时间
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
            val mBody = FormBody.Builder()
                .add("pid", pid)
                .add("file", strFile)
                .add("key", key)
                .add("secret", secret)
                .add("removeimgData", "1")
                .build()
            val request = Request.Builder().url(url).post(mBody).build()
            val response = client.newCall(request).execute()
            val result = response.body()!!.string()
            when (pid) {
                "1" -> {
                    val vingson = Gson()
                    val vinBean: VinBean = vingson.fromJson(result, VinBean::class.java)
                    EventBus.getDefault().post(
                        EBOCRCallBack(
                            "",
                            vinBean.vin,
                            "",
                            "",
                            false,
                            intent.getIntExtra("ocrType", 0)
                        )
                    )
                    finish()
                }
                "6" -> {
                    val cpgson = Gson()
                    var chepai = ""
                    val chePaiBack: ChePaiBack = cpgson.fromJson(result, ChePaiBack::class.java)
                    for (entity in chePaiBack.plate) {
                        chepai = entity.车牌号
                    }
                    EventBus.getDefault().post(
                        EBOCRCallBack(
                            chepai,
                            "",
                            "",
                            "",
                            false,
                            intent.getIntExtra("ocrType", 0)
                        )
                    )
                    finish()
                }
                "5" -> {
                    var chepai = ""
                    var vin = ""
                    var address = ""
                    var engineNo = ""
                    val gson = Gson()
                    val ocrBack: OcrBackBean = gson.fromJson(result, OcrBackBean::class.java)
                    var i = 0
                    while (i < ocrBack.resultList.size) {
                        for (entity in ocrBack.resultList.get(i).fieldList) {
                            if (entity.key.equals("PlateNo")) chepai = entity.value
                            if (entity.key.equals("VIN")) vin = entity.value
                            if (entity.key.equals("Address")) address = entity.value
                            if (entity.key.equals("EngineNo")) engineNo = entity.value
                        }
                        i++
                    }
                    EventBus.getDefault().post(
                        EBOCRCallBack(
                            chepai,
                            vin,
                            address,
                            engineNo,
                            false,
                            intent.getIntExtra("ocrType", 0)
                        )
                    )
                    finish()
                }
            }
        } catch (e: Exception) {
            ToastUtils.showLong("识别失败$e")
            finish()
            LogUtils.d("tag", e.toString())
        }
    }

    override fun onDestroy() {
        LoadingUtils.dismissLoading()
        super.onDestroy()
    }

}