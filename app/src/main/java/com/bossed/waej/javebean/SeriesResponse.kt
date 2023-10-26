package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class SeriesResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String,
    @SerializedName("result")
    val result: SeriesResult
)

data class SeriesResult(
    @SerializedName("ds1")
    val ds1: List<SeriesDs1>
)

data class SeriesDs1(
    @SerializedName("clzl")
    val clzl: String,
    @SerializedName("familyId")
    val familyId: String,
    @SerializedName("familyName")
    val familyName: String
)