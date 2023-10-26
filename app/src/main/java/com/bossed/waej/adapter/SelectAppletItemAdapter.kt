package com.bossed.waej.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import com.bossed.waej.R
import com.bossed.waej.javebean.ItemRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SelectAppletItemAdapter(type: Int, data: ArrayList<ItemRow>) :
    BaseQuickAdapter<ItemRow, BaseViewHolder>(R.layout.layout_item_sel_applet_item, data) {
    private var type = 0;

    init {
        this.type = type
    }

    override fun convert(helper: BaseViewHolder, item: ItemRow) {
        helper.setText(R.id.tv_item_name, item.name)
            .setText(R.id.et_price_item, item.marketPrice.toString())
            .setChecked(R.id.cb_sel_item, item.isSelect)
            .addOnClickListener(R.id.tv_item_msg)
        helper.getView<EditText>(R.id.et_price_item).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                item.marketPrice = s.toString().toFloat()
            }
        })
        helper.getView<CheckBox>(R.id.cb_sel_item)
            .setOnCheckedChangeListener { buttonView, isChecked ->
                item.isSelect = isChecked
            }
        when (type) {
            0 -> {
                helper.getView<CheckBox>(R.id.cb_sel_item).visibility = View.VISIBLE
            }
            1 -> {
                helper.getView<CheckBox>(R.id.cb_sel_item).visibility = View.GONE
            }
        }
    }
}