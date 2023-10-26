package com.bossed.waej.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.CostCountInfoAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.CateTypeInfoUrl
import com.bossed.waej.javebean.CostCountInfoResponse
import com.bossed.waej.javebean.JournalSearchDataRow
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_cost_count.*

class CostCountInfoActivity : BaseActivity(), View.OnClickListener, OnRefreshLoadMoreListener {
    private lateinit var adapter: CostCountInfoAdapter
    private val list = ArrayList<JournalSearchDataRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_cost_count
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_cost_count)
        tb_cost_count.title = "费用详情"
        tv_cost_type.visibility = View.VISIBLE
        rv_cost_count.layoutManager = LinearLayoutManager(this)
        adapter = CostCountInfoAdapter(list)
        adapter.bindToRecyclerView(rv_cost_count)
        adapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        tv_end_date.text = DateFormatUtils.get().formatDate(TimeUtils.getNowDate())
        tv_start_date.text =
            DateFormatUtils.get().formatDate(CalendarUtils.get().nowDateAddDays(-30))
        tv_cost_type.text = intent.getStringExtra("name")
        tv_total.text = intent.getStringExtra("money")
    }

    override fun initListener() {
        srl_cost_count.setOnRefreshLoadMoreListener(this)
        tb_cost_count.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.tv_start_date -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_start_date.text = DateFormatUtils.get().formatDate(it)
                    tv_start_date.setTextColor(Color.parseColor("#FF3A3A"))
                    tv_start_date.setBackgroundResource(R.drawable.shape_stroke_ffa8a8_corners_fff6f6_dp13)
                    onRefresh(srl_cost_count)
                }
            }
            R.id.tv_end_date -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_end_date.text = DateFormatUtils.get().formatDate(it)
                    tv_end_date.setTextColor(Color.parseColor("#FF3A3A"))
                    tv_end_date.setBackgroundResource(R.drawable.shape_stroke_ffa8a8_corners_fff6f6_dp13)
                    onRefresh(srl_cost_count)
                }
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageNum = 1
        list.clear()
        getInfo()
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getInfo()
        refreshLayout.finishLoadMore()
    }

    override fun onResume() {
        super.onResume()
        onRefresh(srl_cost_count)
    }

    private fun getInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["id"] = intent.getStringExtra("id")
        params["st"] = tv_start_date.text.toString()
        params["et"] = tv_end_date.text.toString()
        params["pageSize"] = "20"
        params["pageNum"] = pageNum.toString()
        RetrofitUtils.get()
            .getJson(CateTypeInfoUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, CostCountInfoResponse::class.java)
                    list.addAll(t.data.billJournalList.rows)
                    adapter.setNewData(list)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}
