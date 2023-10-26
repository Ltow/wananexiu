package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.SupplierRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SupplierListAdapter(data: ArrayList<SupplierRow>) :
    BaseQuickAdapter<SupplierRow, BaseViewHolder>(R.layout.layout_item_supplier, data) {

    override fun convert(helper: BaseViewHolder, item: SupplierRow) {
        helper.setText(R.id.tv_gys_name, item.name)
            .setText(R.id.tv_name_phone, item.contactsPhone)
            .setText(R.id.tv_address, "地址：${item.address}")
            .setText(
                R.id.tv_balance, if (item.supplierPayment != null) "￥${item.supplierPayment.balance}" else "0.00"
            )
    }
}