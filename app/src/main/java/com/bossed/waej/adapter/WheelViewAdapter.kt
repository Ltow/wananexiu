package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.RoleRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class WheelViewAdapter(data: MutableList<RoleRow>) :
    BaseQuickAdapter<RoleRow, BaseViewHolder>(R.layout.layout_item_weelview, data) {
    override fun convert(helper: BaseViewHolder, item: RoleRow) {
        helper.setText(R.id.tv_item_weelview,item.roleName)
    }
}