package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_exception_handler.*
import kotlin.system.exitProcess

class ExceptionHandlerActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_exception_handler
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_exception)
        text_log.text = intent.getStringExtra("log_summary")
        tb_exception.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                startActivity(Intent(this@ExceptionHandlerActivity, SplashActivity::class.java))
                finish()
            }
        })
    }

    override fun onBackPressed() {
//        exitProcess(0)
    }
}