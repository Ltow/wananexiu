package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName

data class BuyProductResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<BuyProductData>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("succ")
    val succ: Boolean
)

data class BuyProductData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("menuCheckStrictly")
    val menuCheckStrictly: Boolean,
    @SerializedName("menuIds")
    val menuIds: String,
    @SerializedName("menuName")
    val menuName: String,
    @SerializedName("packageName")
    val packageName: String,
    @SerializedName("priceDay")
    val priceDay: String,
    @SerializedName("priceYear")
    val priceYear: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("updateTime")
    val updateTime: String,
    @SerializedName("termTime")
    val termTime: String,
    @SerializedName("isSelect")
    var isSelect: Boolean,
    @SerializedName("num")
    var num: Int = 1,
    @SerializedName("orderType")
    var orderType: Int
)