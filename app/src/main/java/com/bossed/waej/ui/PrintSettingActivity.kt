package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.SPUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.States
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_print_setting.*

class PrintSettingActivity : BaseActivity(), View.OnClickListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_print_setting
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_print_setting)
        when (SPUtils.getInstance()
            .getString("PRINTER_NORMS", States.PrinterPageSize.PRINTER_58mm)) {
            States.PrinterPageSize.PRINTER_58mm -> {
                ctv_58.isChecked = true
                ll_58mm.visibility = View.VISIBLE
            }
            States.PrinterPageSize.PRINTER_80mm -> {
                ctv_80.isChecked = true
                ll_80mm.visibility = View.VISIBLE
            }
        }
        tv_fullname.text = "****${SPUtils.getInstance().getString("fullname")}****"
        tv_fullname2.text = "****${SPUtils.getInstance().getString("fullname")}****"
        tv_fullname3.text = "****${SPUtils.getInstance().getString("fullname")}****"
        tv_fullname4.text = "****${SPUtils.getInstance().getString("fullname")}****"
    }

    override fun initListener() {
        tb_print_setting.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
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
            R.id.ctv_58 -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_58.isChecked = true
                ctv_80.isChecked = false
                SPUtils.getInstance().put("PRINTER_NORMS", States.PrinterPageSize.PRINTER_58mm)
                ll_58mm.visibility = View.VISIBLE
                ll_80mm.visibility = View.GONE
            }
            R.id.ctv_80 -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_80.isChecked = true
                ctv_58.isChecked = false
                SPUtils.getInstance().put("PRINTER_NORMS", States.PrinterPageSize.PRINTER_80mm)
                ll_58mm.visibility = View.GONE
                ll_80mm.visibility = View.VISIBLE
            }
        }
    }
}