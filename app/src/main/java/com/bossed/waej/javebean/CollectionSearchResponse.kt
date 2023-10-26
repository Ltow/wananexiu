package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class CollectionSearchResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("rows")
    val rows: List<CollectionSearchRow>,
    @SerializedName("total")
    val total: Int
)

data class CollectionSearchRow(
    @SerializedName("balance")
    val balance: String,
    @SerializedName("billTime")
    val billTime: String,
    @SerializedName("createUser")
    val createUser: Any,
    @SerializedName("customerId")
    val customerId: String,
    @SerializedName("discount")
    val discount: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("model")
    val model: Any,
    @SerializedName("moneyOwe")
    val moneyOwe: String,
    @SerializedName("moneyPay")
    val moneyPay: String,
    @SerializedName("orderSn")
    val orderSn: String,
    @SerializedName("remaining")
    val remaining: Any,
    @SerializedName("remark")
    val remark: Any,
    @SerializedName("shopId")
    val shopId: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("updateBy")
    val updateBy: String,
    @SerializedName("updateTime")
    val updateTime: String,
    @SerializedName("updateUser")
    val updateUser: Any,
    @SerializedName("customerName")
    val customerName: String,
    @SerializedName("customerPhone")
    val customerPhone: String
)