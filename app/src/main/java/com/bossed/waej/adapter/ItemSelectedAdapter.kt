package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.ItemBean
import com.bossed.waej.javebean.ItemRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class ItemSelectedAdapter(data: MutableList<ItemBean>) :
    BaseQuickAdapter<ItemBean, BaseViewHolder>(R.layout.layout_item_selected_item, data) {
    override fun convert(helper: BaseViewHolder, item: ItemBean) {
        helper.setText(R.id.tv_name, item.itemName)
            .setText(R.id.tv_price, "单价：${item.unitPrice}")
            .setText(R.id.tv_num, "数量：${item.num}")
            .setText(
                R.id.tv_money, "金额：${
                    BigDecimal(item.unitPrice.toString()).multiply(BigDecimal(item.num.toString()))
                        .add(
                            BigDecimal(item.serviceFee.toString()).setScale(2, BigDecimal.ROUND_HALF_DOWN)
                        )
                }"
            )
            .setText(R.id.tv_cost, "工时费：${item.serviceFee}")
    }
}