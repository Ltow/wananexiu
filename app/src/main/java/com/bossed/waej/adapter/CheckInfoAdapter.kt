package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.CheckInfoDataDetailsRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class CheckInfoAdapter(mutableList: MutableList<CheckInfoDataDetailsRow>) :
    BaseQuickAdapter<CheckInfoDataDetailsRow, BaseViewHolder>(
        R.layout.layout_item_check_info,
        mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: CheckInfoDataDetailsRow) {
        helper.setText(R.id.tv_name, item.part.name)
            .setText(R.id.tv_code, "自编码：${item.part.code}")
            .setText(R.id.tv_model, "规格/型号：${item.part.model}")
            .setText(R.id.tv_brand, "品牌/产地：${item.part.brand}")
            .setText(R.id.tv_zm_kc, "账面库存：${item.bookQuantity}")
            .setText(R.id.tv_zm_dj, "账面单价：${item.bookCost}")
            .setText(R.id.tv_zm_je, "账面金额：${item.bookAmount}")
            .setText(R.id.tv_kc, item.adjustQuantity)
            .setText(R.id.tv_price, item.adjustCost)
            .setText(R.id.tv_money, item.adjustAmount)
            .setText(
                R.id.tv_chang_num,
                "调整数量：${if (BigDecimal(item.addQuantity) > BigDecimal(0)) "+${item.addQuantity}" else item.addQuantity}"
            )
            .setText(
                R.id.tv_chang_amount,
                "调整金额：${if (BigDecimal(item.addAmount) > BigDecimal(0)) "+${item.addAmount}" else item.addAmount}"
            )
    }
}