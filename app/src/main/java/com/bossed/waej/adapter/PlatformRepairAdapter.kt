package com.bossed.waej.adapter

import android.view.View
import android.widget.TextView
import com.bossed.waej.R
import com.bossed.waej.javebean.RepairGood
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class PlatformRepairAdapter(layoutId: Int, list: ArrayList<RepairGood>) :
    BaseQuickAdapter<RepairGood, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder, item: RepairGood) {
        helper.setText(R.id.tv_number_goods, helper.adapterPosition.toString())
            .setText(R.id.tv_cate_goods, item.cateName)
            .setText(R.id.tv_name_repair_detail_goods, item.specName)
            .setText(R.id.tv_goodsNum, item.goodsNum.toString())
        helper.getView<TextView>(R.id.tv_money_repair).visibility = View.GONE
    }
}