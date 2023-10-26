package com.bossed.waej.ui

import androidx.appcompat.app.AppCompatActivity
import com.bossed.waej.util.OnNoRepeatClickListener
import android.os.Bundle
import com.bossed.waej.R
import android.view.WindowManager
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.PictureConfig
import com.bossed.waej.util.GlideEngine
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.bossed.waej.eventbus.EBSelPicCallBack
import org.greenrobot.eventbus.EventBus

class SelectPicActivity : AppCompatActivity(), OnNoRepeatClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocr_scan)
        initView()
    }

    private fun initView() {
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    override fun onRepeatClick(v: View?) {
        when (v!!.id) {
            R.id.tv_open_camera -> PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage()) //                        .compress(true)
                .enableCrop(true) //                    .withAspectRatio(348, 200)
                .freeStyleCropEnabled(true) // 裁剪框是否可拖拽 true or false
                .showCropFrame(true) // 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false) //                        .compress(true)
                //                        .setOutputCameraPath()
                .isOriginalImageControl(true)
                .minimumCompressSize(200)
                .forResult(PictureConfig.CHOOSE_REQUEST)
            R.id.tv_open_album -> PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage()) // 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                .selectionMode(intent.getIntExtra("selMode", PictureConfig.SINGLE)) // 多选 or 单选
                .maxSelectNum(intent.getIntExtra("selNum", 1))
                .minSelectNum(1)
                .imageEngine(GlideEngine.createGlideEngine())
                .previewImage(true) // 是否可预览图片
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(false) // 是否显示拍照按钮
                .enableCrop(true) // 是否裁剪
//                .withAspectRatio(
//                    intent.getIntExtra("ratioX", 9),
//                    intent.getIntExtra("ratioY", 16)
//                )// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .freeStyleCropEnabled(true) // 裁剪框是否可拖拽
                .showCropFrame(true) // 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(true) // 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .compress(true) // 是否压缩
                .minimumCompressSize(200) // 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST) //结果回调onActivityResult code
            R.id.tv_cancel -> finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                val selectList = PictureSelector.obtainMultipleResult(data)
                if (selectList.isEmpty()) return
                if (selectList.size == 1) {
                    val intent = Intent()
                    val bundle = Bundle()
                    bundle.putString("path", selectList[0].cutPath)
                    bundle.putString(
                        "name",
                        if (TextUtils.isEmpty(selectList[0].fileName)) selectList[0].cutPath.substringAfterLast(
                            "/"
                        ) else selectList[0].fileName
                    )
                    setResult(RESULT_OK, intent.putExtras(bundle))
                } else if (selectList.size > 1) {
                    EventBus.getDefault().post(EBSelPicCallBack(selectList))
                }
                finish()
            }
        } else {
            finish()
        }
    }
}