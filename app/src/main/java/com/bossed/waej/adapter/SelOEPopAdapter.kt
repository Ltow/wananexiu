package com.bossed.waej.adapter

import android.view.View
import com.bossed.waej.R
import com.bossed.waej.javebean.PartSearchDs1
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SelOEPopAdapter(data: MutableList<PartSearchDs1>) :
    BaseQuickAdapter<PartSearchDs1, BaseViewHolder>(R.layout.layout_item_sel_oe, data) {
    override fun convert(helper: BaseViewHolder, item: PartSearchDs1) {
        helper.setText(R.id.tv_oe, item.oe)
            .setText(R.id.tv_name, item.oeName)
            .setChecked(R.id.ctv_sel_oe, item.isSel)
//            .addOnClickListener(R.id.tv_details)
        helper.getView<View>(R.id.ctv_sel_oe).visibility = if (item.oeDetails == "1")
            View.VISIBLE else View.GONE
    }
}