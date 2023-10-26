package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.UseTypeDs1
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class UseTypeAdapter(data: MutableList<UseTypeDs1>) :
    BaseQuickAdapter<UseTypeDs1, BaseViewHolder>(R.layout.layout_item_use_type,data) {
    override fun convert(helper: BaseViewHolder, item: UseTypeDs1) {
        helper.setText(R.id.tv_explain, item.操作说明)
            .setText(R.id.tv_number, item.次数)
            .setText(R.id.tv_money, item.金额)
    }
}