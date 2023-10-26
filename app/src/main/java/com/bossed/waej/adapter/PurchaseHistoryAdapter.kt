package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.PurchaseListRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class PurchaseHistoryAdapter(mutableList: MutableList<PurchaseListRow>, private val type: Int) :
    BaseQuickAdapter<PurchaseListRow, BaseViewHolder>(
        R.layout.layout_item_purchase_history, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: PurchaseListRow) {
        helper.setText(R.id.tv_name, "单号：${item.orderSn}")
            .setText(R.id.tv_supplierName, item.supplierName)
            .setText(
                R.id.tv_money,
                if (type == 1) "进货金额：￥${item.amount}" else "退货金额：￥${item.amount}"
            )
            .setText(R.id.tv_czy, "操作员：${item.createBy}")
            .setText(R.id.tv_time, item.settleTime)
    }
}