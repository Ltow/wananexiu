package com.bossed.waej.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.ItemAdapter
import com.bossed.waej.adapter.OrderListAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.HistoryItemResponse
import com.bossed.waej.javebean.ItemBean
import com.bossed.waej.javebean.OrderListBean
import com.bossed.waej.javebean.OrderRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.OnNoRepeatClickListener
import com.bossed.waej.util.RetrofitUtils
import com.bossed.waej.util.States
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_search_history.rv_history
import kotlinx.android.synthetic.main.activity_search_history.srl_search_history
import kotlinx.android.synthetic.main.activity_search_history.tb_search_history
import kotlinx.android.synthetic.main.activity_search_history.tv_document
import kotlinx.android.synthetic.main.activity_search_history.tv_item

class SearchHistoryActivity : BaseActivity(), OnNoRepeatClickListener, OnRefreshLoadMoreListener {
    private var pageNum = 1
    private lateinit var orderHistoryAdapter: OrderListAdapter
    private val orderBean = ArrayList<OrderRow>()
    private lateinit var itemAdapter: ItemAdapter
    private val itemBean = ArrayList<ItemBean>()

    override fun getLayoutId(): Int {
        return R.layout.activity_search_history
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_search_history)
        rv_history.layoutManager = LinearLayoutManager(this)
        switch()
    }

    override fun initListener() {
        tb_search_history.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        srl_search_history.setOnRefreshLoadMoreListener(this)
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_document -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(0)
            }
            R.id.tv_item -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(1)
            }
        }
    }

    private fun switch(i: Int = 0) {
        tv_document.setTextColor(Color.parseColor("#cccccc"))
        tv_item.setTextColor(Color.parseColor("#cccccc"))
        when (i) {
            0 -> {
                tv_document.setTextColor(Color.parseColor("#224bbe"))
                orderHistoryAdapter = OrderListAdapter(1, orderBean)
                orderHistoryAdapter.bindToRecyclerView(rv_history)
                orderHistoryAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
                orderHistoryAdapter.setOnItemChildClickListener { adapter, view, position ->
                    if (view.id == R.id.tv_edit) {
                        val intent = Intent(this, OrderHistoryMsgActivity::class.java)
                        intent.putExtra("type", "1")
                        intent.putExtra("id", orderBean[position].id)
                        startActivity(intent)
                    }
                }
                srl_search_history.setEnableLoadMore(true)
                srl_search_history.setEnableRefresh(true)
                onRefresh(srl_search_history)
            }
            1 -> {
                tv_item.setTextColor(Color.parseColor("#224bbe"))
                itemAdapter = ItemAdapter(States.HISTORY, itemBean)
                itemAdapter.bindToRecyclerView(rv_history)
                itemAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
                srl_search_history.setEnableLoadMore(false)
                srl_search_history.setEnableRefresh(false)
                itemBean.clear()
                getItem()
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        orderBean.clear()
        pageNum = 1
        getDocument()
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getDocument()
        refreshLayout.finishLoadMore()
    }

    private fun getItem() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["cardNo"] = intent.getStringExtra("carNo")!!
        RetrofitUtils.get().getJson(
            UrlConstants.HistoryItemUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, HistoryItemResponse::class.java)
                    itemBean.addAll(t.data)
                    itemAdapter.setNewData(itemBean)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getDocument() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["cardNo"] = intent.getStringExtra("carNo")!!
        params["pageNum"] = pageNum.toString()
//        params["pageSize"] = "10"
        params["orderStatus"] = intent.getIntExtra("orderStatus", 1).toString()
        params["orderType"] = intent.getIntExtra("orderType", 2).toString()
        RetrofitUtils.get().getJson(
            UrlConstants.OrderListUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, OrderListBean::class.java)
                    if (t.rows.isNotEmpty()) {
                        orderBean.addAll(t.rows)
                    }
                    orderHistoryAdapter.setNewData(orderBean)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}