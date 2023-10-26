package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class ReceivableInfoResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: ReceivableInfoData,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("succ")
    val succ: Boolean
)

data class ReceivableInfoData(
    @SerializedName("balance")
    val balance: String,
    @SerializedName("customerBillList")
    val customerBillList: CustomerBillList,
    @SerializedName("discount")
    val discount: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("ledgerList")
    val ledgerList: Any,
    @SerializedName("moneyAdd")
    val moneyAdd: String,
    @SerializedName("moneyOwe")
    val moneyOwe: String,
    @SerializedName("moneyPay")
    val moneyPay: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("supplierBillList")
    val supplierBillList: SupplierBillList,
    @SerializedName("supplierName")
    val supplierName: String
)

data class CustomerBillList(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("rows")
    val rows: List<CustomerBillRow>,
    @SerializedName("total")
    val total: Int
)
data class SupplierBillList(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("rows")
    val rows: List<SupplierBillRow>,
    @SerializedName("total")
    val total: Int
)

data class CustomerBillRow(
    @SerializedName("balance")
    val balance: String,
    @SerializedName("billTime")
    val billTime: String,
    @SerializedName("createUser")
    val createUser: Any,
    @SerializedName("customerId")
    val customerId: String,
    @SerializedName("discount")
    val discount: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("model")
    val model: Any,
    @SerializedName("moneyOwe")
    val moneyOwe: String,
    @SerializedName("moneyPay")
    val moneyPay: String,
    @SerializedName("money")
    val money: String,
    @SerializedName("orderSn")
    val orderSn: String,
    @SerializedName("remaining")
    val remaining: Any,
    @SerializedName("remark")
    val remark: Any,
    @SerializedName("shopId")
    val shopId: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("updateBy")
    val updateBy: String,
    @SerializedName("updateTime")
    val updateTime: String,
    @SerializedName("businessOrderSn")
    val businessOrderSn: String,
    @SerializedName("updateUser")
    val updateUser: Any
)
data class SupplierBillRow(
    @SerializedName("accountId")
    val accountId: Int,
    @SerializedName("accountName")
    val accountName: String,
    @SerializedName("balance")
    val balance: String,
    @SerializedName("billTime")
    val billTime: String,
    @SerializedName("billType")
    val billType: Int,
    @SerializedName("createUser")
    val createUser: Int,
    @SerializedName("discount")
    val discount: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("methodId")
    val methodId: Int,
    @SerializedName("methodName")
    val methodName: String,
    @SerializedName("model")
    val model: String,
    @SerializedName("moneyOwe")
    val moneyOwe: String,
    @SerializedName("money")
    val money: String,
    @SerializedName("moneyPay")
    val moneyPay: String,
    @SerializedName("orderSn")
    val orderSn: String,
    @SerializedName("remaining")
    val remaining: Int,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("shopId")
    val shopId: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("businessOrderSn")
    val businessOrderSn: String,
    @SerializedName("supplierId")
    val supplierId: Int,
    @SerializedName("updateUser")
    val updateUser: Int
)