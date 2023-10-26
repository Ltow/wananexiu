package com.bossed.waej.adapter

import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.bossed.waej.R
import com.bossed.waej.javebean.Item
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class InquiryAdapter(data: ArrayList<Item>) :
    BaseQuickAdapter<Item, BaseViewHolder>(R.layout.layout_item_inquiry, data) {
    private lateinit var inquiryItemAdapter: InquiryItemAdapter

    override fun convert(helper: BaseViewHolder, item: Item) {
        helper.setText(R.id.tv_peij_name, item.itemName)
            .setText(R.id.tv_oe_inquiry, "OE号：${item.oem}")
            .setChecked(R.id.cb_item_inquiry, item.isSelect)
        val checkBox = helper.getView<CheckBox>(R.id.cb_item_inquiry)
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            item.isSelect = isChecked
        }
        val recyclerView = helper.getView<RecyclerView>(R.id.rv_inquiry_item)
        if (item.inquiryList != null) {
            inquiryItemAdapter = InquiryItemAdapter(item.inquiryList as ArrayList)
            inquiryItemAdapter.bindToRecyclerView(recyclerView)
        }
    }
}