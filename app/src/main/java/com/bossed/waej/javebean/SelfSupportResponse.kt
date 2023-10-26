package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class SelfSupportResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("rows")
    var rows: List<SelfSupportRow>,
    @SerializedName("total")
    var total: Int
)

data class SelfSupportRow(
    @SerializedName("actualSales")
    var actualSales: Int,
    @SerializedName("applicableCartypeId")
    var applicableCartypeId: Any,
    @SerializedName("applicableCartypeName")
    var applicableCartypeName: String,
    @SerializedName("categoryId")
    var categoryId: Int,
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
    @SerializedName("praise")
    var praise: String,
    @SerializedName("mainPicture")
    var mainPicture: String,
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
    var status: Int,
    @SerializedName("itemType")
    var itemType: Int,
    @SerializedName("publicStatus")
    var publicStatus: Int,
    @SerializedName("stock")
    var stock: Any,
    @SerializedName("summary")
    var summary: String,
    @SerializedName("virtualPrice")
    var virtualPrice: String,
    @SerializedName("memberPrice")
    var memberPrice: String,
    @SerializedName("publicPrice")
    var publicPrice: String,
    @SerializedName("publicVirtualPrice")
    var publicVirtualPrice: String,
    @SerializedName("virtualSales")
    var virtualSales: Int
)