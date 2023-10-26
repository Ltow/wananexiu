package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.Journal
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class CollectionInfoAccountAdapter(data: MutableList<Journal>, private val type: Int) :
    BaseQuickAdapter<Journal, BaseViewHolder>(R.layout.layout_item_collection_info_account, data) {
    override fun convert(helper: BaseViewHolder, item: Journal) {
        helper.setText(R.id.tv_name, item.methodName)
            .setText(R.id.tv_account_name, item.accountName)
            .setText(R.id.tv_money, "￥${item.money}")
            .setText(R.id.tv_type, if (type == 1) "收款类别" else "付款类别")
            .setText(R.id.tv_account, if (type == 1) "收款账号" else "付款账号")
            .setText(R.id.tv_amount, if (type == 1) "收款金额" else "付款金额")
    }
}