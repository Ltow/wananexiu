package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class BalanceDetailResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("rows")
    var rows: List<BalanceDetailRow>,
    @SerializedName("total")
    var total: Int?
)

data class BalanceDetailRow(
    @SerializedName("balance")
    var balance: String?,
    @SerializedName("billTime")
    var billTime: String?,
    @SerializedName("billType")
    var billType: Int?,
    @SerializedName("channel")
    var channel: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("money")
    var money: String?,
    @SerializedName("moneyFee")
    var moneyFee: Int?,
    @SerializedName("offer")
    var offer: String?,
    @SerializedName("orderSn")
    var orderSn: String?,
    @SerializedName("remark")
    var remark: String?,
    @SerializedName("shopId")
    var shopId: Int?,
    @SerializedName("status")
    var status: Int?,
    @SerializedName("type")
    var type: Int?,
    @SerializedName("userId")
    var userId: Int?
)