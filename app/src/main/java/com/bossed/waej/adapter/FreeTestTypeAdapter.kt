package com.bossed.waej.adapter

import android.graphics.Color
import com.bossed.waej.R
import com.bossed.waej.javebean.FreeTestingData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class FreeTestTypeAdapter(mutableList: MutableList<FreeTestingData>) :
    BaseQuickAdapter<FreeTestingData, BaseViewHolder>(
        R.layout.layout_item_free_test_type, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: FreeTestingData) {
        helper.setText(R.id.tv_name, item.name)
            .setTextColor(
                R.id.tv_name,
                if (item.isSelect) Color.parseColor("#ffffff") else Color.parseColor("#333333")
            )
            .setBackgroundRes(
                R.id.tv_name,
                if (item.isSelect) R.drawable.free_test_item_bg else R.color.white
            )
    }
}