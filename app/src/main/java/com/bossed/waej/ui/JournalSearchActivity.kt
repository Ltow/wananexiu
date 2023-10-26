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
import com.bossed.waej.adapter.JournalSearchAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.customview.BottomDialog
import com.bossed.waej.http.UrlConstants.JournalListUrl
import com.bossed.waej.javebean.JournalSearchDataRow
import com.bossed.waej.javebean.JournalSearchResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_journal_search.*

class JournalSearchActivity : BaseActivity(), View.OnClickListener, OnRefreshLoadMoreListener {
    private lateinit var adapter: JournalSearchAdapter
    private val list = ArrayList<JournalSearchDataRow>()
    private var accountId = ""
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_journal_search
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_journal_search)
        rv_journal_search.layoutManager = LinearLayoutManager(this)
        adapter = JournalSearchAdapter(list)
        adapter.bindToRecyclerView(rv_journal_search)
        adapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        tv_end_date.text = DateFormatUtils.get().formatDate(TimeUtils.getNowDate())
        tv_start_date.text =
            DateFormatUtils.get().formatDate(CalendarUtils.get().nowDateAddDays(-30))
        onRefresh(srl_journal)
    }

    override fun initListener() {
        srl_journal.setOnRefreshLoadMoreListener(this)
        tb_journal_search.setOnTitleBarListener(object : OnTitleBarListener {
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
                    onRefresh(srl_journal)
                }
            }
            R.id.tv_end_date -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_end_date.text = DateFormatUtils.get().formatDate(it)
                    tv_end_date.setTextColor(Color.parseColor("#FF3A3A"))
                    tv_end_date.setBackgroundResource(R.drawable.shape_stroke_ffa8a8_corners_fff6f6_dp13)
                    onRefresh(srl_journal)
                }
            }
            R.id.tv_account -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelAccountPop(this) {
                    tv_account.text = it.name
                    accountId = it.id.toString()
                    onRefresh(srl_journal)
                }
            }
            R.id.tv_type -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                BottomDialog(this).create(R.layout.layout_pop_sel_type)
                    .setCanceledOnTouchOutside(true)
                    .setViewInterface { view, dialog ->
                        view.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                            dialog.dismiss()
                        }
                        view.findViewById<View>(R.id.tv_sr).setOnClickListener {
                            tv_type.text = "收入"
                            dialog.dismiss()
                            onRefresh(srl_journal)
                        }
                        view.findViewById<View>(R.id.tv_zc).setOnClickListener {
                            tv_type.text = "支出"
                            dialog.dismiss()
                            onRefresh(srl_journal)
                        }
                        view.findViewById<View>(R.id.tv_all).setOnClickListener {
                            tv_type.text = "全部"
                            dialog.dismiss()
                            onRefresh(srl_journal)
                        }
                    }.show()
            }
        }
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["billType"] = when (tv_type.text.toString()) {
            "收入" -> "1"
            "支出" -> "2"
            else -> ""
        }
        params["st"] = tv_start_date.text.toString()
        params["et"] = tv_end_date.text.toString()
        params["accountId"] = accountId
        params["pageSize"] = "20"
        params["pageNum"] = pageNum.toString()
        RetrofitUtils.get()
            .getJson(JournalListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, JournalSearchResponse::class.java)
                    tv_numReceivables.text = t.data.numReceivables.toString()
                    tv_numPayment.text = t.data.numPayment.toString()
                    tv_monenReceivables.text = t.data.monenReceivables
                    tv_monenPayment.text = t.data.monenPayment
                    tv_money.text = t.data.money
                    list.addAll(t.data.billJournalList.rows)
                    adapter.setNewData(list)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
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