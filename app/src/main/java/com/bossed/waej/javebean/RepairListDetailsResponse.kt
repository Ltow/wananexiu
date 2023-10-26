package com.bossed.waej.javebean

data class RepairListDetailsResponse(
    val code: Int,
    val `data`: RepairListData,
    val message: String
)

data class RepairListData(
    val orderGoodCate: List<OrderGoodCate>,
    val orderGoods: List<RepairOrderGood>
)

data class OrderGoodCate(
    val brandId: Int,
    val cateDesc: String,
    val cateTitle: String,
    val commissionMoney: Double,
    val createAt: String,
    val id: Int,
    val isDeleted: Int,
    val pid: Int,
    val sort: Int,
    val status: Int
)

data class RepairOrderGood(
    val carId: Int,
    val cateId: Int,
    val cateName: String,
    val goodsNum: Int,
    val id: Int,
    val mjsid: String,
    val money: Double,
    val oem: String,
    val orderId: String,
    val promType: Int,
    val scenterId: Int,
    val shopName: String,
    val shopPhone: String,
    val specName: String
)