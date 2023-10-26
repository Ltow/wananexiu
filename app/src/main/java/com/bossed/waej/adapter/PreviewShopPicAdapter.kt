package com.bossed.waej.adapter

import com.bossed.waej.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class PreviewShopPicAdapter(mutableList: MutableList<Int>) :
    BaseQuickAdapter<Int, BaseViewHolder>(R.layout.layout_item_preview_shop, mutableList) {
    override fun convert(helper: BaseViewHolder, item: Int) {
        helper.setImageResource(R.id.iv_preview, item)
    }
}