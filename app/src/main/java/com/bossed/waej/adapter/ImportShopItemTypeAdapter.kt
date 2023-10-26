package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.ImportShopItemTypeBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ImportShopItemTypeAdapter(mutableList: MutableList<ImportShopItemTypeBean>) :
    BaseQuickAdapter<ImportShopItemTypeBean, BaseViewHolder>(
        R.layout.layout_item_import_item_type, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: ImportShopItemTypeBean) {
        helper.setText(R.id.tv_type, item.name)
            .setBackgroundRes(
                R.id.tv_type,
                if (item.isSelect) R.drawable.shape_corners_3477fc_dp_12_5 else R.drawable.shape_corners_cccccc_dp12_5
            )
    }
}