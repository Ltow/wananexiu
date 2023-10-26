package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.JournalSearchDataRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CostCountInfoAdapter(data: MutableList<JournalSearchDataRow>) :
    BaseQuickAdapter<JournalSearchDataRow, BaseViewHolder>(
        R.layout.layout_item_cost_count_info,
        data
    ) {
    override fun convert(helper: BaseViewHolder, item: JournalSearchDataRow) {
        helper.setText(R.id.tv_name, "摘要：${item.summary}")
            .setText(R.id.tv_money, "${item.money}元")
            .setText(R.id.tv_payAccountName, "付款账号：${item.accountName}")
            .setText(R.id.tv_czy, "操作员：${item.updateBy}")
            .setText(R.id.tv_date, item.billTime)
    }
}