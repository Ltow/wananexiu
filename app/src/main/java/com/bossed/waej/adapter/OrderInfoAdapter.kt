package com.bossed.waej.adapter

import android.text.TextUtils
import com.bossed.waej.R
import com.bossed.waej.javebean.Product
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class OrderInfoAdapter(data: MutableList<Product>) :
    BaseQuickAdapter<Product, BaseViewHolder>(R.layout.layout_item_order_info, data) {
    override fun convert(helper: BaseViewHolder, item: Product) {
        helper.setText(R.id.tv_name, item.packageName)
            .setText(R.id.tv_remark, item.remark)
            .setText(R.id.tv_num, if (TextUtils.isEmpty(item.siteNum)) "" else "数量x${item.siteNum}")
            .setText(R.id.tv_total, "￥${item.orderMoney}")
            .setText(
                R.id.tv_priceYear,
                "￥${
                    BigDecimal(item.orderMoney).divide(
                        BigDecimal(
                            if (TextUtils.isEmpty(item.siteNum)) "1" else item.siteNum
                        ),
                        2,
                        BigDecimal.ROUND_HALF_DOWN
                    )
                }"
            )
    }
}