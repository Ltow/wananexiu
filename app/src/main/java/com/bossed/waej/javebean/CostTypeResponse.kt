package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class CostTypeResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("rows")
    val rows: List<CostTypeRow>?,
    @SerializedName("total")
    val total: Int
)

data class CostTypeRow(
    @SerializedName("billJournalList")
    val billJournalList: Any,
    @SerializedName("id")
    val id: String,
    @SerializedName("money")
    val money: Any,
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