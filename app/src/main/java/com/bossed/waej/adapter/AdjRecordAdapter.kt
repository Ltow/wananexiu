package com.bossed.waej.adapter

import android.graphics.Color
import com.bossed.waej.R
import com.bossed.waej.javebean.AdjRecordRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AdjRecordAdapter(data: MutableList<AdjRecordRow>) :
    BaseQuickAdapter<AdjRecordRow, BaseViewHolder>(R.layout.layout_item_journal_search, data) {
    override fun convert(helper: BaseViewHolder, item: AdjRecordRow) {
        helper.setText(R.id.tv_name, item.accountName)
            .setText(R.id.tv_billTime, item.billTime)
            .setText(R.id.tv_summary, "摘要：${item.summary}")
            .setText(R.id.tv_money_item, item.money)
            .setText(R.id.tv_czy, "操作员：${item.updateBy}")
            .setText(R.id.tv_balance, "余额：${item.balance}")
            .setTextColor(
                R.id.tv_money_item,
                if (item.money.toDouble() < 0) Color.parseColor("#333333") else Color.parseColor("#FF9600")
            )
    }
}