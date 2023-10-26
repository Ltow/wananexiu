package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class TermRemindResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<MyProductData>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("succ")
    val succ: Boolean
)

data class TermRemindData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("menuCheckStrictly")
    val menuCheckStrictly: Int,
    @SerializedName("menuIds")
    val menuIds: String,
    @SerializedName("menuName")
    val menuName: String,
    @SerializedName("packageId")
    val packageId: Int,
    @SerializedName("packageName")
    val packageName: String,
    @SerializedName("priceDay")
    val priceDay: Int,
    @SerializedName("priceYear")
    val priceYear: Int,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("shopId")
    val shopId: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("termTime")
    val termTime: String
)