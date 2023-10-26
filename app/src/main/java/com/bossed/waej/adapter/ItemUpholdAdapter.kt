package com.bossed.waej.adapter

import android.graphics.Color
import android.widget.CheckBox
import android.widget.RelativeLayout
import com.bossed.waej.R
import com.bossed.waej.javebean.ItemRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ItemUpholdAdapter(data: ArrayList<ItemRow>) :
    BaseQuickAdapter<ItemRow, BaseViewHolder>(R.layout.layout_item_uphold_item, data) {
    override fun convert(helper: BaseViewHolder, item: ItemRow) {
        helper.setText(R.id.tv_item_name, item.name)
            .setText(R.id.tv_item_content, item.marketPrice.toString())
            .setChecked(R.id.cb_item_select, item.isSelect)
        val checkBox = helper.getView<CheckBox>(R.id.cb_item_select)
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            item.isSelect = isChecked
        }
        if (helper.layoutPosition % 2 == 0) {
            helper.getView<RelativeLayout>(R.id.rl_item_uphold)
                .setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            helper.getView<RelativeLayout>(R.id.rl_item_uphold)
                .setBackgroundColor(Color.parseColor("#F5F5F5"))
        }
    }
}