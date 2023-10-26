package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class ImportItemResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("rows")
    var rows: List<ImportItemRow>,
    @SerializedName("total")
    var total: Int?
)

data class ImportItemRow(
    @SerializedName("categoryId")
    var categoryId: Int?,
    @SerializedName("categoryName")
    var categoryName: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("logo")
    var logo: String?,
    @SerializedName("originalPrice")
    var originalPrice: String?,
    @SerializedName("virtualPrice")
    var virtualPrice: String?,
    @SerializedName("platformSettlePrice")
    var platformSettlePrice: Int?,
    @SerializedName("priceCash")
    var priceCash: String?,
    @SerializedName("remark")
    var remark: String?,
    @SerializedName("savingCardPrice")
    var savingCardPrice: Int?,
    @SerializedName("serviceItem")
    var serviceItem: String?,
    @SerializedName("isSelect")
    var isSelect: Boolean
)