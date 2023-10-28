package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class PostFileResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var `data`: PostFileData?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("succ")
    var succ: Boolean?
)

data class PostFileData(
    @SerializedName("createBy")
    var createBy: String?,
    @SerializedName("createDept")
    var createDept: Int?,
    @SerializedName("createTime")
    var createTime: String?,
    @SerializedName("createUser")
    var createUser: Int?,
    @SerializedName("data")
    var `data`: String?,
    @SerializedName("file")
    var `file`: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("merchantId")
    var merchantId: Int?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("orderNo")
    var orderNo: String?,
    @SerializedName("orgCode")
    var orgCode: String?,
    @SerializedName("params")
    var params: Params?,
    @SerializedName("remark")
    var remark: String?,
    @SerializedName("shopId")
    var shopId: Int?,
    @SerializedName("tenantId")
    var tenantId: String?,
    @SerializedName("tradeNo")
    var tradeNo: String?,
    @SerializedName("type")
    var type: String?,
    @SerializedName("updateBy")
    var updateBy: String?,
    @SerializedName("updateTime")
    var updateTime: String?,
    @SerializedName("url")
    var url: String?
)