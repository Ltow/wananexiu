package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.DataXX
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CarDisplacementAdapter(data: ArrayList<DataXX>) :
    BaseQuickAdapter<DataXX, BaseViewHolder>(R.layout.layout_item_series_msg, data) {
    override fun convert(helper: BaseViewHolder, item: DataXX) {
        helper.setText(R.id.tv_car_series, item.name)
    }
}