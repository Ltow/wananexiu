package com.bossed.waej.adapter

import android.widget.LinearLayout
import com.bossed.waej.R
import com.bossed.waej.javebean.PageMenu2Bean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class PageMenu2Adapter(data: ArrayList<PageMenu2Bean>) :
    BaseQuickAdapter<PageMenu2Bean, BaseViewHolder>(
        R.layout.layout_item_menu2, data
    ) {
    override fun convert(helper: BaseViewHolder, item: PageMenu2Bean) {
        helper.setImageResource(R.id.iv_page_item, item.imgLocalId)
            .setText(R.id.tv_menu_name, item.menuName)
            .setText(R.id.tv_menu_msg, item.msg)
        val ll_item_page2 = helper.getView<LinearLayout>(R.id.ll_item_page2)
        ll_item_page2.setBackgroundResource(
            when (helper.layoutPosition) {
                0 -> R.drawable.shape_corners_menu1
                1 -> R.drawable.shape_corners_menu2
                2 -> R.drawable.shape_corners_menu3
                3 -> R.drawable.shape_corners_menu4
                4 -> R.drawable.shape_corners_menu5
                5 -> R.drawable.shape_corners_menu6
                6 -> R.drawable.shape_corners_menu7
                7 -> R.drawable.shape_corners_menu8
                8 -> R.drawable.shape_corners_menu9
                9 -> R.drawable.shape_corners_menu10
                10 -> R.drawable.shape_corners_menu11
                11 -> R.drawable.shape_corners_menu12
                else -> R.drawable.shape_corners_menu12
            }
        )
    }
}