package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class BankCardInfoResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var `data`: BankCardInfoData?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("succ")
    var succ: Boolean?
)

data class BankCardInfoData(
    @SerializedName("bankCode")
    var bankCode: String?,
    @SerializedName("bankName")
    var bankName: String?,
    @SerializedName("cardName")
    var cardName: String?,
    @SerializedName("cardNo")
    var cardNo: String?,
    @SerializedName("cardType")
    var cardType: String?,
    @SerializedName("clearingBankCode")
    var clearingBankCode: String?,
    @SerializedName("createUser")
    var createUser: Any?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("orderNo")
    var orderNo: String?,
    @SerializedName("orgCode")
    var orgCode: String?,
    @SerializedName("remark")
    var remark: Any?,
    @SerializedName("shopId")
    var shopId: String?
)