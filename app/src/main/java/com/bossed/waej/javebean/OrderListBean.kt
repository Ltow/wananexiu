package com.bossed.waej.javebean

data class OrderListBean(
    val code: Int,
    val msg: String,
    val rows: List<OrderRow>,
    val total: Int
)

data class OrderRow(
    val brandLogo: String,
    val brandName: String,
    val carId: Int,
    val carName: String,
    val cardMoney: Double,
    val cardNo: String,
    val costPriceTime: String,
    val createBy: String,
    val createTime: String,
    val customerId: Int,
    val customerName: String,
    val customerPhone: String,
    val discountAmount: Int,
    val dispatchTime: String,
    val finishedTime: String,
    val grossProfitMoney: Double,
    val id: Int,
    val itemCostMoney: Double,
    val itemList: List<HistoryItem>,
    val dispatchVoList: List<ItemDispatchBean>,
    val itemMadeFee: Double,
    val itemMadeMoney: Double,
    val mileage: String,
    val netProfitMoney: Double,
    val numInquiry: Int,
    val numOffer: Int,
    val oilNum: String,
    val oilType: String,
    val orderMoney: Double,
    val orderSn: String,
    val orderStatus: Int,
    val orderType: Int,
    val payStatus: Int,
    val realMoney: Double,
    val receiveBy: String,
    val receiveTime: String,
    val remark: String,
    val shopId: Int,
    val tenantId: Int,
    val updateBy: String,
    val updateTime: String,
    val userCarId: Int,
    val vnCode: String
)

data class HistoryItem(
    val costPrice: Double,
    val grossProfitMoney: Double,
    val id: Int,
    val inquiryList: List<HistoryInquiry>,
    val itemMoney: Double,
    val itemName: String,
    val madeFee: Double,
    val madeFeeRate: Int,
    val madeMoney: Double,
    val madeRate: Int,
    val netProfitMoney: Double,
    val num: Double,
    val oem: String,
    val orderId: Int,
    val orderSn: String,
    val remark: String,
    val serviceFee: Double,
    val shopId: Int,
    val tenantId: Int,
    val unitPrice: Double
)

data class HistoryInquiry(
    val contacts: String,
    val contactsPhone: String,
    val id: Int,
    val itemName: String,
    val num: Double,
    val oem: String,
    val offerMoney: Double,
    val orderId: Int,
    val orderSn: String,
    val orderStatus: Int,
    val remark: String,
    val shopItemId: Int,
    val supplierId: Int,
    val supplierName: String
)