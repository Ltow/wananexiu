package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class OrderFinishedResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: OrderFinishedData,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class OrderFinishedData(
    @SerializedName("balance")
    var balance: String,
    @SerializedName("balancePay")
    var balancePay: String,
    @SerializedName("brandLogo")
    var brandLogo: Any,
    @SerializedName("brandName")
    var brandName: Any,
    @SerializedName("carCustomerId")
    var carCustomerId: Any,
    @SerializedName("carCustomerName")
    var carCustomerName: Any,
    @SerializedName("carCustomerPhone")
    var carCustomerPhone: Any,
    @SerializedName("carId")
    var carId: Any,
    @SerializedName("carName")
    var carName: String,
    @SerializedName("cardMoney")
    var cardMoney: Any,
    @SerializedName("cardNo")
    var cardNo: String,
    @SerializedName("costPriceTime")
    var costPriceTime: Any,
    @SerializedName("createBy")
    var createBy: Any,
    @SerializedName("createTime")
    var createTime: Any,
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
    var grossProfitMoney: Any,
    @SerializedName("id")
    var id: String,
    @SerializedName("itemCostMoney")
    var itemCostMoney: Any,
    @SerializedName("itemList")
    var itemList: Any,
    @SerializedName("itemMadeFee")
    var itemMadeFee: Any,
    @SerializedName("itemMadeMoney")
    var itemMadeMoney: Any,
    @SerializedName("itemMoney")
    var itemMoney: String,
    @SerializedName("itemServiceFee")
    var itemServiceFee: String,
    @SerializedName("jycarId")
    var jycarId: Any,
    @SerializedName("mileage")
    var mileage: String,
    @SerializedName("moneyOwe")
    var moneyOwe: String,
    @SerializedName("netProfitMoney")
    var netProfitMoney: Any,
    @SerializedName("numInquiry")
    var numInquiry: Any,
    @SerializedName("numItem")
    var numItem: Int,
    @SerializedName("numOffer")
    var numOffer: Any,
    @SerializedName("numPrint")
    var numPrint: Any,
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
    var receiveTime: Any,
    @SerializedName("remark")
    var remark: Any,
    @SerializedName("shopId")
    var shopId: Any,
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