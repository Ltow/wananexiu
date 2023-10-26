package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class CheckInfoResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: CheckInfoData,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class CheckInfoData(
    @SerializedName("createUser")
    var createUser: Any,
    @SerializedName("createBy")
    var createBy: String,
    @SerializedName("deleteTime")
    var deleteTime: String,
    @SerializedName("details")
    var details: CheckInfoDataDetails,
    @SerializedName("id")
    var id: String,
    @SerializedName("inventoryTime")
    var inventoryTime: Any,
    @SerializedName("orderSn")
    var orderSn: String,
    @SerializedName("remark")
    var remark: String,
    @SerializedName("settleTime")
    var settleTime: String,
    @SerializedName("shopId")
    var shopId: String,
    @SerializedName("status")
    var status: Int
)

data class CheckInfoDataDetails(
    @SerializedName("code")
    var code: Int,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("rows")
    var rows: List<CheckInfoDataDetailsRow>,
    @SerializedName("total")
    var total: Int
)

data class CheckInfoDataDetailsRow(
    @SerializedName("addAmount")
    var addAmount: String,
    @SerializedName("addQuantity")
    var addQuantity: String,
    @SerializedName("adjustAmount")
    var adjustAmount: String,
    @SerializedName("adjustCost")
    var adjustCost: String,
    @SerializedName("adjustQuantity")
    var adjustQuantity: String,
    @SerializedName("bookAmount")
    var bookAmount: String,
    @SerializedName("bookCost")
    var bookCost: String,
    @SerializedName("bookQuantity")
    var bookQuantity: String,
    @SerializedName("createUser")
    var createUser: Any,
    @SerializedName("id")
    var id: String,
    @SerializedName("orderId")
    var orderId: String,
    @SerializedName("part")
    var part: Part,
    @SerializedName("partId")
    var partId: String,
    @SerializedName("remark")
    var remark: Any
)