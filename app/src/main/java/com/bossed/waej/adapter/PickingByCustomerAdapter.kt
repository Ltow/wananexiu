package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.PickByCustomerRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class PickingByCustomerAdapter(data: MutableList<PickByCustomerRow>) :
    BaseQuickAdapter<PickByCustomerRow, BaseViewHolder>(R.layout.layout_item_picking_by_customer, data) {
    override fun convert(helper: BaseViewHolder, item: PickByCustomerRow) {
        helper.setText(R.id.tv_order_id, item.orderSn)
            .setText(R.id.tv_car_no, item.cardNo)
            .setText(R.id.tv_name, item.customerName + " " + item.customerPhone)
            .setText(R.id.tv_type, item.carName)
            .setText(R.id.tv_czy, "操作员：${item.updateBy}")
            .setText(R.id.tv_profit, "￥${item.itemCostMoney}")
            .setText(R.id.tv_time,item.updateTime)
            .addOnClickListener(R.id.iv_info)
    }
}