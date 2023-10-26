package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.TypeData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ItemTypeListAdapter(data: MutableList<TypeData>) :
    BaseQuickAdapter<TypeData, BaseViewHolder>(R.layout.layout_item_part_type,data) {
    override fun convert(helper: BaseViewHolder, item: TypeData) {
        helper.setText(R.id.tv_name, item.name)
    }
}