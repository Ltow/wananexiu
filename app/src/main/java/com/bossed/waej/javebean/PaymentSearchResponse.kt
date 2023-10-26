package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class PaymentSearchResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("rows")
    var rows: List<PaymentSearchRow>,
    @SerializedName("total")
    var total: Int
)

data class PaymentSearchRow(
    @SerializedName("accountId")
    var accountId: Any,
    @SerializedName("accountName")
    var accountName: Any,
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
    @SerializedName("id")
    var id: String,
    @SerializedName("methodId")
    var methodId: Any,
    @SerializedName("methodName")
    var methodName: Any,
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