package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.ModelDs1
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ModelAdapter(data: MutableList<ModelDs1>) :
    BaseQuickAdapter<ModelDs1, BaseViewHolder>(R.layout.layout_item_series_msg, data) {
    override fun convert(helper: BaseViewHolder, item: ModelDs1) {
        helper.setText(R.id.tv_car_series, item.车型名称)
    }
}