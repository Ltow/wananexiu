package com.bossed.waej.adapter

import android.view.View
import android.widget.RatingBar
import com.bossed.waej.R
import com.bossed.waej.javebean.EvaluationRow
import com.bossed.waej.util.GlideUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MyEvaluationAdapter(mutableList: MutableList<EvaluationRow>) :
    BaseQuickAdapter<EvaluationRow, BaseViewHolder>(R.layout.layout_item_evaluation, mutableList) {
    override fun convert(helper: BaseViewHolder, item: EvaluationRow) {
        helper.setText(R.id.tv_packageName, item.packageName)
            .setText(R.id.tv_nickName, item.nickName)
            .setText(R.id.tv_time, item.createTime)
            .setText(R.id.tv_evaluation, item.content)
            .setText(R.id.tv_reply, if (item.replyList.isEmpty()) "" else item.replyList[0].content)
            .addOnClickListener(R.id.iv_reply)
        helper.getView<RatingBar>(R.id.rating_bar).rating = item.userSocre!!.toFloat()
        helper.getView<View>(R.id.iv_reply).visibility =
            if (item.replyList.isEmpty()) View.VISIBLE else View.GONE
        helper.getView<View>(R.id.ll_reply).visibility =
            if (item.replyList.isEmpty()) View.GONE else View.VISIBLE
        GlideUtils.get().loadHead(mContext, item.userImage!!, helper.getView(R.id.iv_head))
    }
}