package com.bossed.waej.javebean

data class PartTypeBean(
    val code: Int,
    val `data`: PartTypeData,
    val msg: String
)

data class PartTypeData(
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