package com.bossed.waej.javebean

data class ItemMsgBean(
    val code: Int,
    val `data`: ItemMsgData,
    val msg: String
)

data class ItemMsgData(
    val briefIntroduction: String,
    val cateId: Int,
    val costPrice: Float,
    val details: String,
    val id: Int,
    val madeFee: Float,
    val madeMoney: Float,
    val mainPicture: String,
    val marketPrice: Float,
    val name: String,
    val platName: String,
    val remark: String,
    val serviceFee: Int,
    val shopId: Int,
    val status: Int,
    val superCateId: Int,
    val tenantId: Int,
    val virtualPrice: Float
)