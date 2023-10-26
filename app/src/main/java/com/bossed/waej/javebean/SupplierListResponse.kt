package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName

data class SupplierListResponse(
    val code: Int,
    val msg: String,
    val rows: List<SupplierRow>,
    val total: Int
)

data class SupplierRow(
    val address: String,
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
    val updateTime: String,
    val supplierPayment: SupplierPayment?,
    var isSelect: Boolean
)

data class SupplierPayment(
    @SerializedName("balance")
    val balance: String,
    @SerializedName("contacts")
    val contacts: String,
    @SerializedName("contactsPhone")
    val contactsPhone: String,
    @SerializedName("createUser")
    val createUser: Int,
    @SerializedName("discount")
    val discount: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("moneyOwe")
    val moneyOwe: String,
    @SerializedName("moneyPay")
    val moneyPay: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("shopId")
    val shopId: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("supplierId")
    val supplierId: Int,
    @SerializedName("supplierName")
    val supplierName: String,
    @SerializedName("updateTime")
    val updateTime: String,
    @SerializedName("updateUser")
    val updateUser: Int
)