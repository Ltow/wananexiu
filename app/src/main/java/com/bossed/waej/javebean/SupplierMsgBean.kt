package com.bossed.waej.javebean

data class SupplierMsgBean(
    val code: Int,
    val `data`: SupplierMsgData,
    val msg: String
)

data class SupplierMsgData(
    val address: String,
    val longitude: String,
    val latitude: String,
    val businessIntroduction: String,
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
    val remark: String,
    val supplierPayment: SupplierPayment?,
    val updateTime: String
)