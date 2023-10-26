package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName

data class HistoryItemResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<ItemBean>,
    @SerializedName("msg")
    val msg: String
)
