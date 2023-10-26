package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class FinanceResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: FinanceData,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class FinanceData(
    @SerializedName("balance")
    var balance: String,
    @SerializedName("payment")
    var payment: String,
    @SerializedName("receivables")
    var receivables: String
)