package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class ContractResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var `data`: ContractData?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("succ")
    var succ: Boolean?
)

data class ContractData(
    @SerializedName("acctName")
    var acctName: String?,
    @SerializedName("acctNo")
    var acctNo: String?,
    @SerializedName("acctTypeCode")
    var acctTypeCode: String?,
    @SerializedName("auditStatus")
    var auditStatus: Int?,
    @SerializedName("businessLicenseName")
    var businessLicenseName: String?,
    @SerializedName("businessLicenseNo")
    var businessLicenseNo: String?,
    @SerializedName("certName")
    var certName: String?,
    @SerializedName("certNo")
    var certNo: String?,
    @SerializedName("certType")
    var certType: String?,
    @SerializedName("contactMobile")
    var contactMobile: String?,
    @SerializedName("contractApplyId")
    var contractApplyId: String?,
    @SerializedName("contractUrl")
    var contractUrl: String?,
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
    @SerializedName("ecNo")
    var ecNo: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("merchantId")
    var merchantId: Int?,
    @SerializedName("mobile")
    var mobile: String?,
    @SerializedName("openningBankCode")
    var openningBankCode: String?,
    @SerializedName("openningBankName")
    var openningBankName: String?,
    @SerializedName("orderNo")
    var orderNo: String?,
    @SerializedName("orgCode")
    var orgCode: String?,
    @SerializedName("params")
    var params: Params?,
    @SerializedName("remark")
    var remark: String?,
    @SerializedName("separateRate")
    var separateRate: String?,
    @SerializedName("settlementCycle")
    var settlementCycle: String?,
    @SerializedName("shopId")
    var shopId: Int?,
    @SerializedName("signUrl")
    var signUrl: String?,
    @SerializedName("tenantId")
    var tenantId: String?,
    @SerializedName("updateBy")
    var updateBy: String?,
    @SerializedName("updateTime")
    var updateTime: String?
)

class Params