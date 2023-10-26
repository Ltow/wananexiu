package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class CollectionInfoResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: CollectionInfoData,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("succ")
    val succ: Boolean
)

data class CollectionInfoData(
    @SerializedName("order")
    var order: CollectionOrder,
    @SerializedName("detailList")
    val detailList: List<CollectionDetail>,
    @SerializedName("journalList")
    val journalList: List<Journal>
)

data class CollectionDetail(
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
    @SerializedName("workOrder")
    var workOrder: CollectionDetailWorkOrder
)

data class CollectionDetailWorkOrder(
    @SerializedName("brandLogo")
    var brandLogo: String,
    @SerializedName("brandName")
    var brandName: String,
    @SerializedName("carCustomerId")
    var carCustomerId: Any,
    @SerializedName("carCustomerName")
    var carCustomerName: String,
    @SerializedName("carCustomerPhone")
    var carCustomerPhone: String,
    @SerializedName("carId")
    var carId: Any,
    @SerializedName("carName")
    var carName: String,
    @SerializedName("cardMoney")
    var cardMoney: String,
    @SerializedName("cardNo")
    var cardNo: String,
    @SerializedName("costPriceTime")
    var costPriceTime: Any,
    @SerializedName("createBy")
    var createBy: String,
    @SerializedName("createTime")
    var createTime: String,
    @SerializedName("customerId")
    var customerId: String,
    @SerializedName("customerName")
    var customerName: String,
    @SerializedName("customerPhone")
    var customerPhone: String,
    @SerializedName("discountAmount")
    var discountAmount: String,
    @SerializedName("dispatchTime")
    var dispatchTime: Any,
    @SerializedName("dispatchVoList")
    var dispatchVoList: Any,
    @SerializedName("finishedTime")
    var finishedTime: String,
    @SerializedName("grossProfitMoney")
    var grossProfitMoney: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("itemCostMoney")
    var itemCostMoney: String,
    @SerializedName("itemList")
    var itemList: Any,
    @SerializedName("itemMadeFee")
    var itemMadeFee: String,
    @SerializedName("itemMadeMoney")
    var itemMadeMoney: String,
    @SerializedName("itemMoney")
    var itemMoney: String,
    @SerializedName("itemServiceFee")
    var itemServiceFee: String,
    @SerializedName("jycarId")
    var jycarId: String,
    @SerializedName("mileage")
    var mileage: String,
    @SerializedName("moneyOwe")
    var moneyOwe: String,
    @SerializedName("netProfitMoney")
    var netProfitMoney: String,
    @SerializedName("numInquiry")
    var numInquiry: Int,
    @SerializedName("numItem")
    var numItem: Int,
    @SerializedName("numOffer")
    var numOffer: Int,
    @SerializedName("oilNum")
    var oilNum: Any,
    @SerializedName("oilType")
    var oilType: Any,
    @SerializedName("orderMoney")
    var orderMoney: String,
    @SerializedName("orderSn")
    var orderSn: String,
    @SerializedName("orderStatus")
    var orderStatus: Int,
    @SerializedName("orderType")
    var orderType: Int,
    @SerializedName("payStatus")
    var payStatus: Int,
    @SerializedName("realMoney")
    var realMoney: String,
    @SerializedName("receiveBy")
    var receiveBy: String,
    @SerializedName("receiveTime")
    var receiveTime: String,
    @SerializedName("remark")
    var remark: String,
    @SerializedName("shopId")
    var shopId: String,
    @SerializedName("supplierList")
    var supplierList: Any,
    @SerializedName("updateBy")
    var updateBy: String,
    @SerializedName("updateTime")
    var updateTime: String,
    @SerializedName("userCarId")
    var userCarId: String,
    @SerializedName("vnCode")
    var vnCode: String
)
data class CollectionOrder(
    @SerializedName("balance")
    var balance: String,
    @SerializedName("billTime")
    var billTime: String,
    @SerializedName("createUser")
    var createUser: Any,
    @SerializedName("customerId")
    var customerId: String,
    @SerializedName("customerName")
    var customerName: String,
    @SerializedName("customerPhone")
    var customerPhone: String,
    @SerializedName("discount")
    var discount: String,
    @SerializedName("payable")
    var payable: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("model")
    var model: Any,
    @SerializedName("money")
    var money: String,
    @SerializedName("moneyOwe")
    var moneyOwe: String,
    @SerializedName("moneyPay")
    var moneyPay: String,
    @SerializedName("orderSn")
    var orderSn: String,
    @SerializedName("remaining")
    var remaining: Any,
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
    var updateUser: Any
)