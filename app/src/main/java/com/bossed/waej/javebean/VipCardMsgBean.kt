package com.bossed.waej.javebean

data class VipCardMsgBean(
    val code: Int,
    val `data`: VipCardMsgData,
    val msg: String
)

data class VipCardMsgData(
    val cardName: String,
    val cardNo: String,
    val costPrice: Int,
    val giveAmount: Int,
    val id: Int,
    val kmShopClubCardItemsList: List<KmShopClubCardItems>,
    val marketPrice: Int,
    val remark: String,
    val saleNum: Int,
    val shopId: Int,
    val status: Int,
    val tenantId: Int,
    val tremTime: String
)
