package com.bossed.waej.javebean

data class NoticeListBean(
    val code: Int,
    val msg: String,
    val rows: List<NoticeRow>,
    val total: Int
)

data class NoticeRow(
    val createBy: String,
    val createTime: String,
    val noticeContent: String,
    val noticeId: Int,
    val noticeTitle: String,
    val noticeType: String,
    val remark: String,
    val searchValue: String,
    val status: String,
    val updateBy: String,
    val updateTime: String
)