package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.StoredBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class StoredAdapter(data: MutableList<StoredBean>) :
    BaseQuickAdapter<StoredBean, BaseViewHolder>(R.layout.layout_item_stored, data) {
    override fun convert(helper: BaseViewHolder, item: StoredBean) {
        helper.setText(R.id.tv_name, item.name)
    }
}