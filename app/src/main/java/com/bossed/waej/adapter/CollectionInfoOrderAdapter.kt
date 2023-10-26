package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.CollectionDetail
import com.bossed.waej.javebean.Detail
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CollectionInfoOrderAdapter(data: MutableList<CollectionDetail>) :
    BaseQuickAdapter<CollectionDetail, BaseViewHolder>(
        R.layout.layout_item_collection_info_order,
        data
    ) {
    override fun convert(helper: BaseViewHolder, item: CollectionDetail) {
        helper.setText(R.id.tv_name, "订单号：${item.workOrder.orderSn}")
            .setText(R.id.tv_car_no, item.workOrder.cardNo)
            .setText(R.id.tv_type, item.workOrder.carName)
            .setText(R.id.tv_czy, "操作员：${item.workOrder.updateBy}")
            .setText(R.id.tv_profit, "￥${item.workOrder.orderMoney}")
            .setText(
                R.id.tv_lxr_name,
                item.workOrder.customerName + " " + item.workOrder.customerPhone
            )
            .addOnClickListener(R.id.iv_see)
    }
}