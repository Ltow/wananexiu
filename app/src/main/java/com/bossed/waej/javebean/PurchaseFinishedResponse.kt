package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class PurchaseFinishedResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: PurchaseFinishedData,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class PurchaseFinishedData(
    @SerializedName("amount")
    var amount: String,
    @SerializedName("backQuantity")
    var backQuantity: Any,
    @SerializedName("balanceAmount")
    var balanceAmount: Any,
    @SerializedName("balanceQuantity")
    var balanceQuantity: Any,
    @SerializedName("businessOrderNo")
    var businessOrderNo: Any,
    @SerializedName("cardNo")
    var cardNo: Any,
    @SerializedName("contacts")
    var contacts: String,
    @SerializedName("contactsPhone")
    var contactsPhone: String,
    @SerializedName("createBy")
    var createBy: String,
    @SerializedName("createDept")
    var createDept: String,
    @SerializedName("createTime")
    var createTime: String,
    @SerializedName("createUser")
    var createUser: Any,
    @SerializedName("delFlag")
    var delFlag: Any,
    @SerializedName("deleteTime")
    var deleteTime: String,
    @SerializedName("discount")
    var discount: Any,
    @SerializedName("id")
    var id: String,
    @SerializedName("inQuantity")
    var inQuantity: String,
    @SerializedName("moneyOwe")
    var moneyOwe: String,
    @SerializedName("numPrint")
    var numPrint: Any,
    @SerializedName("orderPrefix")
    var orderPrefix: String,
    @SerializedName("orderSn")
    var orderSn: String,
    @SerializedName("payStatus")
    var payStatus: Int,
    @SerializedName("payment")
    var payment: Any,
    @SerializedName("quantity")
    var quantity: String,
    @SerializedName("reason")
    var reason: Any,
    @SerializedName("remark")
    var remark: Any,
    @SerializedName("searchKeywords")
    var searchKeywords: String,
    @SerializedName("settleTime")
    var settleTime: String,
    @SerializedName("shopId")
    var shopId: String,
    @SerializedName("status")
    var status: Int,
    @SerializedName("storeStatus")
    var storeStatus: Int,
    @SerializedName("supplierId")
    var supplierId: String,
    @SerializedName("supplierName")
    var supplierName: String,
    @SerializedName("tenantId")
    var tenantId: Any,
    @SerializedName("type")
    var type: Int,
    @SerializedName("updateBy")
    var updateBy: String,
    @SerializedName("updateTime")
    var updateTime: String,
    @SerializedName("version")
    var version: Any
)