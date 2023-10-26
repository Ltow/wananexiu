package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.AssociationAdapter
import com.bossed.waej.base.BaseActivity
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_association_part.*

class AssociationPartActivity : BaseActivity() {
    private lateinit var adapter: AssociationAdapter
    private val list = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_association_part
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_association)
        rv_association.layoutManager = LinearLayoutManager(this)
        list.add("机油")
        list.add("机滤")
        list.add("燃油滤")
        adapter = AssociationAdapter(list)
        adapter.bindToRecyclerView(rv_association)
        adapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        tb_association.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }
}