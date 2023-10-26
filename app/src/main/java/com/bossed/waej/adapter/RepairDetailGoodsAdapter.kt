package com.bossed.waej.adapter

import android.graphics.Color
import android.widget.LinearLayout
import com.bossed.waej.R
import com.bossed.waej.javebean.OrderGood
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class RepairDetailGoodsAdapter(layoutId: Int, list: MutableList<OrderGood>) :
    BaseQuickAdapter<OrderGood, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: OrderGood?) {
        helper?.setText(R.id.tv_name_repair_detail_goods, item?.specName)
            ?.setText(R.id.tv_goodsNum, item?.goodsNum.toString())
            ?.setText(R.id.tv_number_goods, item?.id.toString())
            ?.setText(R.id.tv_cate_goods, item?.cateName)
            ?.setText(R.id.tv_money_repair, item?.money.toString())
        val linearLayout = helper?.getView<LinearLayout>(R.id.ll_repair_detail_goods_item)
        if (helper?.adapterPosition!! % 2 == 0)
            linearLayout?.setBackgroundColor(Color.parseColor("#ffffff"))
        else
            linearLayout?.setBackgroundColor(Color.parseColor("#F6F6F6"))
    }
}