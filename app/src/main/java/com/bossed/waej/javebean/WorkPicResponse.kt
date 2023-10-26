package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class WorkPicResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("rows")
    val rows: List<WorkPicRow>,
    @SerializedName("total")
    val total: Int
)

data class WorkPicRow(
    @SerializedName("id")
    val id: Int,
    @SerializedName("img")
    val img: String,
    @SerializedName("itemId")
    val itemId: Any,
    @SerializedName("orderId")
    val orderId: Int,
    @SerializedName("ossId")
    val ossId: Any,
    @SerializedName("remark")
    val remark: Any,
    @SerializedName("shopId")
    val shopId: Int,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("updateTime")
    val updateTime: String
)