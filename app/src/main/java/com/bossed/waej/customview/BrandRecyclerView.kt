package com.bossed.waej.customview

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossed.waej.R
import com.bossed.waej.javebean.Brand
import com.bossed.waej.util.BrandComparator
import com.bossed.waej.util.CharacterParser
import kotlinx.android.synthetic.main.brand_list_layout.view.*
import java.util.*

class BrandRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), SideBarView.LetterTouchListener {

    private var ll_top_title: LinearLayout? = null
    private var tv_letter: TextView? = null
    private var mLetterHeight = 0
    private var mCurrentPosition = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mData: MutableList<Brand> = mutableListOf()
    private var mParser: CharacterParser = CharacterParser.getInstance()

    init {
        LayoutInflater.from(context).inflate(R.layout.brand_list_layout, this)
        ll_top_title = findViewById(R.id.ll_top_title)
        tv_letter = findViewById(R.id.tv_letter)
        view_sidebar?.setLetterTouchListener(this)
        recycler_view?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mLetterHeight = ll_top_title?.height!!
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                //找到列表下一个可见的View
                val view = mLayoutManager?.findViewByPosition(mCurrentPosition + 1)
                // 检查列表中的letter布局是否显示
                if (view != null && view.top <= mLetterHeight && view.findViewById<TextView>(R.id.tv_letter)?.visibility == View.VISIBLE) {
                    //被顶掉的效果
                    ll_top_title?.y = (-(mLetterHeight - view.top)).toFloat()
                } else {
                    ll_top_title?.y = 0f
                }
                //判断是否需要更新悬浮条
                if (mCurrentPosition != mLayoutManager?.findFirstVisibleItemPosition()) {
                    ll_top_title?.y = 0f
                    updateLetter()
                }
            }

        })
    }

    val getRecycler = {
        recycler_view
    }

    val getHotRecycler = {
        rv_hot_brand
    }

    override fun setLetterVisibility(visibility: Int) {
        tv_letter_show?.visibility = visibility
    }

    override fun setLetter(letter: String) {
        if (TextUtils.isEmpty(letter) || letter.isEmpty()) {
            return
        }
        tv_letter_show?.text = letter
        val position = getPositionForSection(letter[0].toInt())
        if (position != -1) {
            updateLetter()
            mLetterHeight = ll_top_title?.height!!
            mLayoutManager?.scrollToPositionWithOffset(position, 0) // 使当前位置处于最顶端
        }
    }

    /**
     * 刷新 字母title
     */
    private val updateLetter = {
        mCurrentPosition = mLayoutManager?.findFirstVisibleItemPosition() ?: -1
        if (mData.size > 0 && mCurrentPosition > -1 && mCurrentPosition < mData.size) {
            tv_letter?.text = mData[mCurrentPosition].letter
        }
    }

    /**
     * 数据排序
     */
    val sortData = { data: MutableList<Brand> ->
        val list = mutableListOf<Brand>()
        for (i in data.indices) {
            val sm = Brand()
            sm.name = data[i].name
            sm.logo = data[i].logo
            sm.hot = data[i].hot
            sm.code = data[i].code
            sm.id = data[i].id
            sm.isShow = data[i].isShow
            val pinyin = mParser.getSelling(data[i].name)
            val sortString = pinyin.substring(0, 1).toUpperCase()
            if (sortString.matches("[A-Z]".toRegex())) {
                sm.letter = sortString
            } else {
                sm.letter = "#"
            }
            list.add(sm)
        }
        Collections.sort(list, BrandComparator())
        list
    }

    /**
     * 初始化数据
     */
    val initData = { data: MutableList<Brand> ->
        mData.clear()
        mData.addAll(data)
        updateLetter()
    }

    /**
     * 根据输入的内容刷新数据
     */
    val updateData = { filterStr: String ->
        var newData = mutableListOf<Brand>()
        if (mData != null && mData.size > 0) {
            if ("" == filterStr) {
                newData = mData
            } else {
                for (sortModel in mData) {
                    val name = sortModel.name
                    if (name.indexOf(filterStr) != -1 || mParser.getSelling(name)
                            .startsWith(filterStr)
                    ) {
                        newData.add(sortModel)
                    }
                }
            }
        }
        mData.clear()
        mData.addAll(newData)
        updateLetter()
        mData
    }

    /**
     * 获取字母首次出现的位置
     */
    private fun getPositionForSection(it: Int): Int {
        if (mData == null || mData.size == 0) {
            return -1
        } else {
            for (i in 0 until mData.size) {
                val s = mData[i].letter
                val firstChar = s.uppercase(Locale.getDefault())[0]
                if (firstChar.code == it) {
                    return i
                }
            }
        }
        return -1
    }
}