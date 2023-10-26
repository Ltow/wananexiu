package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.InformationBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class InformationAdapter(data: MutableList<InformationBean>) :
    BaseQuickAdapter<InformationBean, BaseViewHolder>(
        R.layout.layout_item_information1,
        data
    ) {
    override fun convert(helper: BaseViewHolder, item: InformationBean) {
        helper.setText(R.id.tv_oe, item.oe)
            .setText(R.id.tv_4s_price, "￥${item.sPrice}")
            .setText(R.id.tv_num, item.num + item.unit)
    }
}