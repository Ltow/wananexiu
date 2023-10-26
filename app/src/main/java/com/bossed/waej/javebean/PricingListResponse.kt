package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class PricingListResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("rows")
    val rows: List<PricingListRow>,
    @SerializedName("total")
    val total: Int
)

data class PricingListRow(
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
    val cardMoney: String,
    @SerializedName("cardNo")
    val cardNo: String,
    @SerializedName("costPriceTime")
    val costPriceTime: Any,
    @SerializedName("createBy")
    val createBy: String,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("customerId")
    val customerId: String,
    @SerializedName("customerName")
    val customerName: String,
    @SerializedName("customerPhone")
    val customerPhone: String,
    @SerializedName("discountAmount")
    val discountAmount: String,
    @SerializedName("dispatchTime")
    val dispatchTime: Any,
    @SerializedName("dispatchVoList")
    val dispatchVoList: Any,
    @SerializedName("finishedTime")
    val finishedTime: String,
    @SerializedName("grossProfitMoney")
    val grossProfitMoney: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("itemCostMoney")
    val itemCostMoney: String,
    @SerializedName("itemList")
    val itemList: Any,
    @SerializedName("itemMadeFee")
    val itemMadeFee: String,
    @SerializedName("itemMadeMoney")
    val itemMadeMoney: String,
    @SerializedName("itemMoney")
    val itemMoney: String,
    @SerializedName("itemServiceFee")
    val itemServiceFee: String,
    @SerializedName("jycarId")
    val jycarId: Any,
    @SerializedName("mileage")
    val mileage: String,
    @SerializedName("moneyOwe")
    val moneyOwe: String,
    @SerializedName("netProfitMoney")
    val netProfitMoney: String,
    @SerializedName("numInquiry")
    val numInquiry: Int,
    @SerializedName("numItem")
    val numItem: Int,
    @SerializedName("numOffer")
    val numOffer: Int,
    @SerializedName("oilNum")
    val oilNum: Any,
    @SerializedName("oilType")
    val oilType: Any,
    @SerializedName("orderMoney")
    val orderMoney: String,
    @SerializedName("orderSn")
    val orderSn: String,
    @SerializedName("orderStatus")
    val orderStatus: Int,
    @SerializedName("orderType")
    val orderType: Int,
    @SerializedName("payStatus")
    val payStatus: Int,
    @SerializedName("realMoney")
    val realMoney: String,
    @SerializedName("receiveBy")
    val receiveBy: String,
    @SerializedName("receiveTime")
    val receiveTime: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("shopId")
    val shopId: String,
    @SerializedName("supplierList")
    val supplierList: Any,
    @SerializedName("updateBy")
    val updateBy: String,
    @SerializedName("updateTime")
    val updateTime: String,
    @SerializedName("userCarId")
    val userCarId: String,
    @SerializedName("vnCode")
    val vnCode: String
)