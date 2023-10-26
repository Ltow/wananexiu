package com.bossed.waej.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.ConsumeFlowAdapter
import com.bossed.waej.adapter.RechargeFlowAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.*
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.OnNoRepeatClickListener
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_balance.*

class BalanceActivity : BaseActivity(), OnRefreshLoadMoreListener, OnNoRepeatClickListener {
    private var pageIndex = 1
    private lateinit var consumeFlowAdapter: ConsumeFlowAdapter
    private val consumeArr = ArrayList<FlowDs1>()
    private lateinit var rechargeFlowAdapter: RechargeFlowAdapter
    private val rechargeArr = ArrayList<RechargeFlowDs1>()

    override fun getLayoutId(): Int {
        return R.layout.activity_balance
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_balance)
        rv_flow.layoutManager = LinearLayoutManager(this)
        getBalance()
        switch(0)
    }

    override fun initListener() {
        tb_balance.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        srl_flow.setOnRefreshLoadMoreListener(this)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        consumeArr.clear()
        pageIndex = 1
        getConsumeFlow()
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageIndex++
        getConsumeFlow()
        refreshLayout.finishLoadMore()
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_recharge -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this, RechargeActivity::class.java))
            }

            R.id.tv_consume_flow -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(0)
            }

            R.id.tv_recharge_flow -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(1)
            }
        }
    }

    private fun switch(index: Int) {
        tv_consume_flow.setTextColor(Color.parseColor("#ff333333"))
        tv_recharge_flow.setTextColor(Color.parseColor("#ff333333"))
        when (index) {
            0 -> {
                tv_consume_flow.setTextColor(Color.parseColor("#0095FF"))
                consumeArr.clear()
                consumeFlowAdapter = ConsumeFlowAdapter(consumeArr)
                consumeFlowAdapter.bindToRecyclerView(rv_flow)
                consumeFlowAdapter.emptyView =
                    layoutInflater.inflate(R.layout.layout_empty_view, null)
                getConsumeFlow()
                srl_flow.setEnableRefresh(true)
                srl_flow.setEnableLoadMore(true)
            }

            1 -> {
                tv_recharge_flow.setTextColor(Color.parseColor("#0095FF"))
                rechargeArr.clear()
                rechargeFlowAdapter = RechargeFlowAdapter(rechargeArr)
                rechargeFlowAdapter.bindToRecyclerView(rv_flow)
                rechargeFlowAdapter.emptyView =
                    layoutInflater.inflate(R.layout.layout_empty_view, null)
                getRechargeFlow()
                srl_flow.setEnableRefresh(false)
                srl_flow.setEnableLoadMore(false)
            }
        }
    }

    private fun getConsumeFlow() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "2102"
        params["instr1"] = pageIndex.toString()
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, FlowResponse::class.java)
                    if (t.code == "1") {
                        consumeArr.addAll(t.result.ds1)
                    } else
                        ToastUtils.showShort(t.message)
                    consumeFlowAdapter.setNewData(consumeArr)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getRechargeFlow() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "2201"
        params["inKind2"] = "3"
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, RechargeFlowResponse::class.java)
                    if (t.code == "1") {
                        if (t.result.ds1.isEmpty())
                            ToastUtils.showShort("无充值记录")
                        else
                            rechargeArr.addAll(t.result.ds1)
                    } else
                        ToastUtils.showShort(t.message)
                    rechargeFlowAdapter.setNewData(rechargeArr)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getBalance() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "2201"
        params["inKind2"] = "2"
        params["instr1"] = "cCardnumber"
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BalanceResponse::class.java)
                    if (t.code == "1") {
                        tv_price.text =
                            if (TextUtils.isEmpty(t.result.ds1[0].车型解析))
                                "￥${t.result.ds1[0].充值金额剩余.toFloat() + t.result.ds1[0].免费金额剩余.toFloat()}"
                            else
                                t.result.ds1[0].车型解析
                        tv_balance.text = if (TextUtils.isEmpty(t.result.ds1[0].车型解析))
                            "剩余金额"
                        else
                            "套餐到期日期"
                    } else
                        ToastUtils.showShort(t.message)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }


}