package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.OnlineMenuBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class OnlineMenuAdapter(mutableList: MutableList<OnlineMenuBean>) :
    BaseQuickAdapter<OnlineMenuBean, BaseViewHolder>(
        R.layout.layout_item_online_menu,
        mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: OnlineMenuBean) {
        helper.setText(R.id.tv_name, item.name)
            .setText(R.id.tv_tag, item.tag)
            .setBackgroundRes(R.id.rl_content, item.backGround!!)
    }
}