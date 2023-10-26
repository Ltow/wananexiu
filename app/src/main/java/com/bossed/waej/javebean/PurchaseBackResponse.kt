package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class PurchaseBackResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: PurchaseBackData,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class PurchaseBackData(
    @SerializedName("amount")
    var amount: String,
    @SerializedName("backAmount")
    var backAmount: Any,
    @SerializedName("backQuantity")
    var backQuantity: String,
    @SerializedName("balanceAmount")
    var balanceAmount: Any,
    @SerializedName("balanceQuantity")
    var balanceQuantity: Any,
    @SerializedName("businessOrderMoney")
    var businessOrderMoney: Any,
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
    var createUser: String,
    @SerializedName("delFlag")
    var delFlag: Any,
    @SerializedName("deleteTime")
    var deleteTime: String,
    @SerializedName("discount")
    var discount: Any,
    @SerializedName("id")
    var id: String,
    @SerializedName("inQuantity")
    var inQuantity: Any,
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
    var storeStatus: Any,
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