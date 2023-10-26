package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class TermTimeResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: String,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("succ")
    val succ: Boolean
)