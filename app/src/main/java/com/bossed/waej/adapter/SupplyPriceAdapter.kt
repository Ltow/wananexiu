package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.SupplyPriceBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class SupplyPriceAdapter(mutableList: MutableList<SupplyPriceBean>) :
    BaseQuickAdapter<SupplyPriceBean, BaseViewHolder>(
        R.layout.layout_item_supply_price,
        mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: SupplyPriceBean) {
        helper.setText(R.id.tv_name, "供应商：${item.name}")
            .setText(R.id.tv_pj_name, "配件名称：${item.partName}")
            .setText(R.id.tv_num, item.num.toString())
            .setText(R.id.tv_price, "￥${item.unitPrice}")
            .setText(
                R.id.tv_money,
                "￥${
                    BigDecimal(item.unitPrice!!).multiply(BigDecimal(item.num!!))
                        .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                }"
            )
            .setText(R.id.tv_remark, item.remark)
    }
}