package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.FlowDs1
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ConsumeFlowAdapter(data: MutableList<FlowDs1>) :
    BaseQuickAdapter<FlowDs1, BaseViewHolder>(R.layout.layout_item_flow, data) {
    override fun convert(helper: BaseViewHolder, item: FlowDs1) {
        helper.setText(R.id.tv_name, item.备注)
            .setText(R.id.tv_price, "-" + item.费用)
            .setText(R.id.tv_time, item.时间)
    }
}