package com.bossed.waej.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossed.waej.R
import com.bossed.waej.javebean.ItemBean
import com.bossed.waej.javebean.SupplyPriceBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class PricingItemAdapter(data: MutableList<ItemBean>, private val type: Int) :
    BaseQuickAdapter<ItemBean, BaseViewHolder>(R.layout.layout_item_pricing_item, data) {
    override fun convert(helper: BaseViewHolder, item: ItemBean) {
        helper.setText(R.id.tv_name, item.itemName)
            .setText(R.id.tv_pricing, if (type == 1) "完善进货" else "查看")
            .addOnClickListener(R.id.tv_pricing)
        val recyclerView = helper.getView<RecyclerView>(R.id.rv_gys_list)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        val supplierAdapter = SupplyPriceAdapter(item.supplyPriceList)
        supplierAdapter.bindToRecyclerView(recyclerView)
        helper.getView<View>(R.id.ll_total).visibility =
            if (item.supplyPriceList.isEmpty()) View.GONE else View.VISIBLE
        var total = BigDecimal(0.0)
        for (supp: SupplyPriceBean in item.supplyPriceList) {
            total = total.add(BigDecimal(supp.unitPrice!!).multiply(BigDecimal(supp.num!!)))
        }
        helper.getView<TextView>(R.id.tv_total_money).text =
            "￥${total.setScale(2, BigDecimal.ROUND_HALF_DOWN)}"

    }
}