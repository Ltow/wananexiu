package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class SelfPackageInfoResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var `data`: SelfPackageInfoData,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("succ")
    var succ: Boolean?
)

data class SelfPackageInfoData(
    @SerializedName("applicableCartypeId")
    var applicableCartypeId: Any?,
    @SerializedName("applicableCartypeName")
    var applicableCartypeName: Any?,
    @SerializedName("associatedProduct")
    var associatedProduct: Any?,
    @SerializedName("briefIntroduction")
    var briefIntroduction: String?,
    @SerializedName("cateId")
    var cateId: Int?,
    @SerializedName("cateName")
    var cateName: String?,
    @SerializedName("costPrice")
    var costPrice: String?,
    @SerializedName("createBy")
    var createBy: String?,
    @SerializedName("createTime")
    var createTime: String?,
    @SerializedName("details")
    var details: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("isHot")
    var isHot: Int?,
    @SerializedName("madeFee")
    var madeFee: String?,
    @SerializedName("madeMoney")
    var madeMoney: String?,
    @SerializedName("mainPicture")
    var mainPicture: String?,
    @SerializedName("marketPrice")
    var marketPrice: String?,
    @SerializedName("memberPrice")
    var memberPrice: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("picture")
    var picture: String?,
    @SerializedName("platId")
    var platId: Any?,
    @SerializedName("platName")
    var platName: Any?,
    @SerializedName("platformSettlePrice")
    var platformSettlePrice: String?,
    @SerializedName("remark")
    var remark: Any?,
    @SerializedName("serviceFee")
    var serviceFee: String?,
    @SerializedName("shopId")
    var shopId: String?,
    @SerializedName("status")
    var status: Int?,
    @SerializedName("superCateId")
    var superCateId: Any?,
    @SerializedName("tenantId")
    var tenantId: String?,
    @SerializedName("updateBy")
    var updateBy: String?,
    @SerializedName("updateTime")
    var updateTime: String?,
    @SerializedName("virtualPrice")
    var virtualPrice: String?
)