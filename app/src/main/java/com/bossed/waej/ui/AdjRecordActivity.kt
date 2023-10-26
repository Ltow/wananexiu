package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.AdjRecordAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.AdjustListUrl
import com.bossed.waej.javebean.AdjRecordResponse
import com.bossed.waej.javebean.AdjRecordRow
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_adj_record.*

class AdjRecordActivity : BaseActivity(), OnRefreshLoadMoreListener {
    private lateinit var adapter: AdjRecordAdapter
    private val list = ArrayList<AdjRecordRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_adj_record
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_adj_record)
        rv_adj_record.layoutManager = LinearLayoutManager(this)
        adapter = AdjRecordAdapter(list)
        adapter.bindToRecyclerView(rv_adj_record)
        onRefresh(srl_adj)
    }

    override fun initListener() {
        srl_adj.setOnRefreshLoadMoreListener(this)
        tb_adj_record.setOnTitleBarListener(object : OnTitleBarListener {
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
        pageNum = 1
        list.clear()
        getList()
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getList()
        refreshLayout.finishLoadMore()
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["type"] = "1"
        params["pageSize"] = "20"
        params["pageNum"] = pageNum.toString()
        RetrofitUtils.get()
            .getJson(AdjustListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, AdjRecordResponse::class.java)
                    list.addAll(t.rows)
                    adapter.setNewData(list)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}