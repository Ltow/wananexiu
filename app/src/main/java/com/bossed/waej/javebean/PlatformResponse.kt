package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class PlatformResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var `data`: PlatformData?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("succ")
    var succ: Boolean?
)

data class PlatformData(
    @SerializedName("acctCertificateNo")
    var acctCertificateNo: String?,
    @SerializedName("acctCertificateType")
    var acctCertificateType: String?,
    @SerializedName("acctClearBankCode")
    var acctClearBankCode: String?,
    @SerializedName("acctName")
    var acctName: String?,
    @SerializedName("acctNo")
    var acctNo: String?,
    @SerializedName("acctOpenBankCode")
    var acctOpenBankCode: String?,
    @SerializedName("acctOpenBankName")
    var acctOpenBankName: String?,
    @SerializedName("acctTypeCode")
    var acctTypeCode: String?,
    @SerializedName("attach")
    var attach: Any?,
    @SerializedName("attachIds")
    var attachIds: String?,
    @SerializedName("attachName")
    var attachName: Any?,
    @SerializedName("auditStatus")
    var auditStatus: Int?,
    @SerializedName("contactMobile")
    var contactMobile: String?,
    @SerializedName("createBy")
    var createBy: Any?,
    @SerializedName("createDept")
    var createDept: Any?,
    @SerializedName("createTime")
    var createTime: Any?,
    @SerializedName("createUser")
    var createUser: Any?,
    @SerializedName("id")
    var id: Any?,
    @SerializedName("legalPersonCertificateType")
    var legalPersonCertificateType: String?,
    @SerializedName("legalPersonName")
    var legalPersonName: String?,
    @SerializedName("licenseName")
    var licenseName: Any?,
    @SerializedName("licenseNo")
    var licenseNo: Any?,
    @SerializedName("orderNo")
    var orderNo: String?,
    @SerializedName("orgCode")
    var orgCode: String?,
    @SerializedName("orgId")
    var orgId: String?,
    @SerializedName("orgName")
    var orgName: String?,
    @SerializedName("receiverName")
    var receiverName: String?,
    @SerializedName("receiverNo")
    var receiverNo: String?,
    @SerializedName("remark")
    var remark: Any?,
    @SerializedName("shopId")
    var shopId: String?,
    @SerializedName("tenantId")
    var tenantId: Any?,
    @SerializedName("updateBy")
    var updateBy: Any?,
    @SerializedName("updateTime")
    var updateTime: Any?,
    @SerializedName("userId")
    var userId: Any?
)