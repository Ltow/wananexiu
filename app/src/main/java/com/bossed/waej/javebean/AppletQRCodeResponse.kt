package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class AppletQRCodeResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var `data`: String?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("succ")
    var succ: Boolean?
)