package com.bossed.waej.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bossed.waej.R
import com.bossed.waej.adapter.PreviewMainPicAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseFragmentPagerAdapter
import com.bossed.waej.ui.preview.PJFragment
import com.bossed.waej.ui.preview.SJFragment
import com.bossed.waej.ui.preview.ZYXMFragment
import com.bossed.waej.ui.preview.ZYSPFragment
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_preview.*

/**
 * 默认模板
 */
class PreviewActivity : BaseActivity(), OnClickListener {
    private lateinit var mainPicAdapter: PreviewMainPicAdapter
    private val mainPics = ArrayList<Int>()
    private val fragments = ArrayList<Fragment>()
    private lateinit var pagerAdapter: BaseFragmentPagerAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_preview
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_preview)
        rv_main_pic.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mainPics.add(R.mipmap.icon_preview_pic1)
        mainPics.add(R.mipmap.icon_preview_pic2)
        mainPics.add(R.mipmap.icon_preview_pic3)
        mainPicAdapter = PreviewMainPicAdapter(mainPics)
        mainPicAdapter.bindToRecyclerView(rv_main_pic)
        fragments.add(ZYXMFragment())
        fragments.add(ZYSPFragment())
        fragments.add(PJFragment())
        fragments.add(SJFragment())
        pagerAdapter = BaseFragmentPagerAdapter(supportFragmentManager, fragments)
        ns_vp_preview.adapter = pagerAdapter
        switch(0)
    }

    override fun initListener() {
        tb_preview.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        ns_vp_preview.addOnPageChangeListener(object :OnPageChangeListener{
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
        ns_vp_preview.currentItem = i
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