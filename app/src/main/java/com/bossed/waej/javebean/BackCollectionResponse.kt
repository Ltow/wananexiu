package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class BackCollectionResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var `data`: BackCollectionData?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("succ")
    var succ: Boolean?
)

data class BackCollectionData(
    @SerializedName("list")
    var list: Any?,
    @SerializedName("moneyOwe")
    var moneyOwe: String?,
    @SerializedName("purchaseOrderList")
    var purchaseOrderList: List<PurchaseOrder>
)
