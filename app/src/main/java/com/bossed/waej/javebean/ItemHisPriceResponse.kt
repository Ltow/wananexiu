package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName

data class ItemHisPriceResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: ItemBean,
    @SerializedName("msg")
    val msg: String
)
