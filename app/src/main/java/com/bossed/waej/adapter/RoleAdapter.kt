package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.RoleRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class RoleAdapter(data: MutableList<RoleRow>) :
    BaseQuickAdapter<RoleRow, BaseViewHolder>(R.layout.layout_item_role, data) {
    override fun convert(helper: BaseViewHolder, item: RoleRow) {
        helper.setText(R.id.tv_name, item.roleName)
            .addOnClickListener(R.id.iv_edit)
            .addOnClickListener(R.id.iv_delete)
    }
}