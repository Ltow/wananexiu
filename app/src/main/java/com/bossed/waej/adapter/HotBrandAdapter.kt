package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.Hot
import com.bossed.waej.util.GlideUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class HotBrandAdapter(data: ArrayList<Hot>) :
    BaseQuickAdapter<Hot, BaseViewHolder>(R.layout.layout_item_hot_brand, data) {
    override fun convert(helper: BaseViewHolder, item: Hot) {
        helper.setText(R.id.tv_brand_hot, item.name)
        GlideUtils.get().loadCarLogo(mContext, item.logo, helper.getView(R.id.iv_brand_hot))
    }
}