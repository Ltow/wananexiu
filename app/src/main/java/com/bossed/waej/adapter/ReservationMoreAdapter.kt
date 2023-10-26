package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.RecordListRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ReservationMoreAdapter(mutableList: MutableList<RecordListRow>) :
    BaseQuickAdapter<RecordListRow, BaseViewHolder>(
        R.layout.layout_item_reservation, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: RecordListRow) {
        helper.setText(R.id.tv_time, item.cardNo)
    }
}