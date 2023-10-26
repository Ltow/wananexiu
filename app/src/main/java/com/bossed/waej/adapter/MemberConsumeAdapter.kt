package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.MemberConsumeBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MemberConsumeAdapter(data: MutableList<MemberConsumeBean>) :
    BaseQuickAdapter<MemberConsumeBean, BaseViewHolder>(R.layout.layout_item_member_consume, data) {
    override fun convert(helper: BaseViewHolder, item: MemberConsumeBean) {
        helper.setText(R.id.tv_che_no, item.name)
    }
}