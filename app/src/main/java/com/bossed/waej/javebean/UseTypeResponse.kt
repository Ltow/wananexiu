package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class UseTypeResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String,
    @SerializedName("result")
    val result: UseTypeResult
)

data class UseTypeResult(
    @SerializedName("ds1")
    val ds1: List<UseTypeDs1>
)

data class UseTypeDs1(
    @SerializedName("操作说明")
    val 操作说明: String,
    @SerializedName("次数")
    val 次数: String,
    @SerializedName("金额")
    val 金额: String
)