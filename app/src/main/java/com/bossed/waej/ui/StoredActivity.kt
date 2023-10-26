package com.bossed.waej.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.StoredAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.javebean.StoredBean
import com.bossed.waej.util.OnNoRepeatClickListener
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_stored.*

class StoredActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var storedAdapter: StoredAdapter
    private val beans = ArrayList<StoredBean>()

    override fun getLayoutId(): Int {
        return R.layout.activity_stored
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_stored)
        rv_stored_item.layoutManager = LinearLayoutManager(this)
        beans.add(StoredBean("洗车"))
        beans.add(StoredBean("洗车"))
        beans.add(StoredBean("洗车"))
        storedAdapter = StoredAdapter(beans)
        storedAdapter.bindToRecyclerView(rv_stored_item)
        val footer = LayoutInflater.from(this).inflate(R.layout.layout_footer_view_item, null)
        footer.findViewById<TextView>(R.id.tv_add_item).setOnClickListener(this)
        storedAdapter.setFooterView(footer)
    }

    override fun initListener() {
        tb_stored.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_add_item -> {

            }
        }
    }
}