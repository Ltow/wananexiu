package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class BankAccountResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("rows")
    val rows: List<BankAccountRow>?,
    @SerializedName("total")
    val total: Int
)

data class BankAccountRow(
    @SerializedName("account")
    val account: String,
    @SerializedName("balance")
    val balance: String,
    @SerializedName("bank")
    val bank: String,
    @SerializedName("createUser")
    val createUser: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isDefault")
    val isDefault: Int,
    @SerializedName("methodId")
    val methodId: Int,
    @SerializedName("methodName")
    val methodName: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("remark")
    val remark: Any,
    @SerializedName("shopId")
    val shopId: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("updateTime")
    val updateTime: String
)

data class SelAccountBean(
    @SerializedName("methodName")
    val methodName: String,
    @SerializedName("rows")
    val rows: List<BankAccountRow>
)