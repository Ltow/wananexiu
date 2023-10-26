package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.CostCountRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CostCountAdapter(data: MutableList<CostCountRow>) :
    BaseQuickAdapter<CostCountRow, BaseViewHolder>(R.layout.layout_item_cost_count, data) {
    override fun convert(helper: BaseViewHolder, item: CostCountRow) {
        helper.setText(R.id.tv_name, item.name)
            .setText(R.id.tv_money, "${item.money}元")
    }
}