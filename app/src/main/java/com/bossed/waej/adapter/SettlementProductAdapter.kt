package com.bossed.waej.adapter

import android.text.TextUtils
import android.widget.LinearLayout
import com.blankj.utilcode.util.TimeUtils
import com.bossed.waej.R
import com.bossed.waej.javebean.BuyProductData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class SettlementProductAdapter(data: MutableList<BuyProductData>, private val date: String) :
    BaseQuickAdapter<BuyProductData, BaseViewHolder>(R.layout.layout_item_buy_settle, data) {
    override fun convert(helper: BaseViewHolder, item: BuyProductData) {
        helper.setText(R.id.tv_name, item.packageName)
            .setText(
                R.id.tv_priceDay,
                if (TextUtils.isEmpty(date)) "￥${item.priceYear}元/年" else "￥${item.priceDay}元/天"
            )
            .setText(
                R.id.tv_priceYear,
                if (TextUtils.isEmpty(date)) "￥${item.priceYear}" else "￥${
                    BigDecimal((TimeUtils.string2Millis(date) - TimeUtils.getNowMills()) / (1000 * 60 * 60 * 24)).multiply(
                        BigDecimal(item.priceDay)
                    )
                }"
            )
            .setText(R.id.tv_remark, item.remark)
            .setText(R.id.tv_num, "数量x${item.num}")
            .setText(R.id.tv_total, "小计：${BigDecimal(item.num).multiply(BigDecimal(if (TextUtils.isEmpty(date))item.priceYear else BigDecimal((TimeUtils.string2Millis(date) - TimeUtils.getNowMills()) / (1000 * 60 * 60 * 24)).multiply(
                BigDecimal(item.priceDay)
            ).toString()))}")
        helper.getView<LinearLayout>(R.id.ll_content)
            .setBackgroundResource(if (item.isSelect) R.mipmap.icon_product_bg else R.drawable.shape_corners_dp5)
    }
}