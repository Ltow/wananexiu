package com.bossed.waej.javebean

data class CheckItemBean(
    val code: Int,
    val `data`: List<CheckItemData>,
    val msg: String
)

data class CheckItemData(
    val briefIntroduction: String,
    val cateId: Int,
    val costPrice: Int,
    val details: String,
    val id: Int,
    val isHot: Int,
    val madeFee: Int,
    val madeMoney: Int,
    val mainPicture: String,
    val marketPrice: Int,
    val name: String,
    val platName: String,
    val remark: String,
    val serviceFee: Int,
    val shopId: Int,
    val status: Int,
    val superCateId: Int,
    val tenantId: Int,
    val virtualPrice: Int
)