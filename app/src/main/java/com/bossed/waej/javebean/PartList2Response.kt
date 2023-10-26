package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class PartList2Response(
    @SerializedName("code")
    var code: Int,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("rows")
    var rows: List<PartListRow>,
    @SerializedName("total")
    var total: Int
)

data class PartStore(
    @SerializedName("amount")
    var amount: String,
    @SerializedName("cost")
    var cost: String,
    @SerializedName("details")
    var details: Any,
    @SerializedName("id")
    var id: String,
    @SerializedName("part")
    var part: Part?,
    @SerializedName("partId")
    var partId: String,
    @SerializedName("quantity")
    var quantity: String,
    @SerializedName("adjustCost")
    var adjustCost: String,
    @SerializedName("adjustQuantity")
    var adjustQuantity: String,
    @SerializedName("adjustAmount")
    var adjustAmount: String,
    @SerializedName("remark")
    var remark: String
) {
    constructor() : this("", "", "", "", null, "", "", "0.0", "0.0", "0.0", "")
}