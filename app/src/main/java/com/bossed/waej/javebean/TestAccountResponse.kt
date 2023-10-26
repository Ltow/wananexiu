package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class TestAccountResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<TestAccountData>,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class TestAccountData(
    @SerializedName("companyName")
    var companyName: String,
    @SerializedName("enableStatus")
    var enableStatus: Int,
    @SerializedName("id")
    var id: String,
    @SerializedName("nickName")
    var nickName: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("isOnline")
    var isOnline: Boolean,
    @SerializedName("userId")
    var userId: String,
    @SerializedName("userName")
    var userName: String
)