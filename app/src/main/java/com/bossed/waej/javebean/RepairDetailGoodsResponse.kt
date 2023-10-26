package com.bossed.waej.javebean

data class RepairDetailGoodsResponse(
    val code: Int,
    val `data`: RepairDetailData,
    val message: String
)

data class RepairDetailData(
    val orderGoods: List<OrderGood>
)

data class OrderGood(
    val carId: Int,
    val cateId: Int,
    val cateName: String,
    val goodsNum: Int,
    val id: Int,
    val mjsid: String,
    val money: Double,
    val orderId: String,
    val promType: Int,
    val shopId: Int,
    val shopName: String,
    val shopPhone: String,
    val specName: String
)