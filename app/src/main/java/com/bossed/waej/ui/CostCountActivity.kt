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
import com.bossed.waej.adapter.CostCountAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.CateTypeCountListUrl
import com.bossed.waej.javebean.CostCountResponse
import com.bossed.waej.javebean.CostCountRow
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_cost_count.*
import java.math.BigDecimal

class CostCountActivity : BaseActivity(), View.OnClickListener, OnRefreshLoadMoreListener {
    private lateinit var costCountAdapter: CostCountAdapter
    private val list = ArrayList<CostCountRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_cost_count
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_cost_count)
        rv_cost_count.layoutManager = LinearLayoutManager(this)
        costCountAdapter = CostCountAdapter(list)
        costCountAdapter.bindToRecyclerView(rv_cost_count)
        tv_end_date.text = DateFormatUtils.get().formatDate(TimeUtils.getNowDate())
        tv_start_date.text =
            DateFormatUtils.get().formatDate(CalendarUtils.get().nowDateAddDays(-30))
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
        costCountAdapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(this, CostCountInfoActivity::class.java)
            intent.putExtra("id", list[position].id)
            intent.putExtra("name", list[position].name)
            intent.putExtra("money", list[position].money)
            startActivity(intent)
        }
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
        onRefresh(srl_cost_count)
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["st"] = tv_start_date.text.toString()
        params["et"] = tv_end_date.text.toString()
        params["pageSize"] = "20"
        params["pageNum"] = pageNum.toString()
        RetrofitUtils.get()
            .getJson(CateTypeCountListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, CostCountResponse::class.java)
                    list.addAll(t.rows)
                    costCountAdapter.setNewData(list)
                    var total = BigDecimal(0.0)
                    for (row: CostCountRow in list) {
                        total += BigDecimal(row.money)
                    }
                    tv_total.text = "￥${total.setScale(2, BigDecimal.ROUND_HALF_DOWN)}"
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}