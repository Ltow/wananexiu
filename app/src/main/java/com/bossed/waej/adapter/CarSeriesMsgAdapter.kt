package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.CarModelDataX
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CarSeriesMsgAdapter(data: ArrayList<CarModelDataX>) :
    BaseQuickAdapter<CarModelDataX, BaseViewHolder>(R.layout.layout_item_series_msg, data) {
    override fun convert(helper: BaseViewHolder, item: CarModelDataX) {
        helper.setText(R.id.tv_car_series, item.name)
    }
}