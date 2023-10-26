package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.BankAccountRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SelAccountAdapter2(mutableList: MutableList<BankAccountRow>) :
    BaseQuickAdapter<BankAccountRow, BaseViewHolder>(
        R.layout.layout_item_account_sel,
        mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: BankAccountRow) {
        helper.setText(R.id.tv_name, item.name)
    }
}