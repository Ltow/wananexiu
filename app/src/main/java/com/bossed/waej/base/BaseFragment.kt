package com.bossed.waej.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.BarUtils
import com.hjq.bar.TitleBar


abstract class BaseFragment : Fragment() {
    /**
     * 视图是否加载完毕
     */
    private var isViewPrepare = false

    /**
     * 数据是否加载过了
     */
    private var hasLoadData = false
    lateinit var mContext: Context
    lateinit var mActivity: Activity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
        initListener()
    }

    private fun lazyLoadDataPrepared() {
        if (!isHidden && !isViewPrepare && !hasLoadData) {
            lazyLoad()
            isViewPrepare = true
            hasLoadData = true
        }
    }

    /**
     * 懒加载
     */
    abstract fun lazyLoad()

    /**
     * 加载布局
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * 初始化 View
     */
    open fun initView(view: View?, savedInstanceState: Bundle?) {

    }

    open fun initListener() {}

    /**
     * 请求网络数据
     */
//    abstract fun startNetwork()
    var i = 1
    override fun onResume() {
        super.onResume()
        lazyLoadDataPrepared()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewPrepare = false
    }

    /**
     * 动态设置titleBar距顶部一个状态栏的高度（px）
     */
    fun setTopMargin(view: View) {
        when (view.parent) {
            is LinearLayout -> {
                val layoutParams: LinearLayout.LayoutParams =
                    view.layoutParams as LinearLayout.LayoutParams
                layoutParams.topMargin = BarUtils.getStatusBarHeight()
                view.layoutParams = layoutParams
            }
            else -> {
                val layoutParams: RelativeLayout.LayoutParams =
                    view.layoutParams as RelativeLayout.LayoutParams
                layoutParams.topMargin = BarUtils.getStatusBarHeight()
                view.layoutParams = layoutParams
            }
        }

    }
}