package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.PartListRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SelectPartAdapter2(mutableList: MutableList<PartListRow>) :
    BaseQuickAdapter<PartListRow, BaseViewHolder>(R.layout.layout_item_select_part2, mutableList) {
    override fun convert(helper: BaseViewHolder, item: PartListRow) {
        helper.setText(R.id.tv_name, item.name)
            .setText(R.id.tv_oe, "OE号：${item.oe}")
            .setText(R.id.tv_code, "自编码：${item.code}")
            .setText(R.id.tv_brand, "品牌：${item.brand}")
            .setText(R.id.tv_model, "型号：${item.model}")
            .setText(R.id.tv_quantity, "账面库存：${item.partStore!!.quantity}")
            .setChecked(R.id.ctv_sel_part, item.isSelected)
    }
}