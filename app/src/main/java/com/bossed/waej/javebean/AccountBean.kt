package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName

data class AccountBean(
    @SerializedName("accountId")
    var accountId: Int?,
    @SerializedName("accountName")
    var accountName: String,
    @SerializedName("accountType")
    var accountType: String,
    @SerializedName("money")
    var money: String = ""
) {
    constructor() : this(null, "", "", "")
}