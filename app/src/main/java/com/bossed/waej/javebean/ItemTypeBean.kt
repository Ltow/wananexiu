package com.bossed.waej.javebean

data class ItemTypeBean(
    val code: Int,
    val `data`: ItemTypeData,
    val msg: String
)

data class ItemTypeData(
    val createBy: String,
    val createTime: String,
    val id: Int,
    val imgUrl: String,
    val level: Int,
    val name: String,
    val parentId: Int,
    val remark: String,
    val shopId: Int,
    val sort: Int,
    val status: Int,
    val tenantId: Int,
    val updateBy: String,
    val updateTime: String
)