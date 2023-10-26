package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class JournalSearchResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: JournalSearchData,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("succ")
    val succ: Boolean
)

data class JournalSearchData(
    @SerializedName("billJournalList")
    val billJournalList: BillJournalList,
    @SerializedName("monenPayment")
    val monenPayment: String,
    @SerializedName("monenReceivables")
    val monenReceivables: String,
    @SerializedName("money")
    val money: String,
    @SerializedName("numPayment")
    val numPayment: Int,
    @SerializedName("numReceivables")
    val numReceivables: Int
)

data class BillJournalList(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("rows")
    val rows: List<JournalSearchDataRow>,
    @SerializedName("total")
    val total: Int
)

data class JournalSearchDataRow(
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
    val methodId: String,
    @SerializedName("methodName")
    val methodName: String,
    @SerializedName("money")
    val money: String,
    @SerializedName("payAccountId")
    val payAccountId: Any,
    @SerializedName("payAccountName")
    val payAccountName: Any,
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