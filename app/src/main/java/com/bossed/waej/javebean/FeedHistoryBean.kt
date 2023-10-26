package com.bossed.waej.javebean

data class FeedHistoryBean(
    val code: Int,
    val msg: String,
    val rows: List<FeedHistoryRow>,
    val total: Int
)

data class FeedHistoryRow(
    val backContent: String,
    val backImg: String,
    val backTime: String,
    val feedTime: String,
    val id: Int,
    val img: String,
    val problem: String,
    val remark: String,
    val status: Int,
    val type: Int,
    val typeName: String,
    val userId: Int
)