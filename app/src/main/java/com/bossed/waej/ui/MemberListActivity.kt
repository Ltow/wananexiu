package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.MemberListAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.javebean.MemberListBean
import com.bossed.waej.util.OnNoRepeatClickListener
import com.bossed.waej.util.DoubleClicksUtils
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_member_list.*
import kotlinx.android.synthetic.main.layout_special_title.*

class MemberListActivity : BaseActivity(), OnRefreshLoadMoreListener, OnNoRepeatClickListener {
    private lateinit var memberListAdapter: MemberListAdapter
    private val beans = ArrayList<MemberListBean>()

    override fun getLayoutId(): Int {
        return R.layout.activity_member_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(rl_special_title)
        rv_member_list.layoutManager = LinearLayoutManager(this)
        beans.add(MemberListBean("张佳丽"))
        beans.add(MemberListBean("张佳丽"))
        beans.add(MemberListBean("张佳丽"))
        beans.add(MemberListBean("张佳丽"))
        memberListAdapter = MemberListAdapter(beans)
        memberListAdapter.bindToRecyclerView(rv_member_list)
    }

    override fun initListener() {
        srl_member_list.setOnRefreshLoadMoreListener(this)
        memberListAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id==R.id.tv_see){
                val intent= Intent(this,MemberDetailActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        refreshLayout.finishLoadMore()
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }
        }
    }
}