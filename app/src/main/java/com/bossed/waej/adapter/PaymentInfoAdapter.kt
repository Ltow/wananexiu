package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.Detail
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class PaymentInfoAdapter(mutableList: MutableList<Detail>) :
    BaseQuickAdapter<Detail, BaseViewHolder>(R.layout.layout_item_payment_info_order, mutableList) {
    override fun convert(helper: BaseViewHolder, item: Detail) {
        helper.setText(R.id.tv_order_id, "业务单号：${item.businessOrderSn}")
            .setText(
                R.id.tv_name,
                item.purchaseOrder.supplierName + " " + item.purchaseOrder.contactsPhone
            )
            .setText(R.id.tv_time, item.billTime)
            .setText(R.id.tv_money, item.purchaseOrder.amount)
            .setText(R.id.tv_czy, "操作员：${item.purchaseOrder.updateBy}")
            .addOnClickListener(R.id.iv_see)
    }
}