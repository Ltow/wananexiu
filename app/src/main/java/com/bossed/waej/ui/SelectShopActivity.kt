package com.bossed.waej.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.bossed.waej.R
import com.bossed.waej.adapter.ContactSortAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.javebean.SortModel
import com.bossed.waej.util.RecyclerViewUtil
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_sel_shop.*

class SelectShopActivity : BaseActivity() {
    private lateinit var mAdater: ContactSortAdapter
    private var mDataList: MutableList<SortModel>? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_sel_shop
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_sel_shop)
        initData()
    }

    private fun initData() {
        val arrayData = arrayOf(
            "a",
            "bd",
            "ced",
            "de",
            "as",
            "东皇太一",
            "宫本武藏",
            "王昭君",
            "李元芳",
            "刘禅",
            "后裔",
            "许爱明",
            "无名",
            "流海",
            "亚瑟",
            "吕布",
            "秋雅",
            "夏洛",
            "公孙离",
            "张良",
            "孙尚香",
            "我",
            "你",
            "啊",
            "哈哈",
            "嘿",
            "无名",
            "流海",
            "亚瑟",
            "吕布",
            "夏洛",
            "公孙离",
            "张良",
            "孙尚香",
            "无名",
            "流海",
            "亚瑟",
            "吕布",
            "刘备",
            "夏洛",
            "公孙离",
            "张良",
            "孙尚香",
            "无名",
            "流海",
            "亚瑟",
            "吕布",
            "秋雅",
            "夏洛",
            "公孙离",
            "张良",
            "孙尚香",
            "无名",
            "流海",
            "亚瑟",
            "吕布",
            "秋雅",
            "夏洛",
            "公孙离",
            "张良",
            "孙尚香"
        )
        val data = mutableListOf<String>()
        for (i in arrayData) {
            data.add(i)
        }
        mAdater = ContactSortAdapter()
        RecyclerViewUtil.initNoDecoration(this, contact_view.getRecycler(), mAdater)
        mDataList = contact_view.sortData(data)
        contact_view?.initData(mDataList)
        mAdater.initData(mDataList)
    }

    override fun initListener() {
        tb_sel_shop.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {

            }

            override fun onRightClick(view: View?) {

            }
        })
        et_search_shop.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    contact_view?.initData(mDataList)
                    mAdater.initData(mDataList)
                } else {
                    mAdater.initData(contact_view?.updateData(s.toString()))
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}