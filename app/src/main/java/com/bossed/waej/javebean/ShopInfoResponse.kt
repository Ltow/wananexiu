package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class ShopInfoResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: ShopInfoData,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class ShopInfoData(
    @SerializedName("account")
    var account: Any,
    @SerializedName("auditStatus")
    var auditStatus: Int?,
    @SerializedName("accountReceive")
    var accountReceive: Any,
    @SerializedName("accountUsername")
    var accountUsername: Any,
    @SerializedName("address")
    var address: String,
    @SerializedName("agreement")
    var agreement: Any,
    @SerializedName("browseCount")
    var browseCount: Int,
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
    @SerializedName("bankCard")
    var bankCard: String,
    @SerializedName("entryTime")
    var entryTime: String,
    @SerializedName("followCount")
    var followCount: Int,
    @SerializedName("freecheckitems")
    var freecheckitems: Any,
    @SerializedName("fullname")
    var fullname: String,
    @SerializedName("businessName")
    var businessName: String,
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
    var kmShopImageVoList: List<kmShopImageVo>?,
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
    var shopMiniQrcode: Any,
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
    @SerializedName("lklIdCardFront")
    var lklIdCardFront: String,
    @SerializedName("lklIdCardBack")
    var lklIdCardBack: String,
    @SerializedName("lklBusinessLicense")
    var lklBusinessLicense: String,
    @SerializedName("lklDoorPhoto")
    var lklDoorPhoto: String,
    @SerializedName("technicianNum")
    var technicianNum: Int,
    @SerializedName("withdrawalAccount")
    var withdrawalAccount: String
)

data class kmShopImageVo(
    @SerializedName("createDept")
    var createDept: Int,
    @SerializedName("createBy")
    var createBy: String,
    @SerializedName("createTime")
    var createTime: String,
    @SerializedName("updateBy")
    var updateBy: String,
    @SerializedName("updateTime")
    var updateTime: String,
    @SerializedName("params")
    var params: Any,
    @SerializedName("id")
    var id: Int,
    @SerializedName("shopId")
    var shopId: Int,
    @SerializedName("imageUrl")
    var imageUrl: String,
    @SerializedName("isMain")
    var isMain: Int
)