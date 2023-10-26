package com.bossed.waej.adapter

import android.widget.CheckBox
import com.bossed.waej.R
import com.bossed.waej.javebean.FriendsData
import com.bossed.waej.javebean.SelectFriendsBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SelectFriendsAdapter(data: ArrayList<FriendsData>) :
    BaseQuickAdapter<FriendsData, BaseViewHolder>(
        R.layout.layout_item_friends, data
    ) {
    override fun convert(helper: BaseViewHolder, item: FriendsData) {
        helper.setText(R.id.tv_name_friends, item.name)
            .setText(R.id.tv_contacts, item.contacts + "   " + item.contactsPhone)
            .setText(R.id.tv_address, item.address)
            .setText(R.id.tv_business_scope, item.address)
            .setChecked(R.id.cb_item_friends, item.isSelect)
        val checkBox = helper.getView<CheckBox>(R.id.cb_item_friends)
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            item.isSelect = isChecked
        }
    }
}