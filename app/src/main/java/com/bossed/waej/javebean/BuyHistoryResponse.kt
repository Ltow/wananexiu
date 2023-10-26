package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName

data class BuyHistoryResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Order>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("succ")
    val succ: Boolean
)

data class Order(
    @SerializedName("addTime")
    val addTime: String,
    @SerializedName("adminNote")
    val adminNote: Any,
    @SerializedName("cashMoney")
    val cashMoney: String,
    @SerializedName("commentStatus")
    val commentStatus: Any,
    @SerializedName("couponId")
    val couponId: Any,
    @SerializedName("couponPrice")
    val couponPrice: String,
    @SerializedName("customerId")
    val customerId: String,
    @SerializedName("customerName")
    val customerName: Any,
    @SerializedName("customerPhone")
    val customerPhone: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("integral")
    val integral: Int,
    @SerializedName("integralMoney")
    val integralMoney: String,
    @SerializedName("orderMoney")
    val orderMoney: String,
    @SerializedName("orderSn")
    val orderSn: String,
    @SerializedName("orderStatus")
    val orderStatus: Int,
    @SerializedName("orderType")
    val orderType: Int,
    @SerializedName("payCode")
    val payCode: String,
    @SerializedName("payMethod")
    val payMethod: Int,
    @SerializedName("payName")
    val payName: String,
    @SerializedName("payStatus")
    val payStatus: Int,
    @SerializedName("payTime")
    val payTime: String,
    @SerializedName("productList")
    val productList: List<Product>,
    @SerializedName("remark")
    val remark: Any,
    @SerializedName("shopId")
    val shopId: String,
    @SerializedName("shopName")
    val shopName: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("transactionId")
    val transactionId: String,
    @SerializedName("userNote")
    val userNote: Any
)

data class Product(
    @SerializedName("cashMoney")
    val cashMoney: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("menuIds")
    val menuIds: String,
    @SerializedName("menuName")
    val menuName: String,
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("orderMoney")
    val orderMoney: String,
    @SerializedName("orderSn")
    val orderSn: String,
    @SerializedName("orderType")
    val orderType: Int,
    @SerializedName("packageId")
    val packageId: String,
    @SerializedName("packageName")
    val packageName: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("priceDay")
    val priceDay: String,
    @SerializedName("priceYear")
    val priceYear: String,
    @SerializedName("shopId")
    val shopId: String,
    @SerializedName("siteNum")
    val siteNum: String,
    @SerializedName("termTime")
    val termTime: String
)