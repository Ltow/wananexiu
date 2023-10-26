package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.ExchangeData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ExchangeAdapter(mutableList: MutableList<ExchangeData>) :
    BaseQuickAdapter<ExchangeData, BaseViewHolder>(R.layout.layout_item_exchange, mutableList) {
    override fun convert(helper: BaseViewHolder, item: ExchangeData) {
        helper.setText(R.id.tv_name, item.packageName)
            .setText(R.id.tv_content, "包含功能：${item.remark}")
    }
}