package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class PaymentSearchInfoResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: PaymentSearchInfoData,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class PaymentSearchInfoData(
    @SerializedName("detailList")
    var detailList: List<Detail>,
    @SerializedName("journalList")
    var journalList: List<Journal>,
    @SerializedName("order")
    var order: PayOrder
)

data class Detail(
    @SerializedName("billTime")
    var billTime: String,
    @SerializedName("businessOrderSn")
    var businessOrderSn: String,
    @SerializedName("createUser")
    var createUser: Any,
    @SerializedName("discount")
    var discount: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("model")
    var model: Any,
    @SerializedName("payable")
    var payable: String,
    @SerializedName("payment")
    var payment: String,
    @SerializedName("paymentOrderSn")
    var paymentOrderSn: String,
    @SerializedName("remaining")
    var remaining: String,
    @SerializedName("remark")
    var remark: Any,
    @SerializedName("shopId")
    var shopId: String,
    @SerializedName("status")
    var status: Int,
    @SerializedName("summary")
    var summary: String,
    @SerializedName("updateBy")
    var updateBy: String,
    @SerializedName("updateTime")
    var updateTime: String,
    @SerializedName("updateUser")
    var updateUser: Any,
    @SerializedName("purchaseOrder")
    var purchaseOrder: PurchaseOrder
)

data class Journal(
    @SerializedName("accountId")
    var accountId: String,
    @SerializedName("accountName")
    var accountName: String,
    @SerializedName("balance")
    var balance: String,
    @SerializedName("billTime")
    var billTime: String,
    @SerializedName("billType")
    var billType: Int,
    @SerializedName("createBy")
    var createBy: String,
    @SerializedName("createUser")
    var createUser: Any,
    @SerializedName("expenseId")
    var expenseId: Any,
    @SerializedName("expenseName")
    var expenseName: Any,
    @SerializedName("id")
    var id: String,
    @SerializedName("methodId")
    var methodId: String,
    @SerializedName("methodName")
    var methodName: String,
    @SerializedName("money")
    var money: String,
    @SerializedName("moneyPay")
    var moneyPay: String,
    @SerializedName("payAccountId")
    var payAccountId: Any,
    @SerializedName("payAccountName")
    var payAccountName: Any,
    @SerializedName("payMethodId")
    var payMethodId: Any,
    @SerializedName("payMethodName")
    var payMethodName: Any,
    @SerializedName("remark")
    var remark: Any,
    @SerializedName("shopId")
    var shopId: String,
    @SerializedName("status")
    var status: Int,
    @SerializedName("summary")
    var summary: String,
    @SerializedName("type")
    var type: Int,
    @SerializedName("updateBy")
    var updateBy: String,
    @SerializedName("updateTime")
    var updateTime: String,
    @SerializedName("updateUser")
    var updateUser: Any
)

data class PayOrder(
    @SerializedName("balance")
    var balance: String,
    @SerializedName("billTime")
    var billTime: String,
    @SerializedName("billType")
    var billType: Any,
    @SerializedName("contacts")
    var contacts: String,
    @SerializedName("contactsPhone")
    var contactsPhone: String,
    @SerializedName("createUser")
    var createUser: Any,
    @SerializedName("discount")
    var discount: String,
    @SerializedName("payable")
    var payable: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("model")
    var model: String,
    @SerializedName("money")
    var money: String,
    @SerializedName("moneyOwe")
    var moneyOwe: String,
    @SerializedName("moneyPay")
    var moneyPay: String,
    @SerializedName("orderSn")
    var orderSn: String,
    @SerializedName("remaining")
    var remaining: String,
    @SerializedName("remark")
    var remark: Any,
    @SerializedName("shopId")
    var shopId: String,
    @SerializedName("status")
    var status: Int,
    @SerializedName("summary")
    var summary: String,
    @SerializedName("supplierId")
    var supplierId: String,
    @SerializedName("supplierName")
    var supplierName: String,
    @SerializedName("updateBy")
    var updateBy: String,
    @SerializedName("updateUser")
    var updateUser: Any
)

data class PurchaseOrder(
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
    @SerializedName("type")
    var type: Int,
    @SerializedName("isSelected")
    var isSelected: Boolean,
    @SerializedName("updateBy")
    var updateBy: String
)