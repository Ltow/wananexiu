package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class RecordListResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("rows")
    var rows: List<RecordListRow>,
    @SerializedName("total")
    var total: Int
)

data class RecordListRow(
    @SerializedName("beginTime")
    var beginTime: String,
    @SerializedName("cardNo")
    var cardNo: String,
    @SerializedName("createdTime")
    var createdTime: String,
    @SerializedName("driverId")
    var driverId: String,
    @SerializedName("driverName")
    var driverName: String,
    @SerializedName("driverPhone")
    var driverPhone: String,
    @SerializedName("endTime")
    var endTime: String,
    @SerializedName("extraStr")
    var extraStr: Any,
    @SerializedName("face")
    var face: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("nickname")
    var nickname: String,
    @SerializedName("operator")
    var `operator`: Any,
    @SerializedName("operatorId")
    var operatorId: Any,
    @SerializedName("orderNo")
    var orderNo: String,
    @SerializedName("planId")
    var planId: Any,
    @SerializedName("remarks")
    var remarks: Any,
    @SerializedName("shopId")
    var shopId: String,
    @SerializedName("shopName")
    var shopName: String,
    @SerializedName("shopPhone")
    var shopPhone: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("technicianId")
    var technicianId: Any,
    @SerializedName("technicianNickName")
    var technicianNickName: Any,
    @SerializedName("updatedTime")
    var updatedTime: String,
    @SerializedName("userId")
    var userId: String,
    @SerializedName("username")
    var username: String
)