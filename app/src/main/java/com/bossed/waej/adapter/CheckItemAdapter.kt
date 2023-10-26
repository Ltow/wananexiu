package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.CheckItemData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CheckItemAdapter(data: MutableList<CheckItemData>) :
    BaseQuickAdapter<CheckItemData, BaseViewHolder>(R.layout.layout_item_check_items, data) {
    override fun convert(helper: BaseViewHolder, item: CheckItemData) {
        helper.setText(R.id.tv_name, item.name)
            .setText(R.id.tv_remark, item.remark)
    }
}