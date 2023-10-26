package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class PartListResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("rows")
    var rows: List<PartListRow>,
    @SerializedName("total")
    var total: Int
)

data class PartListRow(
    @SerializedName("brand")
    var brand: String?,
    @SerializedName("code")
    var code: String?,
    @SerializedName("createUser")
    var createUser: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("marketPrice")
    var marketPrice: String?,
    @SerializedName("purchasePrice")
    var purchasePrice: String?,
    @SerializedName("model")
    var model: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("oe")
    var oe: String?,
    @SerializedName("partStore")
    var partStore: PartStore?,
    @SerializedName("place")
    var place: String?,
    @SerializedName("remark")
    var remark: String?,
    @SerializedName("shopId")
    var shopId: String?,
    @SerializedName("status")
    var status: Int?,
    @SerializedName("unit")
    var unit: String?,
    @SerializedName("num")
    var num: Double?,
    @SerializedName("isSelected")
    var isSelected: Boolean = false
) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        0.0
    )
}

data class PartInfoResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("data")
    var data: PartListRow,
    @SerializedName("succ")
    var succ: Boolean
)