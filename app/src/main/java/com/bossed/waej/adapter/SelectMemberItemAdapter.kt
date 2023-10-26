package com.bossed.waej.adapter

import android.graphics.Color
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import com.bossed.waej.R
import com.bossed.waej.javebean.ItemRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SelectMemberItemAdapter(data: ArrayList<ItemRow>) :
    BaseQuickAdapter<ItemRow, BaseViewHolder>(
        R.layout.layout_item_select_member_item, data
    ) {
    override fun convert(helper: BaseViewHolder, item: ItemRow) {
        helper.setText(R.id.tv_item_name, item.name)
            .setChecked(R.id.cb_sel_item, item.isSelect)
        helper.getView<CheckBox>(R.id.cb_sel_item)
            .setOnCheckedChangeListener { buttonView, isChecked ->
                item.isSelect = isChecked
            }
        helper.getView<EditText>(R.id.et_ky_cs).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!TextUtils.isEmpty(s))
                    item.availableQuantity = s.toString().toFloat()
            }
        })
        val linearLayout = helper.getView<LinearLayout>(R.id.ll_select_member_item)
        linearLayout.setBackgroundColor(
            if (helper.layoutPosition % 2 == 0) Color.parseColor("#FFFFFF") else Color.parseColor(
                "#F5F5F5"
            )
        )
    }
}