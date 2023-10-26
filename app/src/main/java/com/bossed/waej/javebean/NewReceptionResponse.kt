package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class NewReceptionResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: NewReceptionData,
    @SerializedName("msg")
    val msg: String
)

data class NewReceptionData(
    @SerializedName("brandLogo")
    val brandLogo: Any,
    @SerializedName("brandName")
    val brandName: Any,
    @SerializedName("carCustomerId")
    val carCustomerId: Any,
    @SerializedName("carCustomerName")
    val carCustomerName: String,
    @SerializedName("carCustomerPhone")
    val carCustomerPhone: String,
    @SerializedName("carId")
    val carId: Any,
    @SerializedName("carName")
    val carName: String,
    @SerializedName("cardMoney")
    val cardMoney: Any,
    @SerializedName("cardNo")
    val cardNo: String,
    @SerializedName("costPriceTime")
    val costPriceTime: Any,
    @SerializedName("createBy")
    val createBy: String,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("customerId")
    val customerId: Any,
    @SerializedName("customerName")
    val customerName: String,
    @SerializedName("customerPhone")
    val customerPhone: String,
    @SerializedName("delFlag")
    val delFlag: Any,
    @SerializedName("discountAmount")
    val discountAmount: String,
    @SerializedName("dispatchTime")
    val dispatchTime: Any,
    @SerializedName("finishedTime")
    val finishedTime: Any,
    @SerializedName("grossProfitMoney")
    val grossProfitMoney: Any,
    @SerializedName("id")
    val id: String,
    @SerializedName("isFinished")
    val isFinished: Boolean,
    @SerializedName("isInShop")
    val isInShop: Boolean,
    @SerializedName("isOnShop")
    val isOnShop: Boolean,
    @SerializedName("itemCostMoney")
    val itemCostMoney: Any,
    @SerializedName("itemMadeFee")
    val itemMadeFee: Any,
    @SerializedName("itemMadeMoney")
    val itemMadeMoney: Any,
    @SerializedName("itemMoney")
    val itemMoney: Any,
    @SerializedName("itemServiceFee")
    val itemServiceFee: Any,
    @SerializedName("mileage")
    val mileage: String,
    @SerializedName("netProfitMoney")
    val netProfitMoney: Any,
    @SerializedName("numInquiry")
    val numInquiry: Any,
    @SerializedName("numItem")
    val numItem: Any,
    @SerializedName("numOffer")
    val numOffer: Any,
    @SerializedName("oilNum")
    val oilNum: Any,
    @SerializedName("oilType")
    val oilType: Any,
    @SerializedName("orderMoney")
    val orderMoney: Any,
    @SerializedName("orderSn")
    val orderSn: String,
    @SerializedName("orderStatus")
    val orderStatus: Int,
    @SerializedName("orderType")
    val orderType: Int,
    @SerializedName("payStatus")
    val payStatus: Any,
    @SerializedName("realMoney")
    val realMoney: String,
    @SerializedName("receiveBy")
    val receiveBy: String,
    @SerializedName("receiveTime")
    val receiveTime: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("searchValue")
    val searchValue: Any,
    @SerializedName("shopId")
    val shopId: String,
    @SerializedName("tenantId")
    val tenantId: Any,
    @SerializedName("updateBy")
    val updateBy: String,
    @SerializedName("updateTime")
    val updateTime: String,
    @SerializedName("userCarId")
    val userCarId: String,
    @SerializedName("version")
    val version: Any,
    @SerializedName("vnCode")
    val vnCode: String
)
