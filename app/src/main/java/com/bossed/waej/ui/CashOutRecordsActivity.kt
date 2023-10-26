package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.CashOutRecordsAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.WithdrawalListUrl
import com.bossed.waej.javebean.CashOutRecordsResponse
import com.bossed.waej.javebean.CashOutRecordsRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_cash_out_records.*

class CashOutRecordsActivity : BaseActivity(), OnRefreshLoadMoreListener {
    private lateinit var adapter: CashOutRecordsAdapter
    private val list = ArrayList<CashOutRecordsRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_cash_out_records
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_cash_out_records)
        rv_cash_out.layoutManager = LinearLayoutManager(this)
        adapter = CashOutRecordsAdapter(list)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        adapter.bindToRecyclerView(rv_cash_out)
    }

    override fun initListener() {
        sml_records.setOnRefreshLoadMoreListener(this)
        tb_cash_out_records.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        adapter.setOnItemClickListener { adapter, view, position ->
            if (DoubleClicksUtils.get().isFastDoubleClick)
                return@setOnItemClickListener
            val intent = Intent(this, CashOutDetailsActivity::class.java)
            intent.putExtra("id",list[position].id)
            startActivity(intent)
        }
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["pageSize"] = "10"
        params["pageNum"] = pageNum.toString()
        params["orderByColumn"] = "createTime"
        RetrofitUtils.get()
            .getJson(WithdrawalListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, CashOutRecordsResponse::class.java)
                    list.addAll(t.rows)
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

    override fun onResume() {
        super.onResume()
        onRefresh(sml_records)
    }
}