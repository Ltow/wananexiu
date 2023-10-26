package com.bossed.waej.adapter

import android.graphics.Color
import com.bossed.waej.R
import com.bossed.waej.javebean.GoodsBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ZYTypeAdapter(mutableList: MutableList<GoodsBean>, private val type: Int) :
    BaseQuickAdapter<GoodsBean, BaseViewHolder>(R.layout.layout_item_goods_preview, mutableList) {
    override fun convert(helper: BaseViewHolder, item: GoodsBean) {
        when (type) {
            3 -> {
                helper.setText(R.id.tv_name, item.name)
                    .setBackgroundColor(
                        R.id.rl_content,
                        if (item.isSelect) Color.parseColor("#ffffff") else Color.parseColor("#ECF6FF")
                    )
                    .setVisible(R.id.v_tag, item.isSelect)
                    .setTextColor(
                        R.id.tv_name,
                        if (item.isSelect) Color.parseColor("#000000") else Color.parseColor("#999999")
                    )
                    .setVisible(R.id.tv_hui, item.name == "美容")
            }

            4 -> {
                helper.setText(R.id.tv_name, item.name)
                    .setBackgroundColor(
                        R.id.rl_content,
                        if (item.isSelect) Color.parseColor("#E7F3FF") else Color.parseColor("#f6f6f6")
                    )
                    .setVisible(R.id.v_tag, false)
                    .setTextColor(
                        R.id.tv_name,
                        if (item.isSelect) Color.parseColor("#198CFF") else Color.parseColor("#999999")
                    )
                    .setVisible(R.id.tv_hui, item.name == "美容")
            }

            else -> {
                helper.setText(R.id.tv_name, item.name)
                    .setBackgroundColor(
                        R.id.rl_content,
                        if (item.isSelect) Color.parseColor("#ffffff") else Color.parseColor("#f6f6f6")
                    )
                    .setVisible(R.id.v_tag, item.isSelect)
                    .setTextColor(
                        R.id.tv_name,
                        if (item.isSelect) Color.parseColor("#000000") else Color.parseColor("#999999")
                    )
                    .setVisible(R.id.tv_hui, item.name == "美容")
            }
        }
    }
}