package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.DetailX
import com.bossed.waej.javebean.SupplyPriceBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class SupplyPriceBySupplierAdapter(mutableList: MutableList<DetailX>) :
    BaseQuickAdapter<DetailX, BaseViewHolder>(
        R.layout.layout_item_supply_price,
        mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: DetailX) {
        helper.setText(R.id.tv_name, "配件名称：${item.itemName}")
            .setText(R.id.tv_num, item.quantity.toString())
            .setText(R.id.tv_price, "￥${item.cost}")
            .setText(R.id.tv_money, item.amount)
            .setVisible(R.id.tv_pj_name, false)
    }
}