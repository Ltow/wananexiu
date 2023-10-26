package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName

data class SettlementResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: SettlementData,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("succ")
    val succ: Boolean
)

data class SettlementData(
    @SerializedName("order")
    val order: Order,
    @SerializedName("payString")
    val payString: Any
)

data class PayString(
    @SerializedName("appid")
    val appid: String,
    @SerializedName("noncestr")
    val noncestr: String,
    @SerializedName("package")
    val packageX: String,
    @SerializedName("partnerid")
    val partnerid: String,
    @SerializedName("prepayid")
    val prepayid: String,
    @SerializedName("sign")
    val sign: String,
    @SerializedName("timestamp")
    val timestamp: String
)


