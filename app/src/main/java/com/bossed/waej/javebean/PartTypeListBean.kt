package com.bossed.waej.javebean

data class PartTypeListBean(
    val code: Int,
    val msg: String,
    val rows: List<PartTypeListRow>,
    val total: Int
)

data class PartTypeListRow(
    val id: Int,
    val imgUrl: String,
    val level: Int,
    val name: String,
    val parentId: Int,
    val remark: String,
    val shopId: Int,
    val sort: Int,
    val status: Int,
    val tenantId: Int
)