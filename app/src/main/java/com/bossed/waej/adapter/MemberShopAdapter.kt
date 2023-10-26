package com.bossed.waej.adapter

import android.widget.CheckBox
import com.bossed.waej.R
import com.bossed.waej.javebean.MemberShopBean
import com.bossed.waej.javebean.ShopRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MemberShopAdapter(data: ArrayList<ShopRow>) :
    BaseQuickAdapter<ShopRow, BaseViewHolder>(R.layout.layout_item_use_shop, data) {
    override fun convert(helper: BaseViewHolder, item: ShopRow) {
        helper.setText(R.id.cb_use_shop, item.fullname)
            .setChecked(R.id.cb_use_shop, item.isSelect)
        helper.getView<CheckBox>(R.id.cb_use_shop)
            .setOnCheckedChangeListener { buttonView, isChecked ->
                item.isSelect = isChecked
            }
    }
}