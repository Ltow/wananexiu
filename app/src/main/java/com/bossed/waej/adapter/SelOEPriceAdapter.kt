package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.OESearchDs1
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SelOEPriceAdapter(data: MutableList<OESearchDs1>) :
    BaseQuickAdapter<OESearchDs1, BaseViewHolder>(R.layout.layout_item_sel_oe_price, data) {
    override fun convert(helper: BaseViewHolder, item: OESearchDs1) {
        helper.setText(R.id.tv_price, item.指导价格)
            .setChecked(R.id.ctv_sel_price, item.isSel)
    }
}