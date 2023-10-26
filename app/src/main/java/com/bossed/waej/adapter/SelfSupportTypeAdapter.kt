package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.GoodsSortData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SelfSupportTypeAdapter(mutableList: MutableList<GoodsSortData>) :
    BaseQuickAdapter<GoodsSortData, BaseViewHolder>(
        R.layout.layout_item_self_support_type, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: GoodsSortData) {
        helper.setText(R.id.ctv_type, item.name)
            .setChecked(R.id.ctv_type, item.isSelected)
    }
}