package com.bossed.waej.javebean

data class VipManageBean(
    val code: Int,
    val msg: String,
    val rows: List<VipCardRow>,
    val total: Int
)

data class VipCardRow(
    val cardName: String,
    val cardNo: String,
    val costPrice: Int,
    val giveAmount: Int,
    val id: Int,
    val kmShopClubCardItemsList: List<KmShopClubCardItems>,
    val marketPrice: Int,
    val remark: String,
    val disenableTime: String,
    val enableTime: String,
    val saleNum: Int,
    val shopId: Int,
    val status: Int,
    val tenantId: Int,
    val tremTime: String
)

//data class KmShopClubCardItems(
//    val availableQuantity: Float,
//    val cardId: Int,
//    val cardName: String,
//    val cardNo: String,
//    val consumptionCode: String,
//    val createBy: String,
//    val createTime: String,
//    val delFlag: String,
//    val employeeCommission: Int,
//    val flagGive: Boolean,
//    val id: Int,
//    val itemName: String,
//    val leftQuantity: Int,
//    val materialCost: Int,
//    val remark: String,
//    val searchValue: String,
//    val shopId: Int,
//    val status: Int,
//    val tenantId: Int,
//    val updateBy: String,
//    val updateTime: String,
//    val usedQuantity: Int,
//    val version: Int
//)