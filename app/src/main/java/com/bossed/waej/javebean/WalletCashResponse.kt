package com.bossed.waej.javebean

data class WalletCashResponse(
    val code: Int,
    val `data`: WalletCashData,
    val message: String
)

data class WalletCashData(
    val endRow: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val info: CashInfo,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val list: List<CashBill>,
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

data class CashInfo(
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

data class CashBill(
    val account: String,
    val applyMoney: Double,
    val channel: String,
    val createTime: String,
    val facMoney: Double,
    val id: Int,
    val orderSn: String,
    val payFee: Double,
    val realName: String,
    val remark: String,
    val status: Int,
    val type: Int,
    val userId: Int
)