package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class PickByCustomerResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("rows")
    var rows: List<PickByCustomerRow>,
    @SerializedName("total")
    var total: Int
)

data class PickByCustomerRow(
    @SerializedName("brandLogo")
    var brandLogo: Any,
    @SerializedName("brandName")
    var brandName: Any,
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
    var costPriceTime: String,
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
    var jycarId: Any,
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