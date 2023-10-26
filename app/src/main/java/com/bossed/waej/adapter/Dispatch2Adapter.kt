package com.bossed.waej.adapter

import android.widget.CheckedTextView
import com.bossed.waej.R
import com.bossed.waej.javebean.PersonRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class Dispatch2Adapter(data: MutableList<PersonRow>) :
    BaseQuickAdapter<PersonRow, BaseViewHolder>(R.layout.layout_item_dispatch2, data) {
    override fun convert(helper: BaseViewHolder, item: PersonRow) {
        helper.setText(R.id.tv_name, item.name)
            .setChecked(R.id.tv_name, item.isSelect)
        val textView = helper.getView<CheckedTextView>(R.id.tv_name)
        textView.setOnClickListener {
            textView.isChecked = !textView.isChecked
            item.isSelect = textView.isChecked
        }
    }
}