package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName

data class BvdcUserIdResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String
)