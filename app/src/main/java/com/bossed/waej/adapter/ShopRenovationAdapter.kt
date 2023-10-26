package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.ShopRenovationBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ShopRenovationAdapter(mutableList: MutableList<ShopRenovationBean>) :
    BaseQuickAdapter<ShopRenovationBean, BaseViewHolder>(
        R.layout.layout_item_renovation, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: ShopRenovationBean) {
        helper.setText(R.id.tv_title, item.title)
            .setBackgroundRes(
                R.id.tv_title,
                if (item.isSelect) R.drawable.shape_corners_e3e3e3_dp12_5 else R.drawable.shape_corners_ffffff_dp10
            )
            .setText(R.id.tv_status, if (item.isSelect) "使用中" else "使用")
            .setBackgroundRes(
                R.id.tv_status,
                if (item.isSelect) R.drawable.shape_corners_e3e3e3_dp12_5 else R.drawable.shape_corners_ec9142_dp12_5
            )
            .setImageResource(R.id.iv_preview, item.res)
            .addOnClickListener(R.id.tv_status)
            .addOnClickListener(R.id.tv_load)
    }
}