package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.KmAppointRecordVo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ReservationAdapter(mutableList: MutableList<KmAppointRecordVo>) :
    BaseQuickAdapter<KmAppointRecordVo, BaseViewHolder>(
        R.layout.layout_item_online_reservation,
        mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: KmAppointRecordVo) {
        helper.setText(R.id.tv_name, item.cardNo)
            .setText(R.id.tv_time, item.createdTime)
            .setText(R.id.tv_lxr, item.driverName + " " + item.driverPhone)
    }
}