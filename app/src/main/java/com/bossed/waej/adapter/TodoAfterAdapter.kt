package com.bossed.waej.adapter

import com.bossed.waej.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class TodoAfterAdapter(mutableList: MutableList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.layout_item_todo_after, mutableList) {
    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.tv_name, item)
    }
}