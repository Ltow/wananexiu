package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class AdjRecordResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("rows")
    val rows: List<AdjRecordRow>,
    @SerializedName("total")
    val total: Int
)

data class AdjRecordRow(
    @SerializedName("accountId")
    val accountId: String,
    @SerializedName("accountName")
    val accountName: String,
    @SerializedName("balance")
    val balance: String,
    @SerializedName("billTime")
    val billTime: String,
    @SerializedName("billType")
    val billType: Int,
    @SerializedName("createUser")
    val createUser: Any,
    @SerializedName("expenseId")
    val expenseId: Any,
    @SerializedName("expenseName")
    val expenseName: Any,
    @SerializedName("id")
    val id: String,
    @SerializedName("methodId")
    val methodId: Any,
    @SerializedName("methodName")
    val methodName: Any,
    @SerializedName("money")
    val money: String,
    @SerializedName("payAccountId")
    val payAccountId: Any,
    @SerializedName("payAccountName")
    val payAccountName: String,
    @SerializedName("payMethodId")
    val payMethodId: Any,
    @SerializedName("payMethodName")
    val payMethodName: Any,
    @SerializedName("remark")
    val remark: Any,
    @SerializedName("shopId")
    val shopId: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("updateTime")
    val updateTime: String,
    @SerializedName("updateBy")
    val updateBy: String,
    @SerializedName("updateUser")
    val updateUser: Any
)