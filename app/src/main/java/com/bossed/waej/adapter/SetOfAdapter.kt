package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.SetOfDs1
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SetOfAdapter(data: MutableList<SetOfDs1>) :
    BaseQuickAdapter<SetOfDs1, BaseViewHolder>(R.layout.layout_item_series_msg, data) {
    override fun convert(helper: BaseViewHolder, item: SetOfDs1) {
        helper.setText(R.id.tv_car_series, item.groupName)
    }
}