package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.DataXXXX
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CarModelAdapter(data: ArrayList<DataXXXX>) :
    BaseQuickAdapter<DataXXXX, BaseViewHolder>(R.layout.layout_item_series_msg, data) {
    override fun convert(helper: BaseViewHolder, item: DataXXXX) {
        helper.setText(R.id.tv_car_series, item.name)
    }
}