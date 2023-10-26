package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.VinSearchBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class VINSearchAdapter(data: MutableList<VinSearchBean>) :
    BaseQuickAdapter<VinSearchBean, BaseViewHolder>(R.layout.layout_item_vin_search, data) {
    override fun convert(helper: BaseViewHolder, item: VinSearchBean) {
        helper.setText(R.id.tv_name_ms, "${item.name}：")
            .setText(R.id.tv_name, item.content)
    }
}