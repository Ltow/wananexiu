package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseFragmentPagerAdapter
import com.bossed.waej.ui.preview.PJFragment2
import com.bossed.waej.ui.preview.SJFragment2
import com.bossed.waej.ui.preview.ZYXMFragment2
import com.bossed.waej.ui.preview.ZYSPFragment2
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_preview2.*

/**
 * 模板2
 */
class Preview2Activity : BaseActivity(), OnClickListener {
    private val fragments = ArrayList<Fragment>()
    private lateinit var pagerAdapter: BaseFragmentPagerAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_preview2
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(false).init()
        setMarginTop(tb_preview2)
        fragments.add(ZYXMFragment2())
        fragments.add(ZYSPFragment2())
        fragments.add(PJFragment2())
        fragments.add(SJFragment2())
        pagerAdapter = BaseFragmentPagerAdapter(supportFragmentManager, fragments)
        ns_vp_preview2.adapter = pagerAdapter
        switch(0)
    }

    override fun initListener() {
        tb_preview2.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        ns_vp_preview2.addOnPageChangeListener(object : OnPageChangeListener {
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
        ns_vp_preview2.currentItem = i
        v_zyfw.visibility = View.GONE
        v_zysp.visibility = View.GONE
        v_pj.visibility = View.GONE
        v_sj.visibility = View.GONE
        when (i) {
            0 -> v_zyfw.visibility = View.VISIBLE
            1 -> v_zysp.visibility = View.VISIBLE
            2 -> v_pj.visibility = View.VISIBLE
            3 -> v_sj.visibility = View.VISIBLE
        }
    }
}