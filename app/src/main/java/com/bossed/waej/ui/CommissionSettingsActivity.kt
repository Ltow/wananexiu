package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_commision_settings.*

class CommissionSettingsActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_commision_settings
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_commission_settings)
    }

    override fun initListener() {
        tb_commission_settings.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }
}