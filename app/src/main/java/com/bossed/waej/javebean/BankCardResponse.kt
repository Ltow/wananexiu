package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class BankCardResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var `data`: RecordsAccountRow?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("succ")
    var succ: Boolean?
)