package com.bossed.waej.adapter

import android.view.View
import com.bossed.waej.R
import com.bossed.waej.javebean.Inquiry
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class InquiryItemAdapter(data: ArrayList<Inquiry>) :
    BaseQuickAdapter<Inquiry, BaseViewHolder>(R.layout.layout_item_inquiry_item, data) {
    override fun convert(helper: BaseViewHolder, item: Inquiry) {
        helper.setText(R.id.tv_gys_name, item.itemName)
            .setText(R.id.tv_item_bz, "备注：${item.remark}")
            .setText(R.id.tv_price_inquiry, "￥${item.offerMoney}")
        when (item.orderStatus) {
            0 -> {
                helper.getView<View>(R.id.tv_price_inquiry).visibility = View.GONE
                helper.getView<View>(R.id.tv_bj).visibility = View.GONE
                helper.getView<View>(R.id.tv_offer_status).visibility = View.VISIBLE
            }
            1 -> {
                helper.getView<View>(R.id.tv_price_inquiry).visibility = View.VISIBLE
                helper.getView<View>(R.id.tv_bj).visibility = View.VISIBLE
                helper.getView<View>(R.id.tv_offer_status).visibility = View.GONE
            }
        }
    }
}