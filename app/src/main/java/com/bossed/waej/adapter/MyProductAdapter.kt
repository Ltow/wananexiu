package com.bossed.waej.adapter

import android.view.View
import android.widget.LinearLayout
import com.bossed.waej.R
import com.bossed.waej.javebean.MyProductData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MyProductAdapter(data: MutableList<MyProductData>) :
    BaseQuickAdapter<MyProductData, BaseViewHolder>(R.layout.layout_item_buy_settle, data) {
    override fun convert(helper: BaseViewHolder, item: MyProductData) {
        helper.setText(R.id.tv_name, item.packageName)
            .setText(R.id.tv_remark, item.remark)
        helper.getView<LinearLayout>(R.id.ll_content)
            .setBackgroundResource(R.drawable.shape_product_item_bg_blue)
        helper.getView<View>(R.id.tv_priceYear).visibility = View.GONE
        helper.getView<View>(R.id.tv_priceDay).visibility = View.GONE
        helper.getView<View>(R.id.tv_num).visibility = View.GONE
        helper.getView<View>(R.id.tv_total).visibility = View.GONE
    }
}