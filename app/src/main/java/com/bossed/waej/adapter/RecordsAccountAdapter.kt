package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.RecordsAccountRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class RecordsAccountAdapter(mutableList: MutableList<RecordsAccountRow>) :
    BaseQuickAdapter<RecordsAccountRow, BaseViewHolder>(
        R.layout.layout_item_records_account, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: RecordsAccountRow) {
        helper.setText(R.id.tv_name, if (item.cardType == "58") "个人银行卡" else "企业银行卡")
            .setText(R.id.tv_bankName, item.bankName)
            .setText(R.id.tv_cardNo, "卡号：${item.cardNo}")
            .setText(R.id.tv_cardName, "（${item.cardName}）")
            .setBackgroundRes(
                R.id.rl_content,
                if (item.cardType == "58") R.mipmap.icon_gr_bank else R.mipmap.icon_qy_bank
            )
    }
}