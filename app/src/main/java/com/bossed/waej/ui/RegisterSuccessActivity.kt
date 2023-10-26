package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.util.DoubleClicksUtils
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_register_success.*

class RegisterSuccessActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_register_success
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_register_success)
    }

    override fun initListener() {
        tv_confirm.setOnClickListener {
            if (DoubleClicksUtils.get().isFastDoubleClick)
                return@setOnClickListener
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("new", "1")
            startActivity(intent)
        }
    }
}