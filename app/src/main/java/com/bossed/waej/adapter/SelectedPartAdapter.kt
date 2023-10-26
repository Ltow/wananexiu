package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.PartListRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class SelectedPartAdapter(mutableList: MutableList<PartListRow>) :
    BaseQuickAdapter<PartListRow, BaseViewHolder>(
        R.layout.layout_item_selected_part, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: PartListRow) {
        helper.setText(R.id.tv_name, item.name)
            .setText(R.id.tv_sel_num, "数量：${item.num}")
            .setText(
                R.id.tv_money,
                "金额：${BigDecimal(item.num!!).multiply(BigDecimal(item.purchasePrice))}"
            )
    }
}