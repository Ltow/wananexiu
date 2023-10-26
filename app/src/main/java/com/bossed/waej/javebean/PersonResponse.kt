package com.bossed.waej.javebean

data class PersonResponse(
    val code: Int,
    val msg: String,
    val rows: List<PersonRow>?,
    val total: Int
)

data class PersonRow(
    val address: String,
    val avatar: String,
    val certificate: String,
    val emergencyName: String,
    val emergencyPhone: String,
    val id: Int,
    val idCard: String,
    val madeAdd: Float,
    val madeRate: Float,
    val madeType: Int,
    val name: String,
    val phone: String,
    val roleName: String,
    val skillDescription: String,
    val shopId: Int,
    val tenantId: Int,
    val userRole: Int,
    val disableFlag: Int,
    var isSelect: Boolean,
    val workingLife: Int,
    val userName: String,
)