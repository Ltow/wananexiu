package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.WorkOrder
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class GivePersonAdapter(data: MutableList<WorkOrder>) :
    BaseQuickAdapter<WorkOrder, BaseViewHolder>(R.layout.layout_item_give_person, data) {
    override fun convert(helper: BaseViewHolder, item: WorkOrder) {
        helper.setText(R.id.tv_name, item.customerName)
            .setText(R.id.tv_phone, item.customerPhone)
            .setText(R.id.tv_date, item.finishedTime)
    }
}