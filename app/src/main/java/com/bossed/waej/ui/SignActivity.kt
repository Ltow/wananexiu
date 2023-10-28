package com.bossed.waej.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBSign
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_sign.*
import org.greenrobot.eventbus.EventBus


class SignActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_sign
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_sign)
        val webSettings: WebSettings = wb_sign.settings
        webSettings.useWideViewPort = true //将图片调整到适合webView的大小
        webSettings.loadWithOverviewMode = true //缩放至屏幕的大小
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        runOnUiThread {
            wb_sign.loadUrl(intent.getStringExtra("signUrl"))
            wb_sign.webViewClient = object : WebViewClient() {
                //处理加载http的url问题
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    view!!.loadUrl(intent.getStringExtra("signUrl"))
                    return true
                }
            }
        }
    }

    override fun initListener() {
        tb_sign.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        EventBus.getDefault().post(EBSign(true))
    }
}