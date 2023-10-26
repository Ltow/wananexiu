package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.NotepadRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class NotepadListAdapter(layoutId: Int, data: MutableList<NotepadRow>) :
    BaseQuickAdapter<NotepadRow, BaseViewHolder>(layoutId, data) {
    override fun convert(helper: BaseViewHolder, item: NotepadRow) {
        helper.setText(R.id.tv_item_title, item.title)
            .setText(R.id.tv_item_date, item.updateTime)
            .setText(R.id.tv_item_content, item.content)
    }
}