package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.CheckListRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CheckHistoryAdapter(mutableList: MutableList<CheckListRow>) :
    BaseQuickAdapter<CheckListRow, BaseViewHolder>(
        R.layout.layout_item_check_history, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: CheckListRow) {
        helper.setText(R.id.tv_time, item.settleTime)
            .setText(R.id.tv_order_sn, "单号：${item.orderSn}")
            .setText(R.id.tv_czy, "操作员：${item.createBy}")
    }
}