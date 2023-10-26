package com.bossed.waej.javebean

data class RepairResponse(
    val code: Int,
    val `data`: RepairData,
    val message: String
)

data class RepairData(
    val endRow: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val list: List<RepairBean>,
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

data class RepairBean(
    val addTime: String,
    val address: String,
    val city: String,
    val consignee: String,
    val deleted: Int,
    val district: String,
    val id: Int,
    val mobile: String,
    val orderId: String,
    val orderStatus: Int,
    val platformSettlePrice: Double,
    val promType: Int,
    val province: String,
    val scenterId: Int,
    val shippingStatus: Int,
    val shopName: String,
    val shopPhone: String
)