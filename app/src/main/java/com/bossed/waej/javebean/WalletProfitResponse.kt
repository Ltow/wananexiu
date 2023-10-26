package com.bossed.waej.javebean

data class WalletProfitResponse(
    val code: Int,
    val `data`: WalletProfitData,
    val message: String
)

data class WalletProfitData(
    val endRow: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val info: ProfitInfo,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val list: List<ProfitBill>,
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

data class ProfitInfo(
    val address: String,
    val addressId: Int,
    val alipay: Int,
    val centerId: Int,
    val city: String,
    val county: String,
    val createTime: String,
    val headimg: String,
    val id: Int,
    val idCard: String,
    val isReal: Int,
    val lastLogin: String,
    val latitude: String,
    val longLat: String,
    val longitude: String,
    val money: Double,
    val nickName: String,
    val password: String,
    val phone: String,
    val province: String,
    val realName: String,
    val registrationId: String,
    val remark: String,
    val status: Int,
    val userName: String
)

data class ProfitBill(
    val amount: Double,
    val balance: Double,
    val channel: String,
    val createTime: String,
    val id: Int,
    val insertTime: String,
    val offer: String,
    val orderId: String,
    val remark: String,
    val status: Int,
    val type: Int,
    val userId: Int
)