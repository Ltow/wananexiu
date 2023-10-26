package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName

data class OnlineResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: OnlineData,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class OnlineData(
    @SerializedName("kmAppointRecordVo")
    var kmAppointRecordVo: List<KmAppointRecordVo>,
    @SerializedName("kmShop")
    var kmShop: KmShop,
    @SerializedName("shopCount")
    var shopCount: ShopCount
)

data class KmAppointRecordVo(
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

data class KmShop(
    @SerializedName("account")
    var account: Any,
    @SerializedName("accountReceive")
    var accountReceive: Any,
    @SerializedName("accountUsername")
    var accountUsername: Any,
    @SerializedName("address")
    var address: String,
    @SerializedName("merchantNo")
    var merchantNo: String,
    @SerializedName("agreement")
    var agreement: Any,
    @SerializedName("browseCount")
    var browseCount: Int,
    @SerializedName("pageTemplateId")
    var pageTemplateId: Int,
    @SerializedName("businessBegin")
    var businessBegin: String,
    @SerializedName("businessEnd")
    var businessEnd: String,
    @SerializedName("businessIntroduction")
    var businessIntroduction: Any,
    @SerializedName("businessLicense")
    var businessLicense: String,
    @SerializedName("businessStatus")
    var businessStatus: Int,
    @SerializedName("waejStatus")
    var waejStatus: Int,
    @SerializedName("businessTime")
    var businessTime: String,
    @SerializedName("city")
    var city: String,
    @SerializedName("contactName")
    var contactName: String?,
    @SerializedName("contactPhone")
    var contactPhone: String,
    @SerializedName("county")
    var county: String,
    @SerializedName("deposit")
    var deposit: String,
    @SerializedName("deviceDescription")
    var deviceDescription: Any,
    @SerializedName("doorPhoto")
    var doorPhoto: String,
    @SerializedName("entryTime")
    var entryTime: String,
    @SerializedName("followCount")
    var followCount: Int,
    @SerializedName("freecheckitems")
    var freecheckitems: String?,
    @SerializedName("fullname")
    var fullname: String,
    @SerializedName("housekeeperId")
    var housekeeperId: Any,
    @SerializedName("id")
    var id: String,
    @SerializedName("idCardBack")
    var idCardBack: String,
    @SerializedName("idCardFront")
    var idCardFront: String,
    @SerializedName("inviteCode")
    var inviteCode: String,
    @SerializedName("isDefault")
    var isDefault: Any,
    @SerializedName("kmShopImageVoList")
    var kmShopImageVoList: Any,
    @SerializedName("latitude")
    var latitude: String,
    @SerializedName("longitude")
    var longitude: String,
    @SerializedName("madeFeeRate")
    var madeFeeRate: String,
    @SerializedName("madeRate")
    var madeRate: String,
    @SerializedName("madeType")
    var madeType: Int,
    @SerializedName("mainModels")
    var mainModels: Any,
    @SerializedName("operationStation")
    var operationStation: Int,
    @SerializedName("orderCancelCount")
    var orderCancelCount: Int,
    @SerializedName("orderCount")
    var orderCount: Int,
    @SerializedName("praseCount")
    var praseCount: Int,
    @SerializedName("promise")
    var promise: Any,
    @SerializedName("promotion")
    var promotion: Any,
    @SerializedName("province")
    var province: String,
    @SerializedName("rejectCause")
    var rejectCause: Any,
    @SerializedName("score")
    var score: Int,
    @SerializedName("shopDescription")
    var shopDescription: String,
    @SerializedName("shopMiniQrcode")
    var shopMiniQrcode: String?,
    @SerializedName("shopNumber")
    var shopNumber: String,
    @SerializedName("shopPhone")
    var shopPhone: String,
    @SerializedName("shopTrade")
    var shopTrade: Any,
    @SerializedName("shopType")
    var shopType: String,
    @SerializedName("shopkeeperId")
    var shopkeeperId: Any,
    @SerializedName("status")
    var status: Int,
    @SerializedName("tags")
    var tags: String,
    @SerializedName("technicianNum")
    var technicianNum: Int,
    @SerializedName("withdrawalAccount")
    var withdrawalAccount: String
)

data class ShopCount(
    @SerializedName("id")
    var id: Any?,
    @SerializedName("numOrderDay")
    var numOrderDay: Int?,
    @SerializedName("numOrderTotle")
    var numOrderTotle: Int?,
    @SerializedName("pendOrderNum")
    var pendOrderNum: String?,
    @SerializedName("turnoverDay")
    var turnoverDay: String?,
    @SerializedName("turnoverMonth")
    var turnoverMonth: String?,
    @SerializedName("turnoverTotle")
    var turnoverTotle: String?
)

