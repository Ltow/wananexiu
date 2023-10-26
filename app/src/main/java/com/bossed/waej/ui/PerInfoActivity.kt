package com.bossed.waej.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.eventbus.EBUpdateUser
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.luck.picture.lib.config.PictureConfig
import kotlinx.android.synthetic.main.activity_per_info.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PerInfoActivity : BaseActivity(), OnNoRepeatClickListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_per_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).init()
        BarUtils.setStatusBarLightMode(window, true)
        setMarginTop(tb_per_info)
        GlideUtils.get()
            .loadHead(this, intent.getStringExtra("avatar"), iv_head)
        tv_nickName.text = intent.getStringExtra("nickName")
        tv_phonenumber.text = intent.getStringExtra("phonenumber")
    }

    override fun initListener() {
        tb_per_info.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.ll_phone -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, SetNameActivity::class.java)
                intent.putExtra("title", "设置电话")
                intent.putExtra("phone", tv_phonenumber.text.toString())
                intent.putExtra("name", tv_nickName.text.toString())
                startActivity(intent)
            }
            R.id.ll_name -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, SetNameActivity::class.java)
                intent.putExtra("title", "设置姓名")
                intent.putExtra("phone", tv_phonenumber.text.toString())
                intent.putExtra("name", tv_nickName.text.toString())
                startActivity(intent)
            }
            R.id.ll_update_pwd -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this, UpDatePwdActivity::class.java))
            }
            R.id.ll_head -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, SelectPicActivity::class.java)
                intent.putExtra("selNum", 1)
                intent.putExtra("selMode", PictureConfig.SINGLE)
                launcher.launch(intent)
            }
            R.id.iv_head -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get()
                    .showPhotoPop(intent.getStringExtra("avatar"), this, rl_content)
            }
            R.id.ll_inviteCode -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return

            }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                RESULT_OK -> {
                    val bundle = it.data?.extras
                    upLoadPic(bundle!!.getString("name")!!, bundle.getString("path")!!)
                    GlideUtils.get().loadHead(this, bundle.getString("path")!!, iv_head)
                }
            }
        }

    private fun upLoadPic(imgName: String, imgLocalPath: String) {
        val dialog = Dialog(this, R.style.Dialog)
        val contentView = View.inflate(this, R.layout.layout_progress_dialog, null)
        dialog.setContentView(contentView)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val progressBar = contentView.findViewById<ProgressBar>(R.id.pb_progress)
        dialog.show()
        val objectKey = "waej3/" + SPUtils.getInstance()
            .getInt("shopId") + "/" + System.currentTimeMillis() + imgName
        val imgUrl = UrlConstants.BaseOssUrl + objectKey
        OssUtils.get().asyncPutImage(objectKey, imgLocalPath, this,
            object : OssUtils.OnOssCallBackListener {
                override fun onSuccess(float: Float) {
                    dialog.dismiss()
                    ToastUtils.showShort("上传成功，用时：${float}秒")
                    val intent = Intent()
                    setResult(RESULT_OK, intent.putExtra("imgUrl", imgUrl))
                    finish()
                }

                override fun onFailed(e: String) {
                    dialog.dismiss()
                    ToastUtils.showShort(e)
                }

                override fun onProgress(progress: Int) {
                    progressBar.progress = progress
                }
            })
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(eb: EBUpdateUser) {
        if (eb.isFinish)
            finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

//    override fun onResume() {
//        super.onResume()
//        tv_inviteCode.text = SPUtils.getInstance().getString("inviteCode")
//    }
}