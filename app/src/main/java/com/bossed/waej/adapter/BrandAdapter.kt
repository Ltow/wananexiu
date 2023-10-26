package com.bossed.waej.adapter

import android.view.View
import com.bossed.waej.R
import com.bossed.waej.javebean.BrandDs1
import com.bossed.waej.util.GlideUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class BrandAdapter(data: MutableList<BrandDs1>) :
    BaseQuickAdapter<BrandDs1, BaseViewHolder>(R.layout.layout_item_all_brand, data) {
    override fun convert(helper: BaseViewHolder, item: BrandDs1) {
        helper.setText(R.id.name, item.brandName)
        GlideUtils.get().loadCarLogo(mContext, item.brandIcon, helper.getView(R.id.iv_car_brand))
        helper.getView<View>(R.id.include).visibility = View.GONE
    }
}