package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.PurchaseOrderRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class PaymentAdapter(data: MutableList<PurchaseOrderRow>) :
    BaseQuickAdapter<PurchaseOrderRow, BaseViewHolder>(R.layout.layout_item_payment, data) {
    override fun convert(helper: BaseViewHolder, item: PurchaseOrderRow) {
        helper.setText(R.id.tv_name, item.supplierName)
            .setText(R.id.tv_order_id, "订单号：${item.orderSn}")
            .setText(R.id.tv_money, "订单金额：${item.amount}")
            .setText(R.id.tv_owe, "剩余应付：${item.moneyOwe}")
            .setText(R.id.tv_czy, "操作员：${item.updateBy}")
            .setText(R.id.tv_time, item.settleTime)
            .setChecked(R.id.ctv_sel_payment, item.isSelected)
    }
}