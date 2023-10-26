package com.bossed.waej.adapter

import com.bossed.waej.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class WorkerAdapter(mutableList: MutableList<String>, type: Int) :
    BaseQuickAdapter<String, BaseViewHolder>(
        when (type) {
            1 -> R.layout.layout_item_preview_worker
            2 -> R.layout.layout_item_preview_worker2
            3 -> R.layout.layout_item_preview_worker3
            else -> R.layout.layout_item_preview_worker
        }, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.tv_name, item)
    }
}