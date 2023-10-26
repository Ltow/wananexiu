package com.bossed.waej.javebean

data class SelectItemBean(
    val code: Int,
    val msg: String,
    val rows: List<ItemRow>,
    val total: Int
)

data class ItemRow(
    val cateId: Int,
    var costPrice: Float,
    val createBy: String,
    val createTime: String,
    val id: Int,
    var madeFee: Float,
    val madeMoney: Float,
    var marketPrice: Float,
    val name: String,
    val platName: String,
    val remark: String,
    val serviceFee: Int,
    val shopId: Int,
    val superCateId: Int,
    val tenantId: Int,
    val isHot: Int,
    val updateBy: String,
    val updateTime: String,
    var availableQuantity: Float,
    var isSelect: Boolean
)