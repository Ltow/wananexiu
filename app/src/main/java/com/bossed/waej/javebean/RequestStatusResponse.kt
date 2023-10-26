package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class RequestStatusResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var `data`: RequestStatusData?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("succ")
    var succ: Boolean?
)

data class RequestStatusData(
    @SerializedName("applyId")
    var applyId: String?,
    @SerializedName("auditStatus")
    var auditStatus: Int?,
    @SerializedName("createUser")
    var createUser: Any?,
    @SerializedName("entrustFile")
    var entrustFile: Any?,
    @SerializedName("entrustFileName")
    var entrustFileName: String?,
    @SerializedName("entrustFilePath")
    var entrustFilePath: String?,
    @SerializedName("entrustId")
    var entrustId: String?,
    @SerializedName("failCause")
    var failCause: Any?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("merCupNo")
    var merCupNo: String?,
    @SerializedName("merInnerNo")
    var merInnerNo: Any?,
    @SerializedName("merchantNo")
    var merchantNo: String?,
    @SerializedName("orderNo")
    var orderNo: String?,
    @SerializedName("orgCode")
    var orgCode: String?,
    @SerializedName("receiverNo")
    var receiverNo: String?,
    @SerializedName("remark")
    var remark: Any?,
    @SerializedName("shopId")
    var shopId: String?,
    @SerializedName("status")
    var status: Int?,
    @SerializedName("userId")
    var userId: Any?
)