package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.TodoAfterAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.util.DoubleClicksUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_todo_activity.*

class TodoAfterActivity : BaseActivity(), OnClickListener {
    private lateinit var adapter: TodoAfterAdapter
    private val list = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_todo_activity
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_todo)
        rv_todo.layoutManager = LinearLayoutManager(this)
        list.add("大沙发上")
        list.add("大沙发上")
        list.add("大沙发上")
        adapter = TodoAfterAdapter(list)
        adapter.bindToRecyclerView(rv_todo)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        tb_todo.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        adapter.setOnItemClickListener { adapter, view, position ->
            startActivity(Intent(this, TodoAfterInfoActivity::class.java))
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_todo -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(0)
            }

            R.id.tv_do -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(1)
            }
        }
    }

    private fun switch(i: Int) {
        tv_todo.setBackgroundResource(R.drawable.shape_corners_cccccc_dp12_5)
        tv_do.setBackgroundResource(R.drawable.shape_corners_cccccc_dp12_5)
        when (i) {
            0 -> {
                tv_todo.setBackgroundResource(R.drawable.shape_corners_3477fc_dp_12_5)
            }

            1 -> {
                tv_do.setBackgroundResource(R.drawable.shape_corners_3477fc_dp_12_5)
            }
        }
    }
}