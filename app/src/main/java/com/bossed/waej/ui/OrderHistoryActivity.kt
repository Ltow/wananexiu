package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.OrderListAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.OrderListBean
import com.bossed.waej.javebean.OrderRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activty_order_history.et_search
import kotlinx.android.synthetic.main.activty_order_history.rv_service_order_history
import kotlinx.android.synthetic.main.activty_order_history.srl_order_history
import kotlinx.android.synthetic.main.activty_order_history.tb_history

class OrderHistoryActivity : BaseActivity(),View.OnClickListener, OnRefreshLoadMoreListener {
    private lateinit var orderHistoryAdapter: OrderListAdapter
    private val orderBean = ArrayList<OrderRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activty_order_history
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_history)
        rv_service_order_history.layoutManager = LinearLayoutManager(this)
        orderHistoryAdapter = OrderListAdapter(1, orderBean)
        orderHistoryAdapter.bindToRecyclerView(rv_service_order_history)
        orderHistoryAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        tb_history.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        orderHistoryAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.tv_edit) {
                val intent = Intent(this, OrderHistoryMsgActivity::class.java)
                intent.putExtra("type", "1")
                intent.putExtra("id", orderBean[position].id)
                startActivity(intent)
            }
        }
        srl_order_history.setOnRefreshLoadMoreListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onRefresh(srl_order_history)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        onRefresh(srl_order_history)
    }

    private fun getOrderHistoryList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["searchValue"] = et_search.text.toString()
        params["pageNum"] = pageNum.toString()
        params["pageSize"] = "10"
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

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageNum = 1
        orderBean.clear()
        getOrderHistoryList()
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getOrderHistoryList()
        refreshLayout.finishLoadMore()
    }
}