package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class StockInfoResponse(
    @SerializedName("amount")
    var amount: String,
    @SerializedName("cost")
    var cost: String,
    @SerializedName("details")
    var details: Details,
    @SerializedName("id")
    var id: String,
    @SerializedName("part")
    var part: Part,
    @SerializedName("partId")
    var partId: String,
    @SerializedName("quantity")
    var quantity: String,
    @SerializedName("remark")
    var remark: String
)

data class Details(
    @SerializedName("code")
    var code: Int,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("rows")
    var rows: List<DetailsRow>,
    @SerializedName("total")
    var total: Int
)


data class DetailsRow(
    @SerializedName("amount")
    var amount: String,
    @SerializedName("balanceAmount")
    var balanceAmount: String,
    @SerializedName("balanceQuantity")
    var balanceQuantity: String,
    @SerializedName("billTime")
    var billTime: String,
    @SerializedName("businessOrderNo")
    var businessOrderNo: String,
    @SerializedName("cost")
    var cost: String,
    @SerializedName("createUser")
    var createUser: Any,
    @SerializedName("id")
    var id: String,
    @SerializedName("mode")
    var mode: String,
    @SerializedName("partId")
    var partId: String,
    @SerializedName("quantity")
    var quantity: String,
    @SerializedName("remark")
    var remark: String,
    @SerializedName("shopId")
    var shopId: String,
    @SerializedName("storeId")
    var storeId: String,
    @SerializedName("storeOrderNo")
    var storeOrderNo: String,
    @SerializedName("summary")
    var summary: String,
    @SerializedName("type")
    var type: Int
)