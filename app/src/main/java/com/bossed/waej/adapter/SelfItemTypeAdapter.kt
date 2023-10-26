package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.ShopItemSortData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SelfItemTypeAdapter(mutableList: MutableList<ShopItemSortData>) :
    BaseQuickAdapter<ShopItemSortData, BaseViewHolder>(
        R.layout.layout_item_self_support_type, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: ShopItemSortData) {
        helper.setText(R.id.ctv_type, item.name)
            .setChecked(R.id.ctv_type, item.isSelected)
    }
}