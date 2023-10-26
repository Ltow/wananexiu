package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class MyProductResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<MyProductData>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("succ")
    val succ: Boolean
)

data class MyProductData(
    @SerializedName("id")
    val id: String,
    @SerializedName("menuCheckStrictly")
    val menuCheckStrictly: Int,
    @SerializedName("menuIds")
    val menuIds: String,
    @SerializedName("menuName")
    val menuName: String,
    @SerializedName("packageId")
    val packageId: String,
    @SerializedName("packageName")
    val packageName: String,
    @SerializedName("priceDay")
    val priceDay: String,
    @SerializedName("priceYear")
    val priceYear: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("shopId")
    val shopId: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("termTime")
    val termTime: String
)