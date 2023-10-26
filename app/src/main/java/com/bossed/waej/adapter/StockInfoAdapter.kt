package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.Details
import com.bossed.waej.javebean.DetailsRow
import com.bossed.waej.util.DateFormatUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class StockInfoAdapter(mutableList: MutableList<DetailsRow>) :
    BaseQuickAdapter<DetailsRow, BaseViewHolder>(R.layout.layout_item_stock_info, mutableList) {
    override fun convert(helper: BaseViewHolder, item: DetailsRow) {
        helper.setText(R.id.tv_date, item.billTime)
            .setText(
                R.id.tv_quantity,
                "数量：${if (BigDecimal(item.quantity) > BigDecimal(0)) "+${item.quantity}" else item.quantity}"
            )
            .setText(R.id.tv_cost, "单价：${item.cost}")
            .setText(
                R.id.tv_amount,
                "金额：${if (BigDecimal(item.amount) > BigDecimal(0)) "+${item.amount}" else item.amount}"
            )
            .setText(R.id.tv_balanceQuantity, "库存：${item.balanceQuantity}")
            .setText(R.id.tv_balanceAmount, "库存金额：${item.balanceAmount}")
            .setText(R.id.tv_summary, "摘要：${item.summary}")
    }
}