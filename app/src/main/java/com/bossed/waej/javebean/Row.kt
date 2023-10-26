package com.bossed.waej.javebean


import com.google.gson.annotations.SerializedName

data class Row(
    @SerializedName("acctName")
    var acctName: Any?,
    @SerializedName("acctNo")
    var acctNo: String?,
    @SerializedName("bankId")
    var bankId: String?,
    @SerializedName("bankNo")
    var bankNo: Any?,
    @SerializedName("createTime")
    var createTime: String?,
    @SerializedName("createUser")
    var createUser: Any?,
    @SerializedName("drawAmt")
    var drawAmt: String?,
    @SerializedName("failCause")
    var failCause: Any?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("merOrderNo")
    var merOrderNo: Any?,
    @SerializedName("merchantNo")
    var merchantNo: String?,
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
    var status: Int?,
    @SerializedName("updateTime")
    var updateTime: String?
)