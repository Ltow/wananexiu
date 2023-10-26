package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.CollectionSearchRow
import com.bossed.waej.util.DateFormatUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class CollectionSearchAdapter(data: MutableList<CollectionSearchRow>) :
    BaseQuickAdapter<CollectionSearchRow, BaseViewHolder>(
        R.layout.layout_item_collection_search, data
    ) {
    override fun convert(helper: BaseViewHolder, item: CollectionSearchRow) {
        helper.setText(R.id.tv_name, item.customerName + "  " + item.customerPhone)
            .setText(R.id.tv_date, DateFormatUtils.get().formatDate(item.billTime))
            .setText(R.id.tv_czy, "操作员：${item.updateBy}")
            .setText(
                R.id.tv_owe,
                "￥${
                    BigDecimal(item.moneyPay).add(BigDecimal(item.discount))
                        .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                }"
            )
            .setText(
                R.id.tv_number,
                "收款单号：${item.orderSn}"
            )
            .setText(R.id.tv_amount, "收款金额：")
            .setText(R.id.tv_discount, "(包含减免金额${item.discount}元)")
    }
}