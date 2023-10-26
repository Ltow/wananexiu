package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.RepairBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class RepairAdapter(layoutId: Int, list: ArrayList<RepairBean>) :
    BaseQuickAdapter<RepairBean, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder, item: RepairBean) {
        helper.setText(R.id.tv_order_id_repair, "订单号：" + item.orderId)
            .setText(R.id.tv_add_time_repair, "下单时间：" + item.addTime)
            .setText(
                R.id.tv_delivery_status, when (item.orderStatus) {
                    1 -> "待发货"
                    2 -> "待收货"
                    4 -> "已完成"
                    else -> ""
                }
            )
            .addOnClickListener(R.id.tv_check_details_repair)
    }
}