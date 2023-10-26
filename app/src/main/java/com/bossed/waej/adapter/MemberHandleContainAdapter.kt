package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.KmShopClubCardItems
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MemberHandleContainAdapter(data: ArrayList<KmShopClubCardItems>) :
    BaseQuickAdapter<KmShopClubCardItems, BaseViewHolder>(R.layout.layout_item_contain_member, data) {
    override fun convert(helper: BaseViewHolder, item: KmShopClubCardItems) {
        helper.setText(R.id.tv_item_name, item.itemName)
            .setText(R.id.tv_num, "X${item.availableQuantity}次")
    }
}