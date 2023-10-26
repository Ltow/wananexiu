package com.bossed.waej.javebean

data class AliPayResponse(
    val code: Int,
    val `data`: PayData,
    val message: String
)

data class PayData(
    val id: Int,
    val realName: String,
    val status: Int,
    val userName: String
)