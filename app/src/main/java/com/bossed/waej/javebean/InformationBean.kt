package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class InformationBean(
    @SerializedName("num")
    val num: String,
    @SerializedName("oe")
    val oe: String,
    @SerializedName("4sPrice")
    val sPrice: String,
    @SerializedName("unit")
    val unit: String
)