package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.PricingListAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.PriceListUrl
import com.bossed.waej.javebean.PricingListResponse
import com.bossed.waej.javebean.PricingListRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_pricing_list.*

class PricingListActivity : BaseActivity(), OnRefreshLoadMoreListener, View.OnClickListener {
    private lateinit var adapter: PricingListAdapter
    private val list = ArrayList<PricingListRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_pricing_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_pricing_list)
        rv_pricing_list.layoutManager = LinearLayoutManager(this)
        adapter = PricingListAdapter(list)
        adapter.bindToRecyclerView(rv_pricing_list)
    }

    override fun initListener() {
        srl_pricing.setOnRefreshLoadMoreListener(this)
        tb_pricing_list.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_info -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    val intent = Intent(this, PricingActivity::class.java)
                    intent.putExtra("id", list[position].id)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onRefresh(srl_pricing)
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

    override fun onResume() {
        super.onResume()
        onRefresh(srl_pricing)
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["searchKeywords"] = et_search.text.toString()
        params["pageSize"] = "20"
        params["pageNum"] = pageNum.toString()
        RetrofitUtils.get()
            .getJson(PriceListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, PricingListResponse::class.java)
                    list.addAll(t.rows)
                    adapter.setNewData(list)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}