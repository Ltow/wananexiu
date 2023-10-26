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
import com.bossed.waej.ui.preview.SJFragment3
import com.bossed.waej.ui.preview.ZYSPFragment3
import com.bossed.waej.ui.preview.ZYXMFragment3
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_preview3.*

class Preview3Activity : BaseActivity(), OnClickListener {
    private val fragments = ArrayList<Fragment>()
    private lateinit var pagerAdapter: BaseFragmentPagerAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_preview3
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_preview3)
        fragments.add(ZYXMFragment3())
        fragments.add(SJFragment3())
        fragments.add(ZYSPFragment3())
        pagerAdapter = BaseFragmentPagerAdapter(supportFragmentManager, fragments)
        ns_vp_preview3.adapter = pagerAdapter
        change(0)
    }

    override fun initListener() {
        tb_preview3.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        ns_vp_preview3.addOnPageChangeListener(object :OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
               change(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_item -> {
                change(0)
            }

            R.id.tv_shop -> {
                change(1)
            }

            R.id.tv_goods -> {
                change(2)
            }
        }
    }

    private fun change(i: Int) {
        ns_vp_preview3.currentItem=i
        tv_item.setTextColor(Color.parseColor("#333333"))
        tv_item.setBackgroundResource(R.drawable.shape_corners_f6f6f6_dp5)
        tv_shop.setTextColor(Color.parseColor("#333333"))
        tv_shop.setBackgroundResource(R.drawable.shape_corners_f6f6f6_dp5)
        tv_goods.setTextColor(Color.parseColor("#333333"))
        tv_goods.setBackgroundResource(R.drawable.shape_corners_f6f6f6_dp5)
        when (i) {
            0 -> {
                tv_item.setTextColor(Color.parseColor("#198CFF"))
                tv_item.setBackgroundResource(R.drawable.shape_corners_eef9ff_dp5)
            }

            1 -> {
                tv_shop.setTextColor(Color.parseColor("#198CFF"))
                tv_shop.setBackgroundResource(R.drawable.shape_corners_eef9ff_dp5)
            }

            2 -> {
                tv_goods.setTextColor(Color.parseColor("#198CFF"))
                tv_goods.setBackgroundResource(R.drawable.shape_corners_eef9ff_dp5)
            }
        }
    }
}