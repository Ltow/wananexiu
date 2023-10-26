package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.PartListRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class PurchaseHistoryInfoAdapter2(mutableList: MutableList<PartListRow>) :
    BaseQuickAdapter<PartListRow, BaseViewHolder>(
        R.layout.layout_item_purchase_history_part, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: PartListRow) {
        helper.setText(R.id.tv_name, item.name)
            .setText(R.id.tv_oe, "OE号：${item.oe}")
            .setText(R.id.tv_num, "x${item.num}")
            .setText(R.id.tv_price, "￥${item.purchasePrice}")
            .setText(
                R.id.tv_money,
                "￥${
                    BigDecimal(item.num!!).multiply(BigDecimal(item.purchasePrice))
                        .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                }"
            )
            .setText(R.id.tv_content, "自编码：${item.code}  规格/型号：${item.model}  品牌/产地：${item.brand}")
            .setText(R.id.tv_num_name, "退货数量：")
            .setText(R.id.tv_price_name, "退货单价：")
            .setText(R.id.tv_money_name, "退货金额：")
    }
}