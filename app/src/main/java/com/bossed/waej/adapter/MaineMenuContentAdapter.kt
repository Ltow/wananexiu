package com.bossed.waej.adapter

import android.widget.ImageView
import com.bossed.waej.R
import com.bossed.waej.javebean.MineMenuBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MaineMenuContentAdapter(data: ArrayList<MineMenuBean>) :
    BaseQuickAdapter<MineMenuBean, BaseViewHolder>(
        R.layout.layout_item_mine_menu_content, data
    ) {
    override fun convert(helper: BaseViewHolder, item: MineMenuBean) {
        helper.setText(R.id.tv_mine_content, item.name)
        helper.getView<ImageView>(R.id.iv_mine_content).setImageResource(item.resource)
    }
}