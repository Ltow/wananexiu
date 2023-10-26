package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.TypeData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ItemTypeAdapter(data: ArrayList<TypeData>) :
    BaseQuickAdapter<TypeData, BaseViewHolder>(
        R.layout.layout_item_select_type, data
    ) {
    override fun convert(helper: BaseViewHolder, item: TypeData) {
        helper.setText(R.id.cb_item_select_type, item.name)
            .setChecked(R.id.cb_item_select_type, item.isSelect)
    }
}