package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.BuyHistoryAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.BuyHistoryUrl
import com.bossed.waej.javebean.BuyHistoryResponse
import com.bossed.waej.javebean.Order
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_buy_history.*

class BuyHistoryActivity : BaseActivity() {
    private lateinit var buyHistoryAdapter: BuyHistoryAdapter
    private val historyList = ArrayList<Order>()

    override fun getLayoutId(): Int {
        return R.layout.activity_buy_history
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        setMarginTop(tb_buy_history)
        BarUtils.setStatusBarLightMode(window, true)
        rv_buy_history.layoutManager = LinearLayoutManager(this)
        buyHistoryAdapter = BuyHistoryAdapter(historyList)
        buyHistoryAdapter.bindToRecyclerView(rv_buy_history)
        getList()
    }

    override fun initListener() {
        tb_buy_history.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        buyHistoryAdapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(this, OrderInfoActivity::class.java)
            intent.putExtra("data", GsonUtils.toJson(historyList[position]))
            startActivity(intent)
        }
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        RetrofitUtils.get()
            .getJson(BuyHistoryUrl, HashMap(), this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BuyHistoryResponse::class.java)
                    historyList.addAll(t.data)
                    buyHistoryAdapter.setNewData(historyList)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}