package com.bossed.waej.adapter

import android.view.View
import android.widget.TextView
import com.bossed.waej.R
import com.bossed.waej.javebean.MemberDetailItemBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MemberDetailItemAdapter(data: MutableList<MemberDetailItemBean>, type: Int) :
    BaseQuickAdapter<MemberDetailItemBean, BaseViewHolder>(
        R.layout.layout_item_member_detail_item,
        data
    ) {
    private var type = -1

    init {
        this.type = type
    }

    override fun convert(helper: BaseViewHolder, item: MemberDetailItemBean) {
        helper.setText(R.id.tv_name, item.name)
        when (type) {
            0 -> helper.getView<TextView>(R.id.tv_use).visibility = View.VISIBLE
            1 -> helper.getView<TextView>(R.id.tv_use).visibility = View.GONE
        }
    }
}