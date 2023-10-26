package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.ApplicableDs1
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ApplicableAdapter(data: MutableList<ApplicableDs1>) :
    BaseQuickAdapter<ApplicableDs1, BaseViewHolder>(R.layout.layout_item_applicable, data) {
    override fun convert(helper: BaseViewHolder, item: ApplicableDs1) {
        helper.setText(R.id.tv_name, item.车型)
    }
}