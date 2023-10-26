package com.bossed.waej.javebean

data class PlatformRepairResponse(
    val code: Int,
    val `data`: PlatformRepairData,
    val message: String
)

data class PlatformRepairData(
    val repairGoods: List<RepairGood>
)

data class RepairGood(
    val carId: Int,
    val cateId: Int,
    val cateName: String,
    val goodsNum: Int,
    val id: Int,
    val mjsid: String,
    val oem: String,
    val promType: Int,
    val scenterId: Int,
    val scenterName: String,
    val scenterPhone: String,
    val specName: String,
    val state: Int
)