package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.NoticeRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class NoticeListAdapter(data: ArrayList<NoticeRow>) :
    BaseQuickAdapter<NoticeRow, BaseViewHolder>(R.layout.layout_item_notice_list, data) {
    override fun convert(helper: BaseViewHolder, item: NoticeRow) {
        helper.setText(R.id.tv_notice_title, item.noticeTitle)
            .setText(R.id.tv_notice_content, item.noticeContent)
            .setText(R.id.tv_notice_date, item.createTime)
    }
}