package com.bossed.waej.adapter

import android.view.View
import com.bossed.waej.R
import com.bossed.waej.javebean.PartSearchDs1
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class PartSearchAdapter(data: MutableList<PartSearchDs1>) :
    BaseQuickAdapter<PartSearchDs1, BaseViewHolder>(
        R.layout.layout_item_part_search, data
    ) {
    override fun convert(helper: BaseViewHolder, item: PartSearchDs1) {
        helper.setText(R.id.tv_oe, item.oe)
            .setText(R.id.tv_name, item.oeName)
            .setText(R.id.tv_name_bz, item.stdPartName)
            .setText(R.id.tv_brand_name, item.partNum)
            .setText(R.id.tv_price, item.partGroupName)
            .setText(R.id.tv_price_hn, item.picNo)
            .setText(R.id.tv_price_hd, item.orderNo)
            .addOnClickListener(R.id.tv_details)
        helper.getView<View>(R.id.tv_details).visibility = if (item.oeDetails == "1")
            View.VISIBLE else View.GONE
    }
}