package com.bossed.waej.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseFragmentPagerAdapter
import com.bossed.waej.ui.preview.PJFragment5
import com.bossed.waej.ui.preview.SJFragment5
import com.bossed.waej.ui.preview.ZYSPFragment
import com.bossed.waej.ui.preview.ZYXMFragment
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_preview5.*

class Preview5Activity : BaseActivity(), OnClickListener {
    private val fragments = ArrayList<Fragment>()
    private lateinit var pagerAdapter: BaseFragmentPagerAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_preview5
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_preview5)
        fragments.add(ZYXMFragment())
        fragments.add(ZYSPFragment())
        fragments.add(PJFragment5())
        fragments.add(SJFragment5())
        pagerAdapter = BaseFragmentPagerAdapter(supportFragmentManager, fragments)
        svp_preview5.adapter = pagerAdapter
        switch(0)
    }

    override fun initListener() {
        tb_preview5.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.ll_zyfw -> {
                switch(0)
            }

            R.id.ll_zysp -> {
                switch(1)
            }

            R.id.ll_pj -> {
                switch(2)
            }

            R.id.ll_sj -> {
                switch(3)
            }
        }
    }

    private fun switch(i: Int) {
        svp_preview5.currentItem = i
        tv_zyfw.textSize = 12f
        tv_zyfw.setTextColor(Color.parseColor("#666666"))
        tv_zyfw.typeface = Typeface.DEFAULT
        v_zyfw.visibility = View.GONE
        tv_zysp.textSize = 12f
        tv_zysp.setTextColor(Color.parseColor("#666666"))
        tv_zysp.typeface = Typeface.DEFAULT
        v_zysp.visibility = View.GONE
        tv_pj.textSize = 12f
        tv_pj.setTextColor(Color.parseColor("#666666"))
        tv_pj.typeface = Typeface.DEFAULT
        v_pj.visibility = View.GONE
        tv_sj.textSize = 12f
        tv_sj.setTextColor(Color.parseColor("#666666"))
        tv_sj.typeface = Typeface.DEFAULT
        v_sj.visibility = View.GONE
        when (i) {
            0 -> {
                tv_zyfw.textSize = 15f
                tv_zyfw.setTextColor(Color.parseColor("#333333"))
                tv_zyfw.typeface = Typeface.DEFAULT_BOLD
                v_zyfw.visibility = View.VISIBLE
            }

            1 -> {
                tv_zysp.textSize = 15f
                tv_zysp.setTextColor(Color.parseColor("#333333"))
                tv_zysp.typeface = Typeface.DEFAULT_BOLD
                v_zysp.visibility = View.VISIBLE
            }

            2 -> {
                tv_pj.textSize = 15f
                tv_pj.setTextColor(Color.parseColor("#333333"))
                tv_pj.typeface = Typeface.DEFAULT_BOLD
                v_pj.visibility = View.VISIBLE
            }

            3 -> {
                tv_sj.textSize = 15f
                tv_sj.setTextColor(Color.parseColor("#333333"))
                tv_sj.typeface = Typeface.DEFAULT_BOLD
                v_sj.visibility = View.VISIBLE
            }
        }
    }
}