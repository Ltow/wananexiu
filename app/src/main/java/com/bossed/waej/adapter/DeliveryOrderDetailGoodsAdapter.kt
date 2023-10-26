package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.OrderGood
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class DeliveryOrderDetailGoodsAdapter(
    layoutId: Int,
    list: MutableList<OrderGood>
) :
    BaseQuickAdapter<OrderGood, BaseViewHolder>(
        layoutId,
        list
    ) {

    override fun convert(helper: BaseViewHolder?, item: OrderGood?) {
        helper?.setText(R.id.tv_goods_name, item?.specName)
            ?.setText(R.id.tv_gg_name, item?.specName)
            ?.setText(R.id.tv_need_sum, item?.goodsNum.toString())
            ?.setText(R.id.tv_money_detail, item?.money.toString())
            ?.setText(R.id.tv_lb_name, item?.cateName)
    }
}