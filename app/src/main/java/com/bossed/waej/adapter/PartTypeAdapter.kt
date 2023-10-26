package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.PartTypeListRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class PartTypeAdapter(data: MutableList<PartTypeListRow>) :
    BaseQuickAdapter<PartTypeListRow, BaseViewHolder>(
        R.layout.layout_item_part_type, data
    ) {
    override fun convert(helper: BaseViewHolder, item: PartTypeListRow) {
        helper.setText(R.id.tv_name, item.name)
    }
}