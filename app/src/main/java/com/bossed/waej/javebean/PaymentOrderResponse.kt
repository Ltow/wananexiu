package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class PaymentOrderResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: PaymentOrderData,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class PaymentOrderData(
    @SerializedName("list")
    var list: Any,
    @SerializedName("moneyOwe")
    var moneyOwe: String,
    @SerializedName("purchaseOrderList")
    var purchaseOrderList: List<PurchaseOrderRow>
)

data class PurchaseOrderRow(
    @SerializedName("amount")
    var amount: String,
    @SerializedName("backQuantity")
    var backQuantity: Int,
    @SerializedName("balanceAmount")
    var balanceAmount: String,
    @SerializedName("balanceQuantity")
    var balanceQuantity: Int,
    @SerializedName("businessOrderNo")
    var businessOrderNo: String,
    @SerializedName("cardNo")
    var cardNo: String,
    @SerializedName("contacts")
    var contacts: String,
    @SerializedName("contactsPhone")
    var contactsPhone: String,
    @SerializedName("createUser")
    var createUser: Any,
    @SerializedName("deleteTime")
    var deleteTime: Any,
    @SerializedName("detailList")
    var detailList: Any,
    @SerializedName("discount")
    var discount: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("inQuantity")
    var inQuantity: Int,
    @SerializedName("moneyOwe")
    var moneyOwe: String,
    @SerializedName("orderSn")
    var orderSn: String,
    @SerializedName("payStatus")
    var payStatus: Int,
    @SerializedName("payment")
    var payment: String,
    @SerializedName("quantity")
    var quantity: Int,
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
    @SerializedName("updateBy")
    var updateBy: String,
    @SerializedName("isSelected")
    var isSelected: Boolean,
    @SerializedName("type")
    var type: Int
)