package com.bossed.waej.adapter

import android.widget.LinearLayout
import com.bossed.waej.R
import com.bossed.waej.javebean.BuyProductData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class BuyProductAdapter(data: MutableList<BuyProductData>) :
    BaseQuickAdapter<BuyProductData, BaseViewHolder>(R.layout.layout_item_buy_product, data) {
    override fun convert(helper: BaseViewHolder, item: BuyProductData) {
        helper.setText(R.id.tv_name, item.packageName)
            .setText(R.id.tv_priceDay, "￥${item.priceDay}元/天")
            .setText(R.id.tv_priceYear, "￥${item.priceYear}元/年")
            .setText(R.id.tv_remark, item.remark)
            .setChecked(R.id.ctv_sel, item.isSelect)
        helper.getView<LinearLayout>(R.id.ll_content)
            .setBackgroundResource(if (item.isSelect) R.drawable.shape_product_item_bg else R.drawable.shape_corners_dp5)
    }
}