package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.PurchaseListRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class PurchaseListAdapter(mutableList: MutableList<PurchaseListRow>) :
    BaseQuickAdapter<PurchaseListRow, BaseViewHolder>(
        R.layout.layout_item_purchase_list,
        mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: PurchaseListRow) {
        helper.setText(R.id.tv_name, item.supplierName)
            .setText(R.id.tv_time, item.settleTime)
            .setText(R.id.tv_contacts, item.contacts + " " + item.contactsPhone)
            .addOnClickListener(R.id.iv_edit)
            .addOnClickListener(R.id.iv_delete)
    }
}