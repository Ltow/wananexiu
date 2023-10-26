package com.bossed.waej.adapter

import android.view.View
import android.widget.TextView
import com.bossed.waej.R
import com.bossed.waej.javebean.VipCardRow
import com.bossed.waej.javebean.VipManageBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class VipManageAdapter(type: Int, data: ArrayList<VipCardRow>) :
    BaseQuickAdapter<VipCardRow, BaseViewHolder>(
        R.layout.layout_item_member_manage, data
    ) {
    private var type = 0

    init {
        this.type = type
    }

    override fun convert(helper: BaseViewHolder, item: VipCardRow) {
        helper.setText(R.id.tv_name, item.cardName)
            .setText(R.id.tv_price, "售价：${item.marketPrice}元")
            .addOnClickListener(R.id.tv_handle)
            .addOnClickListener(R.id.tv_off)
        val handle = helper.getView<TextView>(R.id.tv_handle)
        val off = helper.getView<TextView>(R.id.tv_off)
        when (type) {
            0 -> {
                helper.setText(R.id.tv_time, "上架日期：${item.enableTime}")
            }
            1 -> {
                handle.visibility = View.GONE
            }
            2 -> {
                helper.setText(R.id.tv_time, "下架日期：${item.disenableTime}")
                off.text = "编辑"
                handle.text = "上架"
            }
        }
    }
}