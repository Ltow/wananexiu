package com.bossed.waej.javebean

data class DeliveryShopResponse(
    val code: Int,
    val `data`: DeliveryShopData,
    val message: String
)

data class DeliveryShopData(
    val endRow: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val list: List<DeliveryShopBean>,
    val navigateFirstPage: Int,
    val navigateLastPage: Int,
    val navigatePages: Int,
    val navigatepageNums: List<Int>,
    val nextPage: Int,
    val pageNum: Int,
    val pageSize: Int,
    val pages: Int,
    val prePage: Int,
    val size: Int,
    val startRow: Int,
    val total: Int
)

data class DeliveryShopBean(
    val account: String,
    val address: String,
    val browseCount: Int,
    val businessBegin: String,
    val businessEnd: String,
    val businessIntroduction: String,
    val businessStatus: Int,
    val businesstime: String,
    val city: String,
    val contactName: String,
    val contactPhone: String,
    val county: String,
    val createdTime: String,
    val deposit: Double,
    val deviceDescription: String,
    val distance: String,
    val doorPhoto: String,
    val followCount: Int,
    val freecheckitems: String,
    val fullname: String,
    val housekeeperId: Int,
    val id: Int,
    val isprized: Int,
    val latitude: String,
    val longitude: String,
    val mainModels: String,
    val operationStation: Int,
    val orderCancelCount: Int,
    val orderCount: Int,
    val orderNum: Int,
    val praseCount: Int,
    val province: String,
    val repairDate: String,
    val score: Int,
    val shopDescription: String,
    val shopLogo: String,
    val shopPhone: String,
    val shopTrade: String,
    val shopkeeperId: Int,
    val status: Int,
    val technicianNum: Int,
    val updatedTime: String
)