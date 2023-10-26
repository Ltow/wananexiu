package com.bossed.waej.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBSelModelBack
import com.bossed.waej.javebean.AppletQRCodeResponse
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.GlideUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_applet_qrcode.iv_qrCode
import kotlinx.android.synthetic.main.activity_applet_qrcode.tb_applet
import kotlinx.android.synthetic.main.activity_applet_qrcode.tv_fullname
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.File

class AppletQRCodeActivity : BaseActivity(), OnClickListener {
    private var url = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_applet_qrcode
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_applet)
        tv_fullname.text = intent.getStringExtra("fullname") + "汽服商城"
        if (TextUtils.isEmpty(intent.getStringExtra("shopMiniQrcode")))
            getQrCode()
        else {
            GlideUtils.get().loadItemPic(this, intent.getStringExtra("shopMiniQrcode"), iv_qrCode)
            url = intent.getStringExtra("shopMiniQrcode")
        }

    }

    override fun initListener() {
        tb_applet.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {

            }

            override fun onRightClick(view: View?) {

            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_commit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, ShopRenovationActivity::class.java)
                intent.putExtra("pageTemplateId", getIntent().getIntExtra("pageTemplateId", -1))
                intent.putExtra("shopId", getIntent().getStringExtra("shopId"))
                intent.putExtra("fullname", getIntent().getStringExtra("fullname"))
                startActivity(intent)
            }

            R.id.btn_submit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                Thread {//必须用子线程启动
                    GlideUtils.get()
                        .startDownload(
                            this@AppletQRCodeActivity, url,
                            object : GlideUtils.ImageDownLoadCallBack {
                                override fun onDownLoadSuccess(file: File) {
                                    ToastUtils.showShort("已保存到本地相册")
                                }

                                override fun onDownLoadFailed() {
                                    ToastUtils.showShort("保存失败")
                                }
                            })
                }.start()
            }
        }
    }

    private fun getQrCode() {
        LoadingUtils.showLoading(this, "加载中...")
        RetrofitUtils.get().getJson(
            "km/user/getwxacodeunlimit", HashMap(), this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, AppletQRCodeResponse::class.java)
                    url = t.data!!
                    GlideUtils.get().loadItemPic(this@AppletQRCodeActivity, t.data!!, iv_qrCode)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    @Subscribe
    fun onMessageEvent(eb: EBSelModelBack) {
        if (eb.isFinish)
            finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}