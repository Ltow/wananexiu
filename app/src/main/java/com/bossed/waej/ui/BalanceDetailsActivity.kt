package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.BalanceDetailsAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.WalletBalanceListUrl
import com.bossed.waej.javebean.BalanceDetailResponse
import com.bossed.waej.javebean.BalanceDetailRow
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_balance_details.*

class BalanceDetailsActivity : BaseActivity(), OnRefreshLoadMoreListener {
    private lateinit var adapter: BalanceDetailsAdapter
    private val list = ArrayList<BalanceDetailRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_balance_details
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_balance_details)
        rv_balance.layoutManager = LinearLayoutManager(this)
        adapter = BalanceDetailsAdapter(list)
        adapter.bindToRecyclerView(rv_balance)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        tb_balance_details.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        srl_balance.setOnRefreshLoadMoreListener(this)
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["orderByColumn"] = "updateTime"
        params["isAsc"] = "desc"
        params["pageSize"] = "10"
        params["pageNum"] = pageNum.toString()
        RetrofitUtils.get()
            .getJson(WalletBalanceListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BalanceDetailResponse::class.java)
                    list.addAll(t.rows)
                    adapter.setNewData(list)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        onRefresh(srl_balance)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        list.clear()
        pageNum = 1
        getList()
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getList()
        refreshLayout.finishLoadMore()
    }
}