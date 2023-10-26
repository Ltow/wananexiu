package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.util.DoubleClicksUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_add_shop_package.*

class AddShopPackageActivity : BaseActivity(), View.OnClickListener {

    override fun getLayoutId(): Int {
        return R.layout.activity_add_shop_package
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_add_shop_package)
    }

    override fun initListener() {
        tb_add_shop_package.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.tv_association -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this, AssociationPartActivity::class.java))
            }
        }
    }
}