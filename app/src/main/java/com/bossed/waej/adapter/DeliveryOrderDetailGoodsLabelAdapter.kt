package com.bossed.waej.adapter

import android.graphics.Color
import android.widget.TextView
import com.bossed.waej.R
import com.bossed.waej.javebean.LabelData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class DeliveryOrderDetailGoodsLabelAdapter(
    layoutId: Int,
    list: MutableList<LabelData>
) : BaseQuickAdapter<LabelData, BaseViewHolder>(layoutId, list) {
    override fun convert(holder: BaseViewHolder, item: LabelData) {
        holder.setText(R.id.tv_name_delivery_order_detail_goods_label, item.cateTitle)
            .addOnClickListener(R.id.tv_name_delivery_order_detail_goods_label)
        val textView = holder.getView<TextView>(R.id.tv_name_delivery_order_detail_goods_label)
        if (item.isSelected) {
            textView.setBackgroundResource(R.drawable.shape_corners_dp2)
            textView.setTextColor(Color.parseColor("#ffffff"))
        } else {
            textView.setBackgroundResource(R.drawable.shape_stroke_999999_corners_dp2)
            textView.setTextColor(Color.parseColor("#666666"))
        }
    }
}