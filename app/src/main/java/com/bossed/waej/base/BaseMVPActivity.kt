package com.bossed.waej.base

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.bossed.waej.R
import com.hjq.bar.TitleBar


abstract class BaseMVPActivity<P : BaseContract.BaseMvpPresenter> : AppCompatActivity(),
    BaseContract.BaseView {
    var mPresenter: P? = null
    lateinit var mContext: Context
    lateinit var mActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        //适配全面屏
        val lp: WindowManager.LayoutParams = this.window.attributes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        this.window.attributes = lp
        //隐藏导航栏
        WindowInsetsControllerCompat(
            window, window.decorView
        ).hide(WindowInsetsCompat.Type.navigationBars())
        if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        super.onCreate(savedInstanceState)
        mContext = this.applicationContext
        mActivity = this
        mPresenter = getPresenter()
        mPresenter?.attachView(this)
        setContentView(getLayoutId())
        initView(savedInstanceState)
        initListener()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        currentFocus?.apply {
            if (this is EditText) {
                if (ev.action == MotionEvent.ACTION_DOWN) {
                    if (isShouldHideKeyboard(this, ev)) {
                        this.clearFocus()
                        KeyboardUtils.hideSoftInput(window)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 判断是否点击的是EditText以外的区域
     */
    private fun isShouldHideKeyboard(v: View, event: MotionEvent): Boolean {
        if (v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return (event.x <= left || event.x >= right
                    || event.y <= top || event.y >= bottom)
        }
        return false
    }

    abstract fun getLayoutId(): Int

    open fun initView(savedInstanceState: Bundle?) {}
    open fun initListener() {}

    abstract fun getPresenter(): P

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null) {
            mPresenter!!.cancelAll()
            mPresenter!!.detachView()
        }
    }

    /**
     * 动态设置titleBar距顶部一个状态栏的高度（px）
     */
    fun setTitleBarMarginTop(titleBar: View) {
        val layoutParams: RelativeLayout.LayoutParams =
            titleBar.layoutParams as RelativeLayout.LayoutParams
        layoutParams.topMargin = BarUtils.getStatusBarHeight()
        titleBar.layoutParams = layoutParams
    }

    fun setTextColorAndSize(textView: TextView, color: String, textSize: Float) {
        textView.setTextColor(Color.parseColor(color))
        textView.textSize = textSize
    }

    fun buttonClickState(isChecked: Boolean, view: View) {
        if (isChecked)
            view.setBackgroundResource(R.drawable.shape_blue_gradient_bg)
        else
            view.setBackgroundResource(R.drawable.shape_stroke_cccccc_dp6)
    }
}