package com.bossed.waej.customview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2021-11-19
 * Time: 10:41
 */
class NoSlideViewPager : ViewPager {
    private var isCanScroll = false
    private var mDownPosX = 0f
    private var mDownPosY = 0f


    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    fun setNoScroll(isCanScroll: Boolean) {
        this.isCanScroll = isCanScroll
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (isCanScroll) {
            false
        } else {
            super.onInterceptTouchEvent(ev)
        }
    }

    /**
     * 是否消费事件
     * 消费:事件就结束
     * 不消费:往父控件传
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return if (isCanScroll) {
            false
        } else {
            super.onTouchEvent(ev)
        }
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, false)
    }
}