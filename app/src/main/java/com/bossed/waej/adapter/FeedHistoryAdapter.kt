package com.bossed.waej.adapter

import android.graphics.Color
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat
import com.bossed.waej.R
import com.bossed.waej.javebean.FeedHistoryBean
import com.bossed.waej.javebean.FeedHistoryRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class FeedHistoryAdapter(data: MutableList<FeedHistoryRow>) :
    BaseQuickAdapter<FeedHistoryRow, BaseViewHolder>(
        R.layout.layout_item_feed_history, data
    ) {
    override fun convert(helper: BaseViewHolder, item: FeedHistoryRow) {
        helper.setText(R.id.tv_name, item.typeName)
            .setText(R.id.tv_feedTime, item.feedTime)
        when (item.typeName) {
            "功能异常" -> mContext.getDrawable(R.drawable.shape_feed_item_status_bg)?.let {
                DrawableCompat.setTint(it, Color.parseColor("#32C5C2"))
                helper.getView<ImageView>(R.id.iv_status).setImageDrawable(it)
            }
            "体验问题" -> mContext.getDrawable(R.drawable.shape_feed_item_status_bg)?.let {
                DrawableCompat.setTint(it, Color.parseColor("#FAAB4A"))
                helper.getView<ImageView>(R.id.iv_status).setImageDrawable(it)
            }
            "交易问题" -> mContext.getDrawable(R.drawable.shape_feed_item_status_bg)?.let {
                DrawableCompat.setTint(it, Color.parseColor("#69B4FE"))
                helper.getView<ImageView>(R.id.iv_status).setImageDrawable(it)
            }
            "订单问题" -> mContext.getDrawable(R.drawable.shape_feed_item_status_bg)?.let {
                DrawableCompat.setTint(it, Color.parseColor("#FF6D74"))
                helper.getView<ImageView>(R.id.iv_status).setImageDrawable(it)
            }

        }
    }
}