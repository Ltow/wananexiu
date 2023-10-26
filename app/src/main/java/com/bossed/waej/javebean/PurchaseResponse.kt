package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class PurchaseResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: PurchaseData,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class PurchaseData(
    @SerializedName("amount")
    var amount: String,
    @SerializedName("backQuantity")
    var backQuantity: String,
    @SerializedName("balanceAmount")
    var balanceAmount: String,
    @SerializedName("balanceQuantity")
    var balanceQuantity: String,
    @SerializedName("businessOrderNo")
    var businessOrderNo: String,
    @SerializedName("cardNo")
    var cardNo: Any,
    @SerializedName("contacts")
    var contacts: String,
    @SerializedName("contactsPhone")
    var contactsPhone: String,
    @SerializedName("createUser")
    var createUser: Any,
    @SerializedName("deleteTime")
    var deleteTime: Any,
    @SerializedName("detailList")
    var detailList: List<PurchaseDetail>,
    @SerializedName("discount")
    var discount: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("inQuantity")
    var inQuantity: String,
    @SerializedName("moneyOwe")
    var moneyOwe: String,
    @SerializedName("orderSn")
    var orderSn: String,
    @SerializedName("payStatus")
    var payStatus: Int,
    @SerializedName("payment")
    var payment: String,
    @SerializedName("quantity")
    var quantity: String,
    @SerializedName("reason")
    var reason: Any,
    @SerializedName("remark")
    var remark: Any,
    @SerializedName("searchKeywords")
    var searchKeywords: String,
    @SerializedName("settleTime")
    var settleTime: Any,
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
    @SerializedName("type")
    var type: Int,
    @SerializedName("updateBy")
    var updateBy: String
)

data class PurchaseDetail(
    @SerializedName("amount")
    var amount: String,
    @SerializedName("backAmount")
    var backAmount: String,
    @SerializedName("backCost")
    var backCost: String,
    @SerializedName("backQuantity")
    var backQuantity: String,
    @SerializedName("balanceAmount")
    var balanceAmount: String,
    @SerializedName("balanceQuantity")
    var balanceQuantity: String,
    @SerializedName("businessDetail")
    var businessDetail: Any,
    @SerializedName("businessDetailId")
    var businessDetailId: Any,
    @SerializedName("businessOrderSn")
    var businessOrderSn: Any,
    @SerializedName("businessSource")
    var businessSource: Any,
    @SerializedName("cost")
    var cost: String,
    @SerializedName("createUser")
    var createUser: Any,
    @SerializedName("detailList")
    var detailList: Any,
    @SerializedName("extendProp")
    var extendProp: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("inQuantity")
    var inQuantity: String,
    @SerializedName("itemId")
    var itemId: Any,
    @SerializedName("itemName")
    var itemName: Any,
    @SerializedName("orderId")
    var orderId: String,
    @SerializedName("orderSn")
    var orderSn: Any,
    @SerializedName("part")
    var part: Part,
    @SerializedName("partId")
    var partId: String,
    @SerializedName("quantity")
    var quantity: String,
    @SerializedName("remark")
    var remark: Any,
    @SerializedName("shopId")
    var shopId: String,
    @SerializedName("num")
    var num: String?,
)

data class Part(
    @SerializedName("brand")
    var brand: String,
    @SerializedName("code")
    var code: String,
    @SerializedName("createUser")
    var createUser: Any,
    @SerializedName("id")
    var id: String,
    @SerializedName("marketPrice")
    var marketPrice: String,
    @SerializedName("model")
    var model: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("oe")
    var oe: String,
    @SerializedName("partStore")
    var partStore: Any,
    @SerializedName("place")
    var place: Any,
    @SerializedName("purchasePrice")
    var purchasePrice: String,
    @SerializedName("remark")
    var remark: String,
    @SerializedName("status")
    var status: Int,
    @SerializedName("unit")
    var unit: String
)