package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class RemitAccountResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var `data`: RemitAccountData?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("succ")
    var succ: Boolean?
)

data class RemitAccountData(
    @SerializedName("acctName")
    var acctName: String?,
    @SerializedName("acctNo")
    var acctNo: String?,
    @SerializedName("acctTypeCode")
    var acctTypeCode: String?,
    @SerializedName("applyId")
    var applyId: Any?,
    @SerializedName("auditStatus")
    var auditStatus: Int?,
    @SerializedName("clearingBankCode")
    var clearingBankCode: String?,
    @SerializedName("contactMobile")
    var contactMobile: Any?,
    @SerializedName("contractId")
    var contractId: Any?,
    @SerializedName("createBy")
    var createBy: Any?,
    @SerializedName("createDept")
    var createDept: Any?,
    @SerializedName("createTime")
    var createTime: Any?,
    @SerializedName("createUser")
    var createUser: Any?,
    @SerializedName("ecId")
    var ecId: Any?,
    @SerializedName("ecNo")
    var ecNo: Any?,
    @SerializedName("entrustData")
    var entrustData: Any?,
    @SerializedName("entrustFile")
    var entrustFile: Any?,
    @SerializedName("entrustId")
    var entrustId: Any?,
    @SerializedName("entrustName")
    var entrustName: Any?,
    @SerializedName("entrustType")
    var entrustType: Any?,
    @SerializedName("entrustUrl")
    var entrustUrl: Any?,
    @SerializedName("failCause")
    var failCause: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("isOnline")
    var isOnline: Any?,
    @SerializedName("larIdType")
    var larIdType: String?,
    @SerializedName("larIdcard")
    var larIdcard: String?,
    @SerializedName("larIdcardExpDt")
    var larIdcardExpDt: String?,
    @SerializedName("larIdcardStDt")
    var larIdcardStDt: String?,
    @SerializedName("larName")
    var larName: String?,
    @SerializedName("mccCode")
    var mccCode: Any?,
    @SerializedName("merBizName")
    var merBizName: String?,
    @SerializedName("merBlis")
    var merBlis: String?,
    @SerializedName("merBlisExpDt")
    var merBlisExpDt: String?,
    @SerializedName("merBlisName")
    var merBlisName: String?,
    @SerializedName("merBlisStDt")
    var merBlisStDt: String?,
    @SerializedName("merBusiContent")
    var merBusiContent: Any?,
    @SerializedName("merContactMobile")
    var merContactMobile: String?,
    @SerializedName("merContactName")
    var merContactName: String?,
    @SerializedName("merCupNo")
    var merCupNo: String?,
    @SerializedName("merInnerNo")
    var merInnerNo: Any?,
    @SerializedName("merRegAddr")
    var merRegAddr: String?,
    @SerializedName("merRegDistCode")
    var merRegDistCode: String?,
    @SerializedName("merRegDistCodeCache")
    var merRegDistCodeCache: String?,
    @SerializedName("merRegName")
    var merRegName: String?,
    @SerializedName("merchantNo")
    var merchantNo: Any?,
    @SerializedName("openningBankCode")
    var openningBankCode: String?,
    @SerializedName("openningBankName")
    var openningBankName: String?,
    @SerializedName("orderNo")
    var orderNo: Any?,
    @SerializedName("orgCode")
    var orgCode: Any?,
    @SerializedName("remark")
    var remark: String?,
    @SerializedName("separateRate")
    var separateRate: Any?,
    @SerializedName("settlePeriod")
    var settlePeriod: Any?,
    @SerializedName("settlementCycle")
    var settlementCycle: Any?,
    @SerializedName("shopId")
    var shopId: Any?,
    @SerializedName("status")
    var status: Any?,
    @SerializedName("tenantId")
    var tenantId: Any?,
    @SerializedName("updateBy")
    var updateBy: Any?,
    @SerializedName("signUrl")
    var signUrl: String?,
    @SerializedName("updateTime")
    var updateTime: Any?
)