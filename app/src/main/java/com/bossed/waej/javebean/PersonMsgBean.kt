package com.bossed.waej.javebean

data class PersonMsgBean(
    val code: Int,
    val `data`: PersonData,
    val msg: String
)

data class PersonData(
    val address: String,
    val avatar: String,
    val certificate: String,
    val emergencyName: String,
    val emergencyPhone: String,
    val entryTime: String,
    val remark: String,
    val id: Int,
    val idCard: String,
    val madeAdd: Float,
    val madeRate: Float,
    val madeType: Int,
    val name: String,
    val phone: String,
    val roleName: String,
    val shopId: Int,
    val skillDescription: String,
    val tenantId: Int,
    val userRole: Int,
    val disableFlag: Int,
    val workingLife: Int,
    val dataScope: String,
    val userName:String
)