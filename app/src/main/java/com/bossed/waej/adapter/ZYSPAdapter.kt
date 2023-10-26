package com.bossed.waej.adapter

import com.bossed.waej.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ZYSPAdapter(mutableList: MutableList<Int>, type: Int) :
    BaseQuickAdapter<Int, BaseViewHolder>(
        when (type) {
            1 -> R.layout.layout_item_goods
            2 -> R.layout.layout_item_goods2
            else -> R.layout.layout_item_goods
        }, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: Int) {
        helper.setImageResource(R.id.iv_pic, item)
    }
}