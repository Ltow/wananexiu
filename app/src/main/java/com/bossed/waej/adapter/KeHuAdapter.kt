package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.KeHuData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class KeHuAdapter(data: ArrayList<KeHuData>) :
    BaseQuickAdapter<KeHuData, BaseViewHolder>(R.layout.layout_item_kehu, data) {
    override fun convert(helper: BaseViewHolder, item: KeHuData) {
        helper.setText(R.id.tv_item_kehu, item.customerName)
            .setText(R.id.tv_item_phone, item.customerPhone)
            .setText(R.id.tv_car_no, item.cardNo)
    }
}