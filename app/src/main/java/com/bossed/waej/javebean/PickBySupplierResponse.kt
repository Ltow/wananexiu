package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class PickBySupplierResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("rows")
    var rows: List<PickBySupplierRow>,
    @SerializedName("total")
    var total: Int
)

data class PickBySupplierRow(
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
    var remark: String,
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