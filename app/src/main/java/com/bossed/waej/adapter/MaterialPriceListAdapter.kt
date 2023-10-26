package com.bossed.waej.adapter

import android.widget.TextView
import com.bossed.waej.R
import com.bossed.waej.javebean.OrderRow
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MaterialPriceListAdapter(data: ArrayList<OrderRow>, type: Int) :
    BaseQuickAdapter<OrderRow, BaseViewHolder>(
        R.layout.layout_item_material_list,
        data
    ) {
    private var type = 0

    init {
        this.type = type
    }

    override fun convert(helper: BaseViewHolder, item: OrderRow) {
        helper.setText(R.id.tv_che_no, item.cardNo)
            .setText(R.id.tv_order_no, item.orderSn)
            .setText(R.id.tv_jdr_item, "接待人：${item.receiveBy}")
            .setText(R.id.tv_jd_time, "接待时间：${item.receiveTime}")
            .setText(R.id.tv_price_material, "￥${item.orderMoney}")
            .addOnClickListener(R.id.tv_go_price)
        helper.getView<TextView>(R.id.tv_go_price).text = if (type == 1) "去派工" else "去划价"
        Glide.with(mContext).load(item.brandLogo).into(helper.getView(R.id.iv_car_logo))
    }
}