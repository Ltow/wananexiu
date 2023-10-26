package com.bossed.waej.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import com.blankj.utilcode.util.Utils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_shop_perfect.*
import java.util.ArrayList

class ShopPerfectActivity : BaseActivity(), View.OnClickListener {
    private val checkBoxes = ArrayList<CheckBox>()

    override fun getLayoutId(): Int {
        return R.layout.activity_shop_perfect
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_shop_perfect)
        val array = arrayOf("汽车主机厂", "维修保养", "汽配生产商", "汽配经销商", "汽配城", "维护工具", "轮胎专营", "其他")
        for (str: String in array) {
            val checkbox = LayoutInflater.from(Utils.getApp()).inflate(
                R.layout.layout_checkbox,
                fl_shop_perfect, false
            ) as CheckBox
            checkbox.text = str
            checkBoxes.add(checkbox)
            fl_shop_perfect.addView(checkbox)
        }
    }

    override fun initListener() {
        tb_shop_perfect.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {

            }

            override fun onRightClick(view: View?) {

            }
        })
        for (checkBox: CheckBox in checkBoxes) {
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                buttonView.setTextColor(
                    if (isChecked) Color.parseColor("#FC8A25") else Color.parseColor(
                        "#666666"
                    )
                )
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_perfect_next -> {
                startActivity(Intent(this, RegisterSuccessActivity::class.java))
            }
        }
    }
}