package com.bossed.waej.adapter

import android.widget.ImageView
import com.bossed.waej.R
import com.bossed.waej.javebean.MineMenuBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MineMenuTopAdapter(data: ArrayList<MineMenuBean>) :
    BaseQuickAdapter<MineMenuBean, BaseViewHolder>(R.layout.layoyt_item_mine_top, data) {
    override fun convert(helper: BaseViewHolder, item: MineMenuBean) {
        helper.setText(R.id.tv_mine_top, item.name)
        helper.getView<ImageView>(R.id.iv_mine_top).setImageResource(item.resource)
    }
}