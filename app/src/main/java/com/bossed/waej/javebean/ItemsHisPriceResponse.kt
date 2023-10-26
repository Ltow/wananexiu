package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class ItemsHisPriceResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<ItemBean>?,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("succ")
    val succ: Boolean
)
