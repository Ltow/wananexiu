package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.CollectionRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CollectionAdapter(data: MutableList<CollectionRow>) :
    BaseQuickAdapter<CollectionRow, BaseViewHolder>(R.layout.layout_item_collection, data) {
    override fun convert(helper: BaseViewHolder, item: CollectionRow) {
        helper.setText(R.id.tv_order_id, "订单号：${item.orderSn}")
            .setText(R.id.tv_money, "订单金额：￥${item.orderMoney}")
            .setText(R.id.tv_total, "￥${item.moneyOwe}")
            .setText(R.id.tv_che_no, item.cardNo)
            .setText(R.id.tv_model_item, item.carName)
            .setText(R.id.tv_name, item.customerName + " " + item.carCustomerPhone)
            .setChecked(R.id.ctv_collection, item.isSelected)
    }
}