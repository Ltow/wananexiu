package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.CostTypeRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CostTypeAdapter(data: MutableList<CostTypeRow>) :
    BaseQuickAdapter<CostTypeRow, BaseViewHolder>(R.layout.layout_item_cost_type, data) {
    override fun convert(helper: BaseViewHolder, item: CostTypeRow) {
        helper.setText(R.id.tv_name, item.name)
            .setChecked(R.id.sb_person, item.status == 1)
            .addOnClickListener(R.id.iv_edit_item)
            .addOnClickListener(R.id.sb_person)
    }
}