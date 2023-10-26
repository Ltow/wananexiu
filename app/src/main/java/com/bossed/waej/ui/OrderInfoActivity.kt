package com.bossed.waej.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.OrderInfoAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.javebean.Order
import com.bossed.waej.javebean.Product
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_order_info.*

class OrderInfoActivity : BaseActivity() {
    private lateinit var orderInfoAdapter: OrderInfoAdapter
    private val proList = ArrayList<Product>()

    override fun getLayoutId(): Int {
        return R.layout.activity_order_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        setMarginTop(tb_order_info)
        BarUtils.setStatusBarLightMode(window, true)
        rv_product.layoutManager = LinearLayoutManager(this)
        orderInfoAdapter = OrderInfoAdapter(proList)
        orderInfoAdapter.bindToRecyclerView(rv_product)
        orderInfoAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        val t = GsonUtils.fromJson(intent.getStringExtra("data"), Order::class.java)
        tv_id.text = "订单号：${t.orderSn}"
        tv_pay.text = "支付方式：${t.payName}"
        tv_pay_time.text = "支付时间：${t.addTime}"
        tv_termTime.text = "产品到期日期：${t.productList[0].termTime}"
        tv_orderMoney.text = "￥${t.orderMoney}"
        proList.addAll(t.productList)
        orderInfoAdapter.setNewData(proList)
    }

    override fun initListener() {
        tv_close.setOnClickListener { onBackPressed() }
    }

}