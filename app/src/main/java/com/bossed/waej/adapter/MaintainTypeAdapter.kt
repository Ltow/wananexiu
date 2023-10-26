package com.bossed.waej.adapter

import android.widget.CheckBox
import com.bossed.waej.R
import com.bossed.waej.javebean.MaintainCate
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MaintainTypeAdapter(data: ArrayList<MaintainCate>) :
    BaseQuickAdapter<MaintainCate, BaseViewHolder>(R.layout.layout_item_select_type, data) {
    override fun convert(helper: BaseViewHolder, item: MaintainCate) {
        helper.setText(R.id.cb_item_select_type, item.name)
            .setChecked(R.id.cb_item_select_type, item.isSelect)
        helper.getView<CheckBox>(R.id.cb_item_select_type)
            .setOnCheckedChangeListener { buttonView, isChecked ->
                item.isSelect = !item.isSelect
            }
    }
}