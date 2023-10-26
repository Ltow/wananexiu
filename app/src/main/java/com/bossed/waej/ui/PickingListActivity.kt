package com.bossed.waej.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.PickingByCustomerAdapter
import com.bossed.waej.adapter.PickingBySupplierAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.PickingListByCustomerUrl
import com.bossed.waej.http.UrlConstants.PickingListBySupplierUrl
import com.bossed.waej.javebean.PickByCustomerResponse
import com.bossed.waej.javebean.PickByCustomerRow
import com.bossed.waej.javebean.PickBySupplierResponse
import com.bossed.waej.javebean.PickBySupplierRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_picking_list.*

/**
 * 领料查询
 */
class PickingListActivity : BaseActivity(), View.OnClickListener, OnRefreshLoadMoreListener {
    private lateinit var byCustomerAdapter: PickingByCustomerAdapter
    private val byCustomerList = ArrayList<PickByCustomerRow>()
    private lateinit var bySupplierAdapter: PickingBySupplierAdapter
    private val bySupplierList = ArrayList<PickBySupplierRow>()
    private var pageNum = 1
    private var selIndex = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_picking_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_picking_list)
        rv_picking_list.layoutManager = LinearLayoutManager(this)
    }

    override fun initListener() {
        srl_picking.setOnRefreshLoadMoreListener(this)
        tb_picking_list.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch()
            }
            R.id.tv_start_date -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                selIndex = 1
                switch()
            }
            R.id.tv_end_date -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                selIndex = 2
                switch()
            }
        }
    }

    private fun switch() {
        tv_start_date.setBackgroundResource(R.drawable.shape_corners_cccccc_dp12_5)
        tv_end_date.setBackgroundResource(R.drawable.shape_corners_cccccc_dp12_5)
        when (selIndex) {
            1 -> {
                srl_picking.setBackgroundColor(Color.parseColor("#f6f6f6"))
                tv_start_date.setBackgroundResource(R.drawable.shape_corners_3477fc_dp_12_5)
                byCustomerAdapter = PickingByCustomerAdapter(byCustomerList)
                byCustomerAdapter.bindToRecyclerView(rv_picking_list)
                byCustomerAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
                byCustomerAdapter.setOnItemChildClickListener { adapter, view, position ->
                    when (view.id) {
                        R.id.iv_info -> {
                            val intent = Intent(this, PickByCustomerInfoActivity::class.java)
                            intent.putExtra("id", byCustomerList[position].id)
                            startActivity(intent)
                        }
                    }
                }
                onRefresh(srl_picking)
            }
            2 -> {
                srl_picking.setBackgroundColor(Color.parseColor("#ffffff"))
                tv_end_date.setBackgroundResource(R.drawable.shape_corners_3477fc_dp_12_5)
                bySupplierAdapter = PickingBySupplierAdapter(bySupplierList)
                bySupplierAdapter.bindToRecyclerView(rv_picking_list)
                bySupplierAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
                bySupplierAdapter.setOnItemChildClickListener { adapter, view, position ->
                    when (view.id) {
                        R.id.iv_info -> {
                            val intent = Intent(this, PickBySupplierInfoActivity::class.java)
                            intent.putExtra("type", "1")
                            intent.putExtra("id", bySupplierList[position].id)
                            startActivity(intent)
                        }
                    }
                }
                onRefresh(srl_picking)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        switch()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageNum = 1
        when (selIndex) {
            1 -> {
                byCustomerList.clear()
                getByCustomerList()
            }
            2 -> {
                bySupplierList.clear()
                getBySupplierList()
            }
        }
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        when (selIndex) {
            1 -> getByCustomerList()
            2 -> getBySupplierList()
        }
        refreshLayout.finishLoadMore()
    }

    private fun getByCustomerList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["pageSize"] = "20"
        params["pageNum"] = pageNum.toString()
        params["searchKeywords"] = et_search.text.toString()
        params["isAsc"] = "desc"
        params["orderByColumn"] = "updateTime"
        RetrofitUtils.get().getJson(
            PickingListByCustomerUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("Tag", s)
                    val t = GsonUtils.fromJson(s, PickByCustomerResponse::class.java)
                    byCustomerList.addAll(t.rows)
                    byCustomerAdapter.setNewData(byCustomerList)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getBySupplierList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["pageSize"] = "20"
        params["pageNum"] = pageNum.toString()
        params["searchKeywords"] = et_search.text.toString()
        params["isAsc"] = "desc"
        params["orderByColumn"] = "updateTime"
        RetrofitUtils.get().getJson(
            PickingListBySupplierUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("Tag", s)
                    val t = GsonUtils.fromJson(s, PickBySupplierResponse::class.java)
                    bySupplierList.addAll(t.rows)
                    bySupplierAdapter.setNewData(bySupplierList)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}