package com.bossed.waej.javebean

data class LoginResponse(
    val code: Int,
    val `data`: LoginData,
    val msg: String
)

data class LoginData(
    val addressId: Int,
    val alipay: Int,
    val centerId: Int,
    val createTime: String,
    val id: Int,
    val isReal: Int,
    val lastLogin: String,
    val money: Double,
    val nickName: String,
    val phone: String,
    val realName: String,
    val registrationId: String,
    val status: Int,
    val userName: String
)