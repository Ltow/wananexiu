package com.bossed.waej.ui.amap

import com.amap.api.services.help.Tip
import com.bossed.waej.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MapSearchAdapter(data: ArrayList<Tip>) :
    BaseQuickAdapter<Tip, BaseViewHolder>(R.layout.item_map_search, data) {
    override fun convert(helper: BaseViewHolder, item: Tip) {
        helper.setText(R.id.name, item.name)
            .setText(R.id.address, item.address)
            .setText(R.id.distance,item.district)
    }
}