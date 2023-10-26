package com.bossed.waej.adapter

import android.graphics.Color
import com.bossed.waej.R
import com.bossed.waej.javebean.AdjRecordRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class RotationHistoryAdapter(data: MutableList<AdjRecordRow>) :
    BaseQuickAdapter<AdjRecordRow, BaseViewHolder>(R.layout.layout_item_rotation_history, data) {
    override fun convert(helper: BaseViewHolder, item: AdjRecordRow) {
        helper.setText(R.id.tv_name, item.payAccountName + " 转到 " + item.accountName)
            .setText(R.id.tv_date, item.billTime)
            .setText(R.id.tv_zy, "摘要：${item.summary}")
            .setText(R.id.tv_money, item.money)
            .setText(R.id.tv_czy, "操作员：${item.updateBy}")
            .setTextColor(
                R.id.tv_money,
                if (item.money.toDouble() < 0) Color.parseColor("#333333") else Color.parseColor("#FF9600")
            )
    }
}