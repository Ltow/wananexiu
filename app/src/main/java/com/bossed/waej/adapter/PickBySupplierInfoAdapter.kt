package com.bossed.waej.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossed.waej.R
import com.bossed.waej.javebean.DetailX
import com.bossed.waej.javebean.PickBySupplierInfoDetail
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class PickBySupplierInfoAdapter(mutableList: MutableList<PickBySupplierInfoDetail>) :
    BaseQuickAdapter<PickBySupplierInfoDetail, BaseViewHolder>(
        R.layout.layout_item_pricing_item,
        mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: PickBySupplierInfoDetail) {
        helper.setVisible(R.id.tv_pricing, false)
            .setText(R.id.tv_name, item.itemName)
        if (item.detailList == null)
            return
        var total = BigDecimal(0.0)
        for (detail: DetailX in item.detailList!!) {
            total += BigDecimal(detail.amount).setScale(2, BigDecimal.ROUND_HALF_DOWN)
        }
        helper.setText(R.id.tv_total_money, "￥$total")
        val recyclerView = helper.getView<RecyclerView>(R.id.rv_gys_list)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        val supplierAdapter = SupplyPriceBySupplierAdapter(item.detailList as ArrayList)
        supplierAdapter.bindToRecyclerView(recyclerView)
    }
}