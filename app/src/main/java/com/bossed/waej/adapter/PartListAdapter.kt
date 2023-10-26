package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.PartListRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class PartListAdapter(mutableList: MutableList<PartListRow>) :
    BaseQuickAdapter<PartListRow, BaseViewHolder>(R.layout.layout_item_part, mutableList) {
    override fun convert(helper: BaseViewHolder, item: PartListRow) {
        helper.setText(R.id.tv_name, item.name)
            .setText(R.id.tv_oe, "OE号：${item.oe}")
            .setText(R.id.tv_model, "规格/型号：${item.model}")
            .setText(R.id.tv_unit, "单位：${item.unit}")
            .setText(R.id.tv_code, "自编码：${item.code}")
            .setText(R.id.tv_brand, "品牌/产地：${item.brand}")
            .setText(R.id.tv_price, "销售参考价：${item.marketPrice}")
    }
}