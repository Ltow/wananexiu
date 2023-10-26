package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class StockListResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("rows")
    var rows: List<StockListRow>?,
    @SerializedName("total")
    var total: Int
)

data class StockListRow(
    @SerializedName("amount")
    var amount: String,
    @SerializedName("cost")
    var cost: String,
    @SerializedName("details")
    var details: Any,
    @SerializedName("id")
    var id: String,
    @SerializedName("part")
    var part: Part,
    @SerializedName("partId")
    var partId: String,
    @SerializedName("quantity")
    var quantity: String,
    @SerializedName("remark")
    var remark: Any
)
