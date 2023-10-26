package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class CheckListResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("rows")
    var rows: List<CheckListRow>,
    @SerializedName("total")
    var total: Int
)

data class CheckListRow(
    @SerializedName("createUser")
    var createUser: Any,
    @SerializedName("createBy")
    var createBy: Any,
    @SerializedName("deleteTime")
    var deleteTime: String,
    @SerializedName("details")
    var details: Any,
    @SerializedName("id")
    var id: String,
    @SerializedName("inventoryTime")
    var inventoryTime: Any,
    @SerializedName("orderSn")
    var orderSn: String,
    @SerializedName("part")
    var part: Any,
    @SerializedName("remark")
    var remark: String,
    @SerializedName("settleTime")
    var settleTime: String,
    @SerializedName("shopId")
    var shopId: String,
    @SerializedName("status")
    var status: Int
)