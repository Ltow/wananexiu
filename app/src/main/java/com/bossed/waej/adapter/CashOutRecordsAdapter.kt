package com.bossed.waej.adapter

import android.graphics.Color
import com.bossed.waej.R
import com.bossed.waej.javebean.CashOutRecordsRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CashOutRecordsAdapter(mutableList: MutableList<CashOutRecordsRow>) :
    BaseQuickAdapter<CashOutRecordsRow, BaseViewHolder>(
        R.layout.layout_item_cash_out_records, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: CashOutRecordsRow) {
        helper.setText(R.id.tv_name, "提现到-${item.acctNo}（${item.nbkName}）")
            .setText(R.id.tv_drawAmt, "￥${item.drawAmt}")
            .setText(R.id.tv_create_time, item.createTime)
            .setText(
                R.id.tv_status, when (item.status) {
                    0 -> "提现中"
                    1 -> "提现成功"
                    2 -> "提现失败"
                    else -> ""
                }
            )
            .setTextColor(
                R.id.tv_status, Color.parseColor(
                    when (item.status) {
                        0 -> "#008DF0"
                        1 -> "#22C134"
                        2 -> "#ff3839"
                        else -> ""
                    }
                )
            )

    }
}