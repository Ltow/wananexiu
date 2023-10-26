package com.bossed.waej.adapter

import android.graphics.Color
import android.view.View
import com.bossed.waej.R
import com.bossed.waej.javebean.ItemBean
import com.bossed.waej.javebean.ItemRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CommonItemAdapter(data: MutableList<ItemBean>) :
    BaseQuickAdapter<ItemBean, BaseViewHolder>(R.layout.layout_item_common_item, data) {
    override fun convert(helper: BaseViewHolder, item: ItemBean) {
        helper.setText(R.id.tv_name, item.itemName)
            .setText(R.id.tv_price, item.unitPrice.toString())
            .setChecked(R.id.cb_item_common, item.isSelect)
        if (helper.layoutPosition % 2 == 0)
            helper.getView<View>(R.id.rl_common_item)
                .setBackgroundColor(Color.parseColor("#F5F5F5"))
        else
            helper.getView<View>(R.id.rl_common_item)
                .setBackgroundColor(Color.parseColor("#FFFFFF"))
    }
}