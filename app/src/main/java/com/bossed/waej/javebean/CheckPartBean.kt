package com.bossed.waej.javebean

data class CheckPartBean(
    val code: Int,
    val `data`: List<CheckPartData>,
    val msg: String
)

data class CheckPartData(
    val briefIntroduction: String,
    val cateId: Int,
    val costPrice: Double,
    val details: String,
    val id: Int,
    val madeFee: Int,
    val madeMoney: Int,
    val mainPicture: String,
    val marketPrice: Float,
    val name: String,
    val oem: String,
    val platName: String,
    val remark: String,
    val serviceFee: Float,
    val shopId: Int,
    val specName: String,
    val status: Int,
    val superCateId: Int,
    val tenantId: Int,
    val unitName: String,
    val num: String,
    val virtualPrice: Int
)