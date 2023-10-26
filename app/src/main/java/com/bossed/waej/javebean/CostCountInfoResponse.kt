package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class CostCountInfoResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: CostCountInfoData,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("succ")
    val succ: Boolean
)

data class CostCountInfoData(
    @SerializedName("billJournalList")
    val billJournalList: BillJournalList,
    @SerializedName("id")
    val id: String,
    @SerializedName("money")
    val money: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("platName")
    val platName: Any,
    @SerializedName("remark")
    val remark: Any,
    @SerializedName("shopId")
    val shopId: String,
    @SerializedName("sort")
    val sort: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("updateBy")
    val updateBy: String,
    @SerializedName("updateTime")
    val updateTime: String
)

//data class BillJournalList(
//    @SerializedName("code")
//    val code: Int,
//    @SerializedName("msg")
//    val msg: String,
//    @SerializedName("rows")
//    val rows: List<Row>,
//    @SerializedName("total")
//    val total: Int
//)
//
//data class Row(
//    @SerializedName("accountId")
//    val accountId: Any,
//    @SerializedName("accountName")
//    val accountName: Any,
//    @SerializedName("balance")
//    val balance: Any,
//    @SerializedName("billTime")
//    val billTime: Any,
//    @SerializedName("billType")
//    val billType: Any,
//    @SerializedName("createBy")
//    val createBy: Any,
//    @SerializedName("createUser")
//    val createUser: Any,
//    @SerializedName("expenseId")
//    val expenseId: Any,
//    @SerializedName("expenseName")
//    val expenseName: Any,
//    @SerializedName("id")
//    val id: Any,
//    @SerializedName("methodId")
//    val methodId: Any,
//    @SerializedName("methodName")
//    val methodName: Any,
//    @SerializedName("money")
//    val money: String,
//    @SerializedName("payAccountId")
//    val payAccountId: Any,
//    @SerializedName("payAccountName")
//    val payAccountName: Any,
//    @SerializedName("payMethodId")
//    val payMethodId: Any,
//    @SerializedName("payMethodName")
//    val payMethodName: Any,
//    @SerializedName("remark")
//    val remark: Any,
//    @SerializedName("shopId")
//    val shopId: Any,
//    @SerializedName("status")
//    val status: Any,
//    @SerializedName("summary")
//    val summary: Any,
//    @SerializedName("type")
//    val type: Any,
//    @SerializedName("updateBy")
//    val updateBy: Any,
//    @SerializedName("updateTime")
//    val updateTime: Any,
//    @SerializedName("updateUser")
//    val updateUser: Any
//)