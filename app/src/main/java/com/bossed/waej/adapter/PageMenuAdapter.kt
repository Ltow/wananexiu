package com.bossed.waej.adapter

import android.view.View
import android.widget.ImageView
import com.bossed.waej.R
import com.bossed.waej.javebean.PageMenuBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class PageMenuAdapter(data: ArrayList<PageMenuBean>, private val type: Int) :
    BaseQuickAdapter<PageMenuBean, BaseViewHolder>(
        if (type == 1) R.layout.layout_item_menu1 else R.layout.layout_item_menu3, data
    ) {
    override fun convert(helper: BaseViewHolder, item: PageMenuBean) {
        helper.setText(R.id.tv_menu1, item.menu)
            .setText(R.id.tv_subscript, item.subscript)
        val imageView = helper.getView<ImageView>(R.id.iv_menu1)
        imageView.setImageResource(item.resource)
        helper.getView<View>(R.id.tv_subscript).visibility =
            if (item.subscript == "0" || !item.isBuy) View.GONE else View.VISIBLE
        helper.getView<View>(R.id.iv_vip).visibility = if (item.isBuy) View.GONE else View.VISIBLE
    }
}