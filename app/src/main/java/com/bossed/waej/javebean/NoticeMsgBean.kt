package com.bossed.waej.javebean

data class NoticeMsgBean(
    val code: Int,
    val `data`: NoticeMsgData,
    val msg: String
)

data class NoticeMsgData(
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
