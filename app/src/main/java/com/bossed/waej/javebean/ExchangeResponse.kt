package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class ExchangeResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<ExchangeData>,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class ExchangeData(
    @SerializedName("id")
    var id: String,
    @SerializedName("menuCheckStrictly")
    var menuCheckStrictly: Boolean,
    @SerializedName("menuIds")
    var menuIds: String,
    @SerializedName("menuName")
    var menuName: String,
    @SerializedName("packageName")
    var packageName: String,
    @SerializedName("priceDay")
    var priceDay: String,
    @SerializedName("priceYear")
    var priceYear: String,
    @SerializedName("remark")
    var remark: String,
    @SerializedName("saleCommission")
    var saleCommission: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("tenantId")
    var tenantId: String,
    @SerializedName("updateTime")
    var updateTime: String
)