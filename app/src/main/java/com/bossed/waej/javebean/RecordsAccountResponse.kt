package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class RecordsAccountResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("rows")
    var rows: List<RecordsAccountRow>,
    @SerializedName("total")
    var total: Int?
)

data class RecordsAccountRow(
    @SerializedName("bankCode")
    var bankCode: String?,
    @SerializedName("bankId")
    var bankId: String?,
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
    @SerializedName("defaultFlag")
    var defaultFlag: Int?,
    @SerializedName("ewalletId")
    var ewalletId: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("merchantNo")
    var merchantNo: String?,
    @SerializedName("orderNo")
    var orderNo: Any?,
    @SerializedName("orgCode")
    var orgCode: String?,
    @SerializedName("remark")
    var remark: String?,
    @SerializedName("shopId")
    var shopId: String?,
    @SerializedName("status")
    var status: Int?
)