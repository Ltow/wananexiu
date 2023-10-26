package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class RemitAccountListResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("rows")
    var rows: List<RemitAccountListRow>?,
    @SerializedName("total")
    var total: Int?
)

data class RemitAccountListRow(
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
    @SerializedName("signUrl")
    var signUrl: String?,
    @SerializedName("contactMobile")
    var contactMobile: Any?,
    @SerializedName("contractId")
    var contractId: Any?,
    @SerializedName("createUser")
    var createUser: Any?,
    @SerializedName("e cId")
    var eCId: String?,
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
    var failCause: Any?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("isOnline")
    var isOnline: Int?,
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
    var mccCode: String?,
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
    var merBusiContent: String?,
    @SerializedName("merContactMobile")
    var merContactMobile: String?,
    @SerializedName("merContactName")
    var merContactName: String?,
    @SerializedName("merCupNo")
    var merCupNo: Any?,
    @SerializedName("merInnerNo")
    var merInnerNo: Any?,
    @SerializedName("merRegAddr")
    var merRegAddr: String?,
    @SerializedName("merRegDistCode")
    var merRegDistCode: String?,
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
    var orgCode: String?,
    @SerializedName("remark")
    var remark: String?,
    @SerializedName("separateRate")
    var separateRate: String?,
    @SerializedName("settlePeriod")
    var settlePeriod: String?,
    @SerializedName("settlementCycle")
    var settlementCycle: String?,
    @SerializedName("merRegDistCodeCache")
    var merRegDistCodeCache: String?,
    @SerializedName("shopId")
    var shopId: String?,
    @SerializedName("idCardFront")
    var idCardFront: String?,
    @SerializedName("idCardBack")
    var idCardBack: String?,
    @SerializedName("bankCard")
    var bankCard: String?,
    @SerializedName("status")
    var status: Int?,
    @SerializedName("isSamePerson")
    var isSamePerson: Int?
)