package com.bossed.waej.ui

import android.os.Bundle
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.util.GlideUtils
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_photo_view.*

class PhotoViewActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_photo_view
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        GlideUtils.get().loadShopPic(this, intent.getStringExtra("url")!!, pv_photo_view)
    }

    override fun initListener() {
        pv_photo_view.setOnClickListener {
            finish()
        }
    }
}