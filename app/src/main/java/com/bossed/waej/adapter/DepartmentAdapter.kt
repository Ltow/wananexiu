package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.DepartListData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class DepartmentAdapter(data: MutableList<DepartListData>) :
    BaseQuickAdapter<DepartListData, BaseViewHolder>(R.layout.layout_item_depart, data) {
    override fun convert(helper: BaseViewHolder, item: DepartListData) {
        helper.setText(R.id.tv_name, item.deptName)
            .addOnClickListener(R.id.iv_delete)
            .addOnClickListener(R.id.iv_edit)
    }
}