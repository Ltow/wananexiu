package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.PurchaseHistoryAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.PurchaseListUrl
import com.bossed.waej.javebean.PurchaseListResponse
import com.bossed.waej.javebean.PurchaseListRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_purchase_list.*

class PurchaseBackHistoryActivity : BaseActivity(), OnRefreshLoadMoreListener,
    View.OnClickListener {
    private lateinit var adapter: PurchaseHistoryAdapter
    private val list = ArrayList<PurchaseListRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_purchase_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_purchase_list)
        tb_purchase_list.rightTitle = ""
        tb_purchase_list.title = "进货退回查询"
        rv_purchase_list.layoutManager = LinearLayoutManager(this)
        adapter = PurchaseHistoryAdapter(list, 2)
        adapter.bindToRecyclerView(rv_purchase_list)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        srl_list.setOnRefreshLoadMoreListener(this)
        tb_purchase_list.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        adapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(this, PurchaseBackHistoryInfoActivity::class.java)
            intent.putExtra("id", list[position].id)
            intent.putExtra("type", "id")
            startActivity(intent)
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

    override fun onResume() {
        super.onResume()
        onRefresh(srl_list)
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["searchKeywords"] = et_search.text.toString()
        params["pageNum"] = pageNum.toString()
        params["pageSize"] = "20"
        params["type"] = "2"//1-进货 2-退回
        params["status"] = "1"// 0-草稿 1-结算 2-作废
        params["isAsc"] = "desc"
        params["orderByColumn"] = "updateTime"
        RetrofitUtils.get()
            .getJson(PurchaseListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, PurchaseListResponse::class.java)
                    list.addAll(t.rows)
                    adapter.setNewData(list)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onRefresh(srl_list)
            }
        }
    }
}