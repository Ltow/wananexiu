package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.CheckHistoryAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.CheckListUrl
import com.bossed.waej.javebean.CheckListResponse
import com.bossed.waej.javebean.CheckListRow
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_purchase_list.*

class CheckHistoryActivity : BaseActivity(), OnRefreshLoadMoreListener, View.OnClickListener {
    private lateinit var adapter: CheckHistoryAdapter
    private val list = ArrayList<CheckListRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_purchase_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_purchase_list)
        tb_purchase_list.title = "盘点单查询"
        tb_purchase_list.rightTitle = ""
        et_search.hint = "输入盘点单号、操作员、配件名称查询"
        ll_time.visibility = View.VISIBLE
        tv_start_date.text =
            DateFormatUtils.get().formatDate(CalendarUtils.get().nowDateAddDays(-30))
        tv_end_date.text = DateFormatUtils.get().formatDate(TimeUtils.getNowString())
        rv_purchase_list.layoutManager = LinearLayoutManager(this)
        adapter = CheckHistoryAdapter(list)
        adapter.bindToRecyclerView(rv_purchase_list)
    }

    override fun initListener() {
        srl_list.setOnRefreshLoadMoreListener(this)
        tb_purchase_list.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        adapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(this, CheckInfoActivity::class.java)
            intent.putExtra("id", list[position].id)
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
        val params = HashMap<String, String>()
        params["pageSize"] = "20"
        params["pageNum"] = pageNum.toString()
        params["searchKeywords"] = et_search.text.toString()
        params["status"] = "1"//0-草稿 1-结算 2-作废
        params["isAsc"] = "desc"
        params["orderByColumn"] = "updateTime"
        RetrofitUtils.get()
            .getJson(CheckListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, CheckListResponse::class.java)
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

            R.id.tv_start_date -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_start_date.text = DateFormatUtils.get().formatDate(it)
                    onRefresh(srl_list)
                }
            }

            R.id.tv_end_date -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_start_date.text = DateFormatUtils.get().formatDate(it)
                    onRefresh(srl_list)
                }
            }
        }
    }
}