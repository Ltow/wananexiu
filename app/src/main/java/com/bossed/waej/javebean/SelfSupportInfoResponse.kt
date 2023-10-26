package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class SelfSupportInfoResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: SelfSupportInfoData,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class SelfSupportInfoData(
    @SerializedName("actualSales")
    var actualSales: Int,
    @SerializedName("applicableCartypeId")
    var applicableCartypeId: Any,
    @SerializedName("applicableCartypeName")
    var applicableCartypeName: String,
    @SerializedName("categoryId")
    var categoryId: Int,
    @SerializedName("categoryName")
    var categoryName: String,
    @SerializedName("costPrice")
    var costPrice: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("marketPrice")
    var marketPrice: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("platformSettlePrice")
    var platformSettlePrice: String,
    @SerializedName("memberPrice")
    var memberPrice: String,
    @SerializedName("praise")
    var praise: String,
    @SerializedName("productType")
    var productType: Int,
    @SerializedName("reason")
    var reason: Any,
    @SerializedName("sellCounts")
    var sellCounts: Any,
    @SerializedName("shopId")
    var shopId: Any,
    @SerializedName("specName")
    var specName: Any,
    @SerializedName("status")
    var status: Int?,
    @SerializedName("stock")
    var stock: Any,
    @SerializedName("summary")
    var summary: String,
    @SerializedName("virtualPrice")
    var virtualPrice: String,
    @SerializedName("mainPicture")
    var mainPicture: String,
    @SerializedName("picture")
    var picture: String,
    @SerializedName("virtualSales")
    var virtualSales: Int
)