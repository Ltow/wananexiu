package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.MemberListBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MemberListAdapter(data: MutableList<MemberListBean>) :
    BaseQuickAdapter<MemberListBean, BaseViewHolder>(R.layout.layout_item_member_list, data) {
    override fun convert(helper: BaseViewHolder, item: MemberListBean) {
        helper.setText(R.id.tv_name, item.name)
            .addOnClickListener(R.id.tv_see)
    }
}