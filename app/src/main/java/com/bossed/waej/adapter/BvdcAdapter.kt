package com.bossed.waej.adapter

import android.widget.ImageView
import com.bossed.waej.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class BvdcAdapter(data: MutableList<Int>) :
    BaseQuickAdapter<Int, BaseViewHolder>(R.layout.layout_item_bvdc_menu, data) {
    override fun convert(helper: BaseViewHolder, item: Int) {
        helper.getView<ImageView>(R.id.iv_bvdc_menu).setImageResource(item)
    }
}