package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class WalletResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var `data`: WalletData?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("succ")
    var succ: Boolean?
)

data class WalletData(
    @SerializedName("balance")
    var balance: String?,
    @SerializedName("balanceFrozen")
    var balanceFrozen: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("totalMoneyOut")
    var totalMoneyOut: Int?,
    @SerializedName("totalMoneyProfit")
    var totalMoneyProfit: Int?
)