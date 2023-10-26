package com.bossed.waej.adapter

import android.widget.ImageView
import com.bossed.waej.R
import com.bossed.waej.javebean.AppletUpholdBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AppletUpholdAdapter(data: ArrayList<AppletUpholdBean>) :
    BaseQuickAdapter<AppletUpholdBean, BaseViewHolder>(R.layout.layout_item_applet, data) {
    override fun convert(helper: BaseViewHolder, item: AppletUpholdBean) {
        helper.setText(R.id.tv_applet_item, item.name)
        helper.getView<ImageView>(R.id.iv_applet_item).setImageResource(item.resource)
    }
}