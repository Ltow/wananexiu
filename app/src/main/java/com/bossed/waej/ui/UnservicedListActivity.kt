package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.UnservicedAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.OnlineOrderListUrl
import com.bossed.waej.javebean.OnlineOrderListResponse
import com.bossed.waej.javebean.OnlineOrderListRow
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_service_order.*

class UnservicedListActivity : BaseActivity(), OnRefreshLoadMoreListener, OnClickListener {
    private lateinit var adapter: UnservicedAdapter
    private val list = ArrayList<OnlineOrderListRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_service_order
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_onder_draft)
        tb_onder_draft.title = "服务中订单"
        rv_service_order.layoutManager = LinearLayoutManager(this)
        adapter = UnservicedAdapter(list)
        adapter.bindToRecyclerView(rv_service_order)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        srl_order_service.setOnRefreshLoadMoreListener(this)
        tb_onder_draft.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        adapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(this, ScanDetailActivity::class.java)
            intent.putExtra("type", 0)//0从列表跳转  1扫码跳转
            intent.putExtra("id", list[position].id)
            startActivity(intent)
        }
        adapter.setOnItemClick {
            val intent = Intent(this, ScanDetailActivity::class.java)
            intent.putExtra("type", 0)//0从列表跳转  1扫码跳转
            intent.putExtra("id", list[it].id)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search -> {
                onRefresh(srl_order_service)
            }
        }
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
        params["searchValue"] = et_search.text.toString()
        params["shippingStatus"] = "1"//1-待服务 2-待完成 3-已完成
        params["pageSize"] = "10"
        params["pageNum"] = pageNum.toString()
        params["orderByColumn"] = "addTime"
        params["isAsc"] = "desc"
        RetrofitUtils.get()
            .getJson(OnlineOrderListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, OnlineOrderListResponse::class.java)
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
        onRefresh(srl_order_service)
    }
}