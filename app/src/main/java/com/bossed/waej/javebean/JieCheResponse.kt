package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class JieCheResponse(
    val code: Int,
    val `data`: JieCheData,
    val msg: String
)

data class JieCheData(
    val brandLogo: String,
    val brandName: String,
    val carId: Int,
    val carName: String,
    val jycarId: String,
    val cardMoney: Double,
    val cardNo: String,
    val costPriceTime: String,
    val createBy: String,
    val createTime: String,
    val customerId: Int,
    val customerName: String,
    val customerPhone: String,
    val discountAmount: Double,
    val dispatchTime: String,
    val finishedTime: String,
    val grossProfitMoney: String,
    val id: Int,
    val itemCostMoney: Double,
    val itemMoney: Double,
    val itemList: List<ItemBean>?,
    val itemMadeFee: Double,
    val itemServiceFee: Double,
    val itemMadeMoney: Double,
    val mileage: String,
    val netProfitMoney: Double,
    val oilNum: String,
    val oilType: String,
    val orderMoney: Double,
    val orderSn: String,
    val orderStatus: Int,
    val orderType: Int,
    val payStatus: Int,
    val realMoney: Double,
    val moneyOwe: Double,
    val remark: String,
    val shopId: Int,
    val tenantId: Int,
    val updateBy: String,
    val updateTime: String,
    val userCarId: Int,
    val vnCode: String,
    val receiveBy: String,
    val carCustomerName: String,
    val carCustomerPhone: String,
    val carCustomerId: Int,
    val billVoList: BillVoList,
    val receiveTime: String,
    val dispatchVoList: List<ItemDispatchBean>
)

data class BillVoList(
    @SerializedName("balance")
    var balance: Any?,
    @SerializedName("balancePay")
    var balancePay: String?,
    @SerializedName("billTime")
    var billTime: Any?,
    @SerializedName("businessOrderSn")
    var businessOrderSn: Any?,
    @SerializedName("createUser")
    var createUser: Any?,
    @SerializedName("customerId")
    var customerId: Any?,
    @SerializedName("customerName")
    var customerName: Any?,
    @SerializedName("customerPhone")
    var customerPhone: Any?,
    @SerializedName("discount")
    var discount: String?,
    @SerializedName("id")
    var id: Any?,
    @SerializedName("model")
    var model: Any?,
    @SerializedName("money")
    var money: String?,
    @SerializedName("moneyOwe")
    var moneyOwe: String?,
    @SerializedName("moneyPay")
    var moneyPay: String?,
    @SerializedName("orderSn")
    var orderSn: Any?,
    @SerializedName("payable")
    var payable: String?,
    @SerializedName("received")
    var received: String?,
    @SerializedName("remaining")
    var remaining: Any?,
    @SerializedName("remark")
    var remark: Any?,
    @SerializedName("shopId")
    var shopId: Any?,
    @SerializedName("status")
    var status: Any?,
    @SerializedName("summary")
    var summary: Any?,
    @SerializedName("updateBy")
    var updateBy: Any?,
    @SerializedName("updateTime")
    var updateTime: Any?,
    @SerializedName("updateUser")
    var updateUser: Any?
)

data class JieCheItem(
    val costPrice: Double,
    val grossProfitMoney: Double,
    val id: String,
    var itemMoney: Double,
    var itemName: String,
    var madeFee: Double,
    var madeFeeRate: Int,
    var madeMoney: Double,
    var madeRate: Int,
    var netProfitMoney: Double,
    var num: Double,
    var orderId: Int,
    var orderSn: String,
    var oem: String,
    var remark: String,
    var serviceFee: Double,
    var shopId: Int?,
    var tenantId: Int,
    var unitPrice: Double,
    var itemDispatchList: MutableList<ItemDispatchBean>,
    var supplyPriceList: List<SupplyPriceBean>
)

open class SupplyPriceBean(
    @SerializedName("contacts")
    val contacts: String,
    @SerializedName("contacts2")
    val contacts2: String,
    @SerializedName("contacts2Phone")
    val contacts2Phone: String,
    @SerializedName("contactsPhone")
    val contactsPhone: String,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("itemId")
    val itemId: Int?,
    @SerializedName("itemName")
    val itemName: String,
    @SerializedName("moneyOwe")
    val moneyOwe: Double?,
    @SerializedName("name")
    var name: String,
    @SerializedName("num")
    var num: Double?,
    @SerializedName("orderId")
    val orderId: Int?,
    @SerializedName("partId")
    var partId: Int?,
    @SerializedName("partName")
    var partName: String,
    @SerializedName("payStatus")
    val payStatus: Int?,
    @SerializedName("remark")
    var remark: String,
    @SerializedName("shopId")
    val shopId: Int?,
    @SerializedName("supplierId")
    var supplierId: Int?,
    @SerializedName("supplyPrice")
    var supplyPrice: Double?,
    @SerializedName("unitPrice")
    var unitPrice: Double?
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        null,
        null,
        "",
        null,
        "",
        null,
        null,
        null,
        "",
        null,
        "",
        null,
        null,
        null,
        null
    )
}

data class ItemDispatchBean(
    var createBy: String?,
    var createTime: String?,
    var id: String?,
    var itemId: String?,
    var itemName: String?,
    var madeFee: Double?,
    var madeFeeRate: Double?,
    var madeMoney: Double?,
    var madeRate: Double?,
    var orderId: Int?,
    var remark: String?,
    var searchValue: String?,
    var shopId: String?,
    var staffId: Int?,
    var staffName: String?,
    var staffPhone: String?,
    var subtotal: String?,
    var tenantId: String?,
    var updateBy: String?,
    var type: String?,
    var updateTime: String?
) {
    constructor() : this(
        null, null, null, null, null, null,
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null
    )
}
