package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.DataXXX
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CarYearsAdapter(data: ArrayList<DataXXX>) :
    BaseQuickAdapter<DataXXX, BaseViewHolder>(R.layout.layout_item_series_msg, data) {
    override fun convert(helper: BaseViewHolder, item: DataXXX) {
        helper.setText(R.id.tv_car_series, item.name)
    }
}