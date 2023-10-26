package com.bossed.waej.javebean

data class SelectItemTypeBean(
    val code: Int,
    val `data`: List<TypeData>,
    val msg: String
)

data class TypeData(
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
    val updateTime: String,
    var isSelect: Boolean = false
)