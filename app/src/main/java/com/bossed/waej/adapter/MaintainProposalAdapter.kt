package com.bossed.waej.adapter

import android.widget.ImageView
import com.bossed.waej.R
import com.bossed.waej.javebean.RecommendDs1
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MaintainProposalAdapter(data: ArrayList<RecommendDs1>) :
    BaseQuickAdapter<RecommendDs1, BaseViewHolder>(R.layout.layout_item_proposal, data) {
    override fun convert(helper: BaseViewHolder, item: RecommendDs1) {
        helper.setText(R.id.tv_proposal_name, item.保养项目名称)
            .setChecked(R.id.tv_proposal_name, item.isSel)
        helper.getView<ImageView>(R.id.iv_tag).setImageResource(
            when (item.isJYItem) {
                "3" -> R.mipmap.icon_ordinary
                "4" -> R.mipmap.icon_depth
                else -> R.drawable.shape_corners_ffffff_dp3
            }
        )
//        helper.getView<LinearLayout>(R.id.ll_proposal).setBackgroundColor(
//            if (helper.adapterPosition % 2 == 0) Color.parseColor("#FFFFFF") else Color.parseColor("#F5F5F5")
//        )
//        val checkBox = helper.getView<CheckBox>(R.id.cb_proposal)
//        checkBox.setOnClickListener {
//            listener!!.onChange(helper.adapterPosition, checkBox.isChecked)
//        }
    }

//    private var listener: OnSelCheckedListener? = null
//
//    fun setOnSelCheckedListener(listener: OnSelCheckedListener) {
//        this.listener = listener
//    }
}