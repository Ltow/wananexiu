package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.util.DoubleClicksUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_todo_info.*

class TodoAfterInfoActivity : BaseActivity(), OnClickListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_todo_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_todo_info)
    }

    override fun initListener() {
        tb_todo_info.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
                TODO("Not yet implemented")
            }

            override fun onRightClick(view: View?) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_commit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
            }
        }
    }
}