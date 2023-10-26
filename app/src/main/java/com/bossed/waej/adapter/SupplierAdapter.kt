package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.SupplierInfoData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SupplierAdapter(data: MutableList<SupplierInfoData>) :
    BaseQuickAdapter<SupplierInfoData, BaseViewHolder>(R.layout.layout_item_check_supplier, data) {
    override fun convert(helper: BaseViewHolder, item: SupplierInfoData) {
        helper.setText(R.id.tv_item_kehu, item.name)
            .setText(R.id.tv_item_phone, item.contactsPhone)
    }
}