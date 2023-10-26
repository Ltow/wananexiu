package com.bossed.waej.adapter

import android.graphics.Color
import android.widget.TextView
import com.bossed.waej.R
import com.bossed.waej.javebean.RechargeFlowDs1
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class RechargeFlowAdapter(data: MutableList<RechargeFlowDs1>) :
    BaseQuickAdapter<RechargeFlowDs1, BaseViewHolder>(
        R.layout.layout_item_flow, data
    ) {
    override fun convert(helper: BaseViewHolder, item: RechargeFlowDs1) {
        helper.setText(R.id.tv_name, item.备注)
            .setText(R.id.tv_price, "+" + item.金额)
            .setText(R.id.tv_time, item.时间)
        helper.getView<TextView>(R.id.tv_price).setTextColor(Color.parseColor("#F8BB00"))
    }
}