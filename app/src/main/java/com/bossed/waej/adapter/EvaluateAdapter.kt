package com.bossed.waej.adapter

import com.bossed.waej.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class EvaluateAdapter(mutableList: MutableList<String>, type: Int) :
    BaseQuickAdapter<String, BaseViewHolder>(
        when (type) {
            1 -> R.layout.layout_item_ping_jia
            2 -> R.layout.layout_item_ping_jia2
            3 -> R.layout.layout_item_ping_jia3
            else -> R.layout.layout_item_ping_jia
        }, mutableList
    ) {
    private var type: Int = 0

    init {
        this.type = type
    }

    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.tv_name, item)
        if (type == 5) {
            helper.setImageResource(R.id.iv_head, R.mipmap.icon_preview_head5)
                .setVisible(R.id.iv_vip, false)
        }
    }
}