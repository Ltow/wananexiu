package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.FreeTestingData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class FreeTestContentAdapter(mutableList: MutableList<FreeTestingData>) :
    BaseQuickAdapter<FreeTestingData, BaseViewHolder>(
        R.layout.layout_item_free_test, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: FreeTestingData) {
        helper.setText(R.id.ctv_free, item.name)
            .setChecked(R.id.ctv_free, item.isSelect)
    }
}