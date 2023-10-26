package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.AlertRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class UnAlertAdapter(data: MutableList<AlertRow>) :
    BaseQuickAdapter<AlertRow, BaseViewHolder>(R.layout.layout_item_alert_un, data) {
    override fun convert(helper: BaseViewHolder, item: AlertRow) {
        helper.setText(R.id.tv_name, item.customerName + "     " + item.customerPhone)
            .setText(R.id.tv_car_no, item.cardNo)
            .setText(R.id.tv_remark, "备注：${item.remark}")
            .setText(R.id.tv_createBy, "操作员：${item.updateBy}")
    }
}