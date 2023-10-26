package com.bossed.waej.adapter

import android.text.TextUtils
import com.bossed.waej.R
import com.bossed.waej.javebean.Product
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class HistoryProductAdapter(data: MutableList<Product>) :
    BaseQuickAdapter<Product, BaseViewHolder>(
        R.layout.layout_item_history_product,
        data
    ) {
    override fun convert(helper: BaseViewHolder, item: Product) {
        helper.setText(R.id.tv_name, item.packageName)
            .setText(R.id.tv_remark, item.remark)
            .setText(R.id.tv_priceYear, "￥${item.orderMoney}")
            .setText(R.id.tv_num, if (TextUtils.isEmpty(item.siteNum)) "" else "数量x${item.siteNum}")
    }
}