package com.bossed.waej.adapter

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import com.bossed.waej.R
import com.bossed.waej.javebean.ItemRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MemberItemAdapter(data: ArrayList<ItemRow>) :
    BaseQuickAdapter<ItemRow, BaseViewHolder>(
        R.layout.layout_item_member_type, data
    ) {
    override fun convert(helper: BaseViewHolder, item: ItemRow) {
        helper.setText(R.id.tv_item_name, item.name)
            .setText(R.id.et_price_item, item.availableQuantity.toString())
            .setText(R.id.et_costPrice, item.costPrice.toString())
            .setText(R.id.et_madeFee, item.madeFee.toString())
            .addOnClickListener(R.id.iv_delete_item)
        helper.getView<EditText>(R.id.et_price_item).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!TextUtils.isEmpty(s))
                    item.availableQuantity = s.toString().toFloat()
            }
        })
        helper.getView<EditText>(R.id.et_costPrice).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!TextUtils.isEmpty(s))
                    item.costPrice = s.toString().toFloat()
            }
        })
        helper.getView<EditText>(R.id.et_madeFee).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!TextUtils.isEmpty(s))
                    item.madeFee = s.toString().toFloat()
            }
        })
    }
}