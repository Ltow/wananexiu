package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.PickBySupplierRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class PickingBySupplierAdapter(data: MutableList<PickBySupplierRow>) :
    BaseQuickAdapter<PickBySupplierRow, BaseViewHolder>(
        R.layout.layout_item_picking_by_supplier,
        data
    ) {
    override fun convert(helper: BaseViewHolder, item: PickBySupplierRow) {
        helper.setText(R.id.tv_name, item.contacts + " " + item.contactsPhone)
            .setText(R.id.tv_order_id, item.orderSn)
            .setText(R.id.tv_car_no, item.supplierName)
            .setText(R.id.tv_profit, "￥${item.amount}")
            .setText(R.id.tv_type, "使用车辆：${item.searchKeywords.split(";")[0]}")
            .setVisible(R.id.tv_time, false)
            .addOnClickListener(R.id.iv_info)
    }
}