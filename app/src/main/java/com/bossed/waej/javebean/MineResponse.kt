package com.bossed.waej.javebean

data class MineResponse(
    val code: Int,
    val `data`: MineData,
    val msg: String
)

data class MineData(
    val kmShopHomeVo: MineKmShopHomeVo,
    val numCar: Int,
    val numInShop: Int,
    val numOnShop: Int,
    val numWorkorder: Int,
    val platNotice: Any,
    val sysUser: MineSysUser,
    val turnoverDay: String,
    val turnoverMonth: String,
    val kmShopWallet: KmShopWallet
)

data class KmShopWallet(
    val balance: Float,
    val balanceFrozen: Float,
    val id: Int,
    val totalMoneyOut: Float,
    val totalMoneyProfit: Float,
)

data class MineKmShopHomeVo(
    val address: Any,
    val browseCount: Int,
    val businessBegin: String,
    val businessEnd: String,
    val businessIntroduction: Any,
    val businessLicense: Any,
    val businessStatus: Int,
    val businessTime: String,
    val city: Any,
    val contactName: Any,
    val contactPhone: String,
    val county: Any,
    val deviceDescription: Any,
    val doorPhoto: Any,
    val entryTime: String,
    val followCount: Int,
    val freecheckitems: Any,
    val fullname: String,
    val inviteCode: String,
    val id: String,
    val idCardBack: String,
    val idCardFront: String,
    val isDefault: Any,
    val kmShopImageVoList: Any,
    val latitude: Any,
    val longitude: Any,
    val madeFeeRate: String,
    val madeRate: String,
    val madeType: Int,
    val mainModels: Any,
    val operationStation: Any,
    val praseCount: Int,
    val promise: Any,
    val promotion: Any,
    val province: Any,
    val rejectCause: Any,
    val score: Int,
    val shopDescription: Any,
    val shopMiniQrcode: Any,
    val shopNumber: String,
    val shopPhone: String,
    val shopTrade: Any,
    val shopType: String,
    val shopkeeperId: Any,
    val status: Int,
    val tags: Any,
    val technicianNum: Any,
    val merchantNo: String,
    val withdrawalAccount: Any
)

data class MineSysUser(
    val admin: Boolean,
    val avatar: String,
    val delFlag: String,
    val deptId: String,
    val email: String,
    val loginDate: String,
    val loginIp: String,
    val nickName: String,
    val phonenumber: String,
    val remark: Any,
    val roleId: Any,
    val sex: String,
    val shopId: String,
    val status: String,
    val userId: String,
    val userName: String,
    val inviteCode: String,
    val userType: String
)