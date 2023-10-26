package com.bossed.waej.adapter

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.bossed.waej.R
import com.bossed.waej.javebean.LabelData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class RepairDetailLabelAdapter(layoutId: Int, list: MutableList<LabelData>) :
    BaseQuickAdapter<LabelData, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: LabelData?) {
        helper?.setText(R.id.tv_name_repair_detail_label, item?.cateTitle)
            ?.addOnClickListener(R.id.rl_repair_detail_label_item)
        val view = helper?.getView<View>(R.id.v_name_repair_detail_label)
        val textView = helper?.getView<TextView>(R.id.tv_name_repair_detail_label)
        if (item?.isSelected == true) {
            textView?.setTextColor(Color.parseColor("#224BBE"))
            textView?.textSize = 14f
            view?.visibility = View.VISIBLE
        } else {
            textView?.setTextColor(Color.parseColor("#666666"))
            textView?.textSize = 12f
            view?.visibility = View.GONE
        }
    }
}