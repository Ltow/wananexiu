package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.ReplaceDs1
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ReplaceAdapter(data: MutableList<ReplaceDs1>) :
    BaseQuickAdapter<ReplaceDs1, BaseViewHolder>(R.layout.layout_item_replace, data) {
    override fun convert(helper: BaseViewHolder, item: ReplaceDs1) {
        helper.setText(R.id.tv_oe, item.oE)
            .setText(R.id.tv_name, item.oeName)
            .setText(R.id.tv_name_bz, item.replacerTypeName)
            .setText(R.id.tv_brand_name, item.guidePrice)
            .setText(R.id.tv_price_hd, item.oeOrder)
    }
}