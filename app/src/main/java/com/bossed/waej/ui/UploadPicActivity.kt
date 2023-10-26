package com.bossed.waej.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.luck.picture.lib.config.PictureConfig
import kotlinx.android.synthetic.main.activity_upload_license.*

class UploadPicActivity : BaseActivity(), OnNoRepeatClickListener {
    private var imgUrl = ""
    private var imgName = ""
    private var imgLocalPath = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_upload_license
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_upload_license)
        when (intent.getStringExtra("type")) {
            "license" -> {
                GlideUtils.get().loadLicensePic(this, intent.getStringExtra("url")!!, photo_view)
            }
            "doorTitle" -> {
                GlideUtils.get().loadDoorTitlePic(this, intent.getStringExtra("url")!!, photo_view)
                tb_upload_license.title = "门头照片"
            }
            "certificate" -> {
                tb_upload_license.title = "证书"
                GlideUtils.get().loadDoorTitlePic(this, intent.getStringExtra("url")!!, photo_view)
            }
        }
    }

    override fun initListener() {
        photo_view.setOnClickListener(this)
        tb_upload_license.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.photo_view -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, SelectPicActivity::class.java)
                intent.putExtra("selNum", 1)
                intent.putExtra("selMode", PictureConfig.SINGLE)
                startActivityForResult(intent, 101)
            }
            R.id.tv_upload -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (TextUtils.isEmpty(imgName) || TextUtils.isEmpty(imgLocalPath))
                    return
                val dialog = Dialog(this, R.style.Dialog)
                val contentView = View.inflate(this, R.layout.layout_progress_dialog, null)
                dialog.setContentView(contentView)
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)
                val progressBar = contentView.findViewById<ProgressBar>(R.id.pb_progress)
                dialog.show()
                val objectKey = "waej3/" + SPUtils.getInstance()
                    .getInt("shopId") + "/" + System.currentTimeMillis() + imgName
                imgUrl = UrlConstants.BaseOssUrl + objectKey
                OssUtils.get().asyncPutImage(objectKey, imgLocalPath, this,
                    object : OssUtils.OnOssCallBackListener {
                        override fun onSuccess(float: Float) {
                            ToastUtils.showShort("上传成功，用时：${float}秒")
                            val intent = Intent()
                            setResult(RESULT_OK, intent.putExtra("url", imgUrl))
                            dialog.dismiss()
                            finish()
                        }

                        override fun onFailed(e: String) {
                            dialog.dismiss()
                            ToastUtils.showShort(e)
                        }

                        override fun onProgress(progress: Int) {
//                            val mes = handler.obtainMessage(0, progress)
//                            mes.arg1 = progress
//                            mes.sendToTarget()
                            progressBar.progress = progress
                        }
                    })
            }
        }
    }
//
//    val handler = object : Handler(Looper.getMainLooper()) {
//        override fun handleMessage(inputMessage: Message) {
//            when (inputMessage.what) {
//                0 -> {
//                    progressDialog?.progress = inputMessage.arg1
//                }
//            }
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK) {
            val bundle = data?.extras
            imgName = bundle!!.getString("name")!!
            imgLocalPath = bundle.getString("path")!!
            when (intent.getStringExtra("type")) {
                "license" -> {
                    GlideUtils.get().loadLicensePic(this, imgLocalPath, photo_view)
                }
                "doorTitle" -> {
                    GlideUtils.get().loadDoorTitlePic(this, imgLocalPath, photo_view)
                }
                "certificate" -> {
                    GlideUtils.get().loadDoorTitlePic(this, imgLocalPath, photo_view)
                }
            }
        }
    }

}