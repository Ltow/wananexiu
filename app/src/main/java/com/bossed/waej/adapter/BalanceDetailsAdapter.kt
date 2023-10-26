package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.BalanceDetailRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class BalanceDetailsAdapter(mutableList: MutableList<BalanceDetailRow>) :
    BaseQuickAdapter<BalanceDetailRow, BaseViewHolder>(
        R.layout.layout_item_balance_details, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: BalanceDetailRow) {
        helper.setText(R.id.tv_name, item.remark)
            .setText(R.id.tv_time,item.billTime)
            .setText(R.id.tv_money,item.money)
            .setText(R.id.tv_balance,"余额：${item.balance}")
    }
}