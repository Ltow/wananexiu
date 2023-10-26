package com.bossed.waej.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.PaymentSearchAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.PaymentSearchUrl
import com.bossed.waej.javebean.PaymentSearchResponse
import com.bossed.waej.javebean.PaymentSearchRow
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_collection_search.*

class PaymentSearchActivity : BaseActivity(), View.OnClickListener, OnRefreshLoadMoreListener {
    private lateinit var adapter: PaymentSearchAdapter
    private val list = ArrayList<PaymentSearchRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_collection_search
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_collection_search)
        tb_collection_search.title = "付款单查询"
        et_search.hint = "输入单号、供应商名称、电话查询"
        rv_collection_search.layoutManager = LinearLayoutManager(this)
        adapter = PaymentSearchAdapter(list)
        adapter.bindToRecyclerView(rv_collection_search)
        tv_end_date.text = DateFormatUtils.get().formatDate(TimeUtils.getNowDate())
        tv_start_date.text =
            DateFormatUtils.get().formatDate(CalendarUtils.get().nowDateAddDays(-30))
    }

    override fun initListener() {
        srl_collection.setOnRefreshLoadMoreListener(this)
        tb_collection_search.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        adapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(this, PaymentInfoActivity::class.java)
            intent.putExtra("id", list[position].id)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onRefresh(srl_collection)
            }

            R.id.tv_start_date -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_start_date.text = DateFormatUtils.get().formatDate(it)
                    tv_start_date.setTextColor(Color.parseColor("#FF3A3A"))
                    tv_start_date.setBackgroundResource(R.drawable.shape_stroke_ffa8a8_corners_fff6f6_dp13)
                    onRefresh(srl_collection)
                }
            }

            R.id.tv_end_date -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_end_date.text = DateFormatUtils.get().formatDate(it)
                    tv_end_date.setTextColor(Color.parseColor("#FF3A3A"))
                    tv_end_date.setBackgroundResource(R.drawable.shape_stroke_ffa8a8_corners_fff6f6_dp13)
                    onRefresh(srl_collection)
                }
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
        params["searchKeywords"] = et_search.text.toString()
        params["st"] = tv_start_date.text.toString()
        params["et"] = tv_end_date.text.toString()
        params["pageSize"] = "20"
        params["pageNum"] = pageNum.toString()
        params["model"] = "500"
        RetrofitUtils.get()
            .getJson(PaymentSearchUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, PaymentSearchResponse::class.java)
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
        onRefresh(srl_collection)
    }
}