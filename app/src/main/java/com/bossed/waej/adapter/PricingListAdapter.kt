package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.PricingListRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class PricingListAdapter(data: MutableList<PricingListRow>) :
    BaseQuickAdapter<PricingListRow, BaseViewHolder>(R.layout.layout_item_pricing, data) {
    override fun convert(helper: BaseViewHolder, item: PricingListRow) {
        helper.setText(R.id.tv_car_no, item.cardNo)
            .setText(R.id.tv_order_id, item.orderSn)
            .setText(R.id.tv_type, item.carName)
            .setText(R.id.tv_profit, "￥${item.orderMoney}")
            .setText(R.id.tv_name, item.customerName + " " + item.customerPhone)
            .setText(R.id.tv_time, item.updateTime)
            .addOnClickListener(R.id.iv_info)
    }
}