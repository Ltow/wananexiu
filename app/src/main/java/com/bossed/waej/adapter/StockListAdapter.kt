package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.StockListRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class StockListAdapter(mutableList: MutableList<StockListRow>) :
    BaseQuickAdapter<StockListRow, BaseViewHolder>(R.layout.layout_item_stock_list, mutableList) {
    override fun convert(helper: BaseViewHolder, item: StockListRow) {
        helper.setText(R.id.tv_name, item.part.name)
            .setText(R.id.tv_code, "自编码：${item.part.code}")
            .setText(R.id.tv_model, "规格/型号：${item.part.model}")
            .setText(R.id.tv_brand, "品牌/产地：${item.part.brand}")
            .setText(R.id.tv_kc, "剩余库存：${item.quantity}")
            .setText(R.id.tv_amount, "剩余库存金额：${item.amount}")
    }
}