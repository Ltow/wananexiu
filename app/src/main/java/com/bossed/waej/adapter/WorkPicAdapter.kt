package com.bossed.waej.adapter

import android.view.View
import com.bossed.waej.R
import com.bossed.waej.javebean.WorkPicRow
import com.bossed.waej.util.GlideUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class WorkPicAdapter(data: MutableList<WorkPicRow>, private val type: String) :
    BaseQuickAdapter<WorkPicRow, BaseViewHolder>(R.layout.layout_item_work_pic, data) {

    override fun convert(helper: BaseViewHolder, item: WorkPicRow) {
        GlideUtils.get().loadShopPic(mContext, item.img, helper.getView(R.id.iv_pic))
        helper.addOnClickListener(R.id.iv_delete_pic)
            .addOnClickListener(R.id.iv_pic)
        helper.getView<View>(R.id.iv_delete_pic).visibility =
            if (type == "history") View.GONE else View.VISIBLE
    }
}