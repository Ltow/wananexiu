package com.bossed.waej.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossed.waej.R
import com.bossed.waej.javebean.Order
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class BuyHistoryAdapter(data: MutableList<Order>) :
    BaseQuickAdapter<Order, BaseViewHolder>(R.layout.layout_item_buy_history, data) {
    override fun convert(helper: BaseViewHolder, item: Order) {
        helper.setText(R.id.tv_order_id, "订单号：${item.orderSn}")
            .setText(R.id.tv_time, "购买日期：${item.payTime}")
            .setText(R.id.tv_total, "合计：￥${item.orderMoney}")
        val recyclerView = helper.getView<RecyclerView>(R.id.rv_history)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        val historyAdapter = HistoryProductAdapter(item.productList as ArrayList)
        historyAdapter.bindToRecyclerView(recyclerView)
    }
}