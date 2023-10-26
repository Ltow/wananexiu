package com.bossed.waej.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.MyOrderAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.OnlineOrderListResponse
import com.bossed.waej.javebean.OnlineOrderListRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_my_order.*
import kotlinx.android.synthetic.main.activity_service_order.et_search

class MyOrderActivity : BaseActivity(), View.OnClickListener, OnRefreshLoadMoreListener {
    private lateinit var adapter: MyOrderAdapter
    private val list = ArrayList<OnlineOrderListRow>()
    private var pageNum = 1
    private var shippingStatus = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_my_order
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_my_order)
        rv_my_order.layoutManager = LinearLayoutManager(this)
        adapter = MyOrderAdapter(list)
        adapter.bindToRecyclerView(rv_my_order)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        switch(0)
    }

    override fun initListener() {
        tb_my_order.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        adapter.setOnItemClickListener { adapter, view, position ->
            val intent1 = Intent(this, ScanDetailActivity::class.java)
            val intent2 = Intent(this, OrderDetailActivity::class.java)
            intent1.putExtra("type", 0) //0从列表跳转  1扫码跳转
            intent1.putExtra("id", list[position].id)
            intent2.putExtra("id", list[position].id)
            when (list[position].shippingStatus) {
                "1" -> {
                    startActivity(intent1)
                }

                "2" -> {
                    startActivity(intent2)
                }

                "3" -> {
                    startActivity(intent2)
                }
            }
        }
        adapter.setOnItemClick {
            val intent1 = Intent(this, ScanDetailActivity::class.java)
            val intent2 = Intent(this, OrderDetailActivity::class.java)
            intent1.putExtra("type", 0) //0从列表跳转  1扫码跳转
            intent1.putExtra("id", list[it].id)
            intent2.putExtra("id", list[it].id)
            when (list[it].shippingStatus) {
                "1" -> {
                    startActivity(intent1)
                }

                "2" -> {
                    startActivity(intent2)
                }

                "3" -> {
                    startActivity(intent2)
                }
            }
        }
        srl_order.setOnRefreshLoadMoreListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onRefresh(srl_order)
            }

            R.id.tv_all -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(0)
            }

            R.id.tv_dfw -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(1)
            }

            R.id.tv_dwg -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(2)
            }

            R.id.tv_ywg -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(3)
            }

            R.id.tv_yqr -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(4)
            }
        }
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["searchValue"] = et_search.text.toString()
        params["shippingStatus"] = shippingStatus//1-待服务 2-待完成 3-已完成
        params["pageSize"] = "10"
        params["pageNum"] = pageNum.toString()
        params["orderByColumn"] = "addTime"
        params["isAsc"] = "desc"
        RetrofitUtils.get()
            .getJson(
                UrlConstants.OnlineOrderListUrl, params, this,
                object : RetrofitUtils.OnCallBackListener {
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

    private fun switch(i: Int) {
        tv_all.setBackgroundResource(R.drawable.shape_corners_ffffff_dp12)
        tv_all.setTextColor(Color.parseColor("#333333"))
        tv_all.typeface = Typeface.DEFAULT
        tv_dfw.setBackgroundResource(R.drawable.shape_corners_ffffff_dp12)
        tv_dfw.setTextColor(Color.parseColor("#333333"))
        tv_dfw.typeface = Typeface.DEFAULT
        tv_dwg.setBackgroundResource(R.drawable.shape_corners_ffffff_dp12)
        tv_dwg.setTextColor(Color.parseColor("#333333"))
        tv_dwg.typeface = Typeface.DEFAULT
        tv_ywg.setBackgroundResource(R.drawable.shape_corners_ffffff_dp12)
        tv_ywg.setTextColor(Color.parseColor("#333333"))
        tv_ywg.typeface = Typeface.DEFAULT
        tv_yqr.setBackgroundResource(R.drawable.shape_corners_ffffff_dp12)
        tv_yqr.setTextColor(Color.parseColor("#333333"))
        tv_yqr.typeface = Typeface.DEFAULT
        when (i) {
            0 -> {
                tv_all.setBackgroundResource(R.drawable.shape_corners_ffffff_12_12_0_0)
                tv_all.setTextColor(Color.parseColor("#3477FC"))
                tv_all.typeface = Typeface.DEFAULT_BOLD
                shippingStatus = ""
                onRefresh(srl_order)
            }

            1 -> {
                tv_dfw.setBackgroundResource(R.drawable.shape_corners_ffffff_12_12_0_0)
                tv_dfw.setTextColor(Color.parseColor("#3477FC"))
                tv_dfw.typeface = Typeface.DEFAULT_BOLD
                shippingStatus = "1"
                onRefresh(srl_order)
            }

            2 -> {
                tv_dwg.setBackgroundResource(R.drawable.shape_corners_ffffff_12_12_0_0)
                tv_dwg.setTextColor(Color.parseColor("#3477FC"))
                tv_dwg.typeface = Typeface.DEFAULT_BOLD
            }

            3 -> {
                tv_ywg.setBackgroundResource(R.drawable.shape_corners_ffffff_12_12_0_0)
                tv_ywg.setTextColor(Color.parseColor("#3477FC"))
                tv_ywg.typeface = Typeface.DEFAULT_BOLD
                shippingStatus = "2"
                onRefresh(srl_order)
            }

            4 -> {
                tv_yqr.setBackgroundResource(R.drawable.shape_corners_ffffff_12_12_0_0)
                tv_yqr.setTextColor(Color.parseColor("#3477FC"))
                tv_yqr.typeface = Typeface.DEFAULT_BOLD
                shippingStatus = "3"
                onRefresh(srl_order)
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
}