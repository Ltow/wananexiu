package com.bossed.waej.adapter

import android.view.View
import com.bossed.waej.R
import com.bossed.waej.javebean.OESearchDs1
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class OESearchAdapter(data: MutableList<OESearchDs1>) :
    BaseQuickAdapter<OESearchDs1, BaseViewHolder>(R.layout.layout_item_oe, data) {
    override fun convert(helper: BaseViewHolder, item: OESearchDs1) {
        helper.setText(R.id.tv_oe, item.oe)
            .setText(R.id.tv_name, item.原厂零件名称)
            .setText(R.id.tv_name_bz, item.标准零件名称)
            .setText(R.id.tv_brand_name, item.车型品牌名称)
            .setText(R.id.tv_price, item.指导价格)
            .setText(R.id.tv_price_hb, item.华北区市场价)
            .setText(R.id.tv_price_hn, item.华南区市场价)
            .setText(R.id.tv_price_hd, item.华东区市场价)
            .setText(R.id.tv_price_db, item.东北区市场价)
            .setText(R.id.tv_price_xb, item.西部区市场价)
            .setText(R.id.tv_price_sh, item.上海市场价)
            .addOnClickListener(R.id.tv_brand)
            .addOnClickListener(R.id.tv_replace)
        helper.getView<View>(R.id.tv_replace).visibility = if (item.替换号 == "1")
            View.VISIBLE
        else
            View.GONE
        helper.getView<View>(R.id.tv_brand).visibility = if (item.适配车型 == "1")
            View.VISIBLE
        else
            View.GONE
    }
}