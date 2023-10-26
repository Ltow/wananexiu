package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.MemberConsumeAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.javebean.MemberConsumeBean
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_card_history.*

class CardHistoryActivity : BaseActivity(), OnRefreshLoadMoreListener {
    private lateinit var memberConsumeAdapter: MemberConsumeAdapter
    private val beans = ArrayList<MemberConsumeBean>()

    override fun getLayoutId(): Int {
        return R.layout.activity_card_history
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_card_history)
        rv_card_history.layoutManager = LinearLayoutManager(this)
        beans.add(MemberConsumeBean("冀AL33G9"))
        beans.add(MemberConsumeBean("冀AL33G9"))
        beans.add(MemberConsumeBean("冀AL33G9"))
        memberConsumeAdapter = MemberConsumeAdapter(beans)
        memberConsumeAdapter.bindToRecyclerView(rv_card_history)
        memberConsumeAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        srl_card_history.setOnRefreshLoadMoreListener(this)
        tb_card_history.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        refreshLayout.finishLoadMore()
    }
}