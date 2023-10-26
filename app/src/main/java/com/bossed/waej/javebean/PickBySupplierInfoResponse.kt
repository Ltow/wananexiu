package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class PickBySupplierInfoResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: PickBySupplierInfoData,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class PickBySupplierInfoData(
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
    var detailList: List<PickBySupplierInfoDetail>,
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
    @SerializedName("type")
    var type: Int
)

data class PickBySupplierInfoDetail(
    @SerializedName("amount")
    var amount: Any,
    @SerializedName("backAmount")
    var backAmount: Any,
    @SerializedName("backCost")
    var backCost: Any,
    @SerializedName("backQuantity")
    var backQuantity: Any,
    @SerializedName("balanceAmount")
    var balanceAmount: Any,
    @SerializedName("balanceQuantity")
    var balanceQuantity: Any,
    @SerializedName("cost")
    var cost: Any,
    @SerializedName("createUser")
    var createUser: Any,
    @SerializedName("detailList")
    var detailList: List<DetailX>?,
    @SerializedName("id")
    var id: Any,
    @SerializedName("inQuantity")
    var inQuantity: Any,
    @SerializedName("itemId")
    var itemId: String,
    @SerializedName("itemName")
    var itemName: String,
    @SerializedName("orderId")
    var orderId: Any,
    @SerializedName("orderSn")
    var orderSn: Any,
    @SerializedName("partId")
    var partId: Any,
    @SerializedName("quantity")
    var quantity: Any,
    @SerializedName("remark")
    var remark: Any,
    @SerializedName("shopId")
    var shopId: Any
)

data class DetailX(
    @SerializedName("amount")
    var amount: String,
    @SerializedName("backAmount")
    var backAmount: String,
    @SerializedName("backCost")
    var backCost: String,
    @SerializedName("backQuantity")
    var backQuantity: Int,
    @SerializedName("balanceAmount")
    var balanceAmount: String,
    @SerializedName("balanceQuantity")
    var balanceQuantity: Int,
    @SerializedName("cost")
    var cost: String,
    @SerializedName("createUser")
    var createUser: Any,
    @SerializedName("detailList")
    var detailList: Any,
    @SerializedName("id")
    var id: String,
    @SerializedName("inQuantity")
    var inQuantity: Int,
    @SerializedName("itemId")
    var itemId: String,
    @SerializedName("itemName")
    var itemName: String,
    @SerializedName("orderId")
    var orderId: String,
    @SerializedName("orderSn")
    var orderSn: String,
    @SerializedName("partId")
    var partId: Any,
    @SerializedName("quantity")
    var quantity: Int,
    @SerializedName("remark")
    var remark: Any,
    @SerializedName("shopId")
    var shopId: String
)