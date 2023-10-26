package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.SeriesDs1
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SeriesAdapter(data: MutableList<SeriesDs1>) :
    BaseQuickAdapter<SeriesDs1, BaseViewHolder>(R.layout.layout_item_series_msg, data) {
    override fun convert(helper: BaseViewHolder, item: SeriesDs1) {
        helper.setText(R.id.tv_car_series, item.familyName)
    }
}