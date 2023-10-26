package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class ApplicableResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String,
    @SerializedName("result")
    val result: ApplicableResult
)

data class  ApplicableResult(
    @SerializedName("ds1")
    val ds1: List<ApplicableDs1>
)

data class  ApplicableDs1(
    @SerializedName("车型")
    val 车型: String
)