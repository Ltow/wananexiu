package com.bossed.waej.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.FeedHistoryAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.FeedHistoryBean
import com.bossed.waej.javebean.FeedHistoryRow
import com.bossed.waej.util.OnNoRepeatClickListener
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_feed_history.*

class FeedHistoryActivity : BaseActivity(), OnRefreshLoadMoreListener, OnNoRepeatClickListener {
    private lateinit var feedHistoryAdapter: FeedHistoryAdapter
    private val beans = ArrayList<FeedHistoryRow>()
    private var pageNum = 1
    private var status = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_feed_history
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_feed_history)
        rv_feed_history.layoutManager = LinearLayoutManager(this)
        feedHistoryAdapter = FeedHistoryAdapter(beans)
        feedHistoryAdapter.bindToRecyclerView(rv_feed_history)
        feedHistoryAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        getFeedBackList()
    }

    override fun initListener() {
        srl_feed_history.setOnRefreshLoadMoreListener(this)
        tb_feed_history.setOnTitleBarListener(object : OnTitleBarListener {
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
        beans.clear()
        pageNum = 1
        getFeedBackList()
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getFeedBackList()
        refreshLayout.finishLoadMore()
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_finished -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(0)
            }
            R.id.tv_processing -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(1)
            }
        }
    }

    private fun switch(i: Int) {
        clearStatus()
        when (i) {
            0 -> {
                tv_finished.setTextColor(Color.parseColor("#333333"))
                iv_finished.visibility = View.VISIBLE
                status = 1
                onRefresh(srl_feed_history)
            }
            1 -> {
                tv_processing.setTextColor(Color.parseColor("#333333"))
                iv_processing.visibility = View.VISIBLE
                status = 0
                onRefresh(srl_feed_history)
            }
        }
    }

    private fun clearStatus() {
        tv_finished.setTextColor(Color.parseColor("#999999"))
        tv_processing.setTextColor(Color.parseColor("#999999"))
        iv_finished.visibility = View.GONE
        iv_processing.visibility = View.GONE
    }

    private fun getFeedBackList() {
        val params = HashMap<String, String>()
        params["status"] = status.toString()
        params["pageNum"] = pageNum.toString()
        params["userId"] = SPUtils.getInstance().getInt("userId").toString()
        params["pageSize"] = "10"
        RetrofitUtils.get().getJson(
            UrlConstants.FeedBackList,
            params,
            this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, FeedHistoryBean::class.java)
                    beans.addAll(t.rows)
                    feedHistoryAdapter.setNewData(beans)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}