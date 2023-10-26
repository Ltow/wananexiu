package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SPUtils
import com.bossed.waej.R
import com.gyf.immersionbar.ImmersionBar
import me.jessyan.autosize.internal.CancelAdapt

class SplashActivity : AppCompatActivity(), CancelAdapt {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        if (SPUtils.getInstance().getBoolean("remember"))
            startActivity(Intent(this, MainActivity::class.java))
        else
            startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}