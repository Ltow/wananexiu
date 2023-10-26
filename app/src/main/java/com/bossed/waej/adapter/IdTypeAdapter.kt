package com.bossed.waej.adapter

import android.graphics.Color
import com.bossed.waej.R
import com.bossed.waej.javebean.IdTypeBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class IdTypeAdapter(mutableList: MutableList<IdTypeBean>, private var selName: String) :
    BaseQuickAdapter<IdTypeBean, BaseViewHolder>(
        R.layout.layout_item_weelview, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: IdTypeBean) {
        helper.setText(R.id.tv_item_weelview, item.name)
            .setTextColor(
                R.id.tv_item_weelview,
                Color.parseColor(if (item.name == selName) "#3477fc" else "#333333")
            )
    }
}