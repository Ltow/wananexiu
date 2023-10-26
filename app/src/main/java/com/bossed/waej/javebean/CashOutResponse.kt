package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class CashOutResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var `data`: CashOutData?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("succ")
    var succ: Boolean?
)

data class CashOutData(
    @SerializedName("acctName")
    var acctName: Any?,
    @SerializedName("acctNo")
    var acctNo: String?,
    @SerializedName("bankId")
    var bankId: String?,
    @SerializedName("bankNo")
    var bankNo: Any?,
    @SerializedName("createUser")
    var createUser: Any?,
    @SerializedName("drawAmt")
    var drawAmt: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("merOrderNo")
    var merOrderNo: Any?,
    @SerializedName("merchantNo")
    var merchantNo: String?,
    @SerializedName("createTime")
    var createTime: String?,
    @SerializedName("updateTime")
    var updateTime: String?,
    @SerializedName("failCause")
    var failCause: String?,
    @SerializedName("nbkName")
    var nbkName: Any?,
    @SerializedName("orderNo")
    var orderNo: String?,
    @SerializedName("orgCode")
    var orgCode: String?,
    @SerializedName("remark")
    var remark: Any?,
    @SerializedName("shopId")
    var shopId: String?,
    @SerializedName("status")
    var status: Int?
)