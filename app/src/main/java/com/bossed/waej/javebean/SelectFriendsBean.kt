package com.bossed.waej.javebean

data class SelectFriendsBean(
    val code: Int,
    val `data`: List<FriendsData>,
    val msg: String
)

data class FriendsData(
    val address: String,
    val contacts: String,
    val contacts2: String,
    val contacts2Phone: String,
    val contactsPhone: String,
    val createBy: String,
    val createTime: String,
    val id: Int,
    val name: String,
    val shopId: Int,
    val status: String,
    val supplierId: Int,
    val tag: String,
    val tenantId: Int,
    val type: Int,
    val updateBy: String,
    val updateTime: String,
    var isSelect:Boolean
)