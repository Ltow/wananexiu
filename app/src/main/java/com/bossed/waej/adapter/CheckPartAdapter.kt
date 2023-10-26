package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.CheckPartData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CheckPartAdapter(data: MutableList<CheckPartData>) :
    BaseQuickAdapter<CheckPartData, BaseViewHolder>(R.layout.layout_item_check_items,data) {
    override fun convert(helper: BaseViewHolder, item: CheckPartData) {
        helper.setText(R.id.tv_name, item.name)
            .setText(R.id.tv_remark, item.specName)
    }
}