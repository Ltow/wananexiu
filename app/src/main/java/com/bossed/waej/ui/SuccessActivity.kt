package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.BarUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBBuyBack
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_success.*
import org.greenrobot.eventbus.EventBus

class SuccessActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_success
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        setMarginTop(tb_success)
        BarUtils.setStatusBarLightMode(window, true)
        tv_money.text=intent.getStringExtra("money")
    }

    override fun initListener() {
        tv_back.setOnClickListener {
            EventBus.getDefault().post(EBBuyBack(true))
            finish()
        }
        tv_see.setOnClickListener {
            val intent = Intent(this, OrderInfoActivity::class.java)
            intent.putExtra("data", getIntent().getStringExtra("data"))
            startActivity(intent)
            EventBus.getDefault().post(EBBuyBack(true))
            finish()
        }
    }
}