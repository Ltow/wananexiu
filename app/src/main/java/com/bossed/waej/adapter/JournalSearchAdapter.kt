package com.bossed.waej.adapter

import android.graphics.Color
import com.bossed.waej.R
import com.bossed.waej.javebean.JournalSearchDataRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class JournalSearchAdapter(data: MutableList<JournalSearchDataRow>) :
    BaseQuickAdapter<JournalSearchDataRow, BaseViewHolder>(
        R.layout.layout_item_journal_search,
        data
    ) {
    override fun convert(helper: BaseViewHolder, item: JournalSearchDataRow) {
        helper.setText(R.id.tv_name, item.accountName)
            .setText(R.id.tv_type, "(${item.methodName})")
            .setText(R.id.tv_billTime, item.billTime)
            .setText(R.id.tv_summary, "摘要：${item.summary}")
            .setText(
                R.id.tv_money_item,
                if (item.money.toDouble() > 0) "+${item.money}" else item.money
            )
            .setText(R.id.tv_balance, "余额：${item.balance}")
            .setText(R.id.tv_czy, "操作员：${item.updateBy}")
            .setTextColor(
                R.id.tv_money_item,
                if (item.money.toDouble() < 0) Color.parseColor("#333333") else Color.parseColor("#FF9600")
            )
    }
}