package com.bossed.waej.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseFragmentPagerAdapter
import com.bossed.waej.ui.preview.PJFragment4
import com.bossed.waej.ui.preview.SJFragment4
import com.bossed.waej.ui.preview.ZYSPFragment4
import com.bossed.waej.ui.preview.ZYXMFragment4
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_preview4.*

class Preview4Activity : BaseActivity(), OnClickListener {
    private val fragments = ArrayList<Fragment>()
    private lateinit var pagerAdapter: BaseFragmentPagerAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_preview4
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_preview4)
        switch(0)
        fragments.add(ZYXMFragment4())
        fragments.add(ZYSPFragment4())
        fragments.add(PJFragment4())
        fragments.add(SJFragment4())
        pagerAdapter = BaseFragmentPagerAdapter(supportFragmentManager, fragments)
        svp_preview4.adapter = pagerAdapter
        switch(0)
    }

    override fun initListener() {
        tb_preview4.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        svp_preview4.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                switch(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rl_fw -> {
                switch(0)
            }

            R.id.rl_sp -> {
                switch(1)
            }

            R.id.rl_pj -> {
                switch(2)
            }

            R.id.rl_sj -> {
                switch(3)
            }
        }
    }

    private fun switch(i: Int) {
        svp_preview4.currentItem = i
        tv_fw.setTextColor(Color.parseColor("#333333"))
        tv_fw.setBackgroundResource(R.drawable.shape_corners_ffffff_dp12)
        tv_sp.setTextColor(Color.parseColor("#333333"))
        tv_sp.setBackgroundResource(R.drawable.shape_corners_ffffff_dp12)
        tv_pj.setTextColor(Color.parseColor("#333333"))
        tv_pj.setBackgroundResource(R.drawable.shape_corners_ffffff_dp12)
        tv_sj.setTextColor(Color.parseColor("#333333"))
        tv_sj.setBackgroundResource(R.drawable.shape_corners_ffffff_dp12)
        when (i) {
            0 -> {
                tv_fw.setTextColor(Color.parseColor("#E55C2E"))
                tv_fw.setBackgroundResource(R.drawable.shape_corners_fff0eb_dp12)
            }

            1 -> {
                tv_sp.setTextColor(Color.parseColor("#E55C2E"))
                tv_sp.setBackgroundResource(R.drawable.shape_corners_fff0eb_dp12)
            }

            2 -> {
                tv_pj.setTextColor(Color.parseColor("#E55C2E"))
                tv_pj.setBackgroundResource(R.drawable.shape_corners_fff0eb_dp12)
            }

            3 -> {
                tv_sj.setTextColor(Color.parseColor("#E55C2E"))
                tv_sj.setBackgroundResource(R.drawable.shape_corners_fff0eb_dp12)
            }
        }
    }
}