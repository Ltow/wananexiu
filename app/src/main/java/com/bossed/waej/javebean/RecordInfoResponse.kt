package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class RecordInfoResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var `data`: RecordInfoData?,
    @SerializedName("msg")
    var msg: String?
)

data class RecordInfoData(
    @SerializedName("beginTime")
    var beginTime: String?,
    @SerializedName("cardNo")
    var cardNo: String?,
    @SerializedName("createdTime")
    var createdTime: String?,
    @SerializedName("driverId")
    var driverId: Any?,
    @SerializedName("driverName")
    var driverName: String?,
    @SerializedName("driverPhone")
    var driverPhone: String?,
    @SerializedName("endTime")
    var endTime: String?,
    @SerializedName("extraStr")
    var extraStr: Any?,
    @SerializedName("face")
    var face: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("nickname")
    var nickname: String?,
    @SerializedName("operator")
    var `operator`: Any?,
    @SerializedName("operatorId")
    var operatorId: Any?,
    @SerializedName("order")
    var order: RecordInfoOrder?,
    @SerializedName("orderNo")
    var orderNo: String?,
    @SerializedName("planId")
    var planId: Any?,
    @SerializedName("remarks")
    var remarks: Any?,
    @SerializedName("shopId")
    var shopId: String?,
    @SerializedName("shopName")
    var shopName: String?,
    @SerializedName("shopPhone")
    var shopPhone: String?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("technicianId")
    var technicianId: Any?,
    @SerializedName("technicianNickName")
    var technicianNickName: Any?,
    @SerializedName("updatedTime")
    var updatedTime: String?,
    @SerializedName("userId")
    var userId: String?,
    @SerializedName("username")
    var username: String?
)

data class RecordInfoOrder(
    @SerializedName("addTime")
    var addTime: String?,
    @SerializedName("address")
    var address: Any?,
    @SerializedName("addressId")
    var addressId: String?,
    @SerializedName("adminNote")
    var adminNote: Any?,
    @SerializedName("apShopId")
    var apShopId: Any?,
    @SerializedName("apShopName")
    var apShopName: Any?,
    @SerializedName("apShopPhone")
    var apShopPhone: Any?,
    @SerializedName("appointment")
    var appointment: String?,
    @SerializedName("appointmentId")
    var appointmentId: String?,
    @SerializedName("carId")
    var carId: String?,
    @SerializedName("cardNo")
    var cardNo: String?,
    @SerializedName("cardPass")
    var cardPass: Any?,
    @SerializedName("cardPassPrice")
    var cardPassPrice: String?,
    @SerializedName("cashMoney")
    var cashMoney: String?,
    @SerializedName("city")
    var city: Any?,
    @SerializedName("commentStatus")
    var commentStatus: Any?,
    @SerializedName("confirmTime")
    var confirmTime: Any?,
    @SerializedName("consignee")
    var consignee: Any?,
    @SerializedName("consumptionCode")
    var consumptionCode: String?,
    @SerializedName("costPrice")
    var costPrice: String?,
    @SerializedName("country")
    var country: Any?,
    @SerializedName("couponId")
    var couponId: Any?,
    @SerializedName("couponPrice")
    var couponPrice: String?,
    @SerializedName("cusName")
    var cusName: String?,
    @SerializedName("cusPhone")
    var cusPhone: String?,
    @SerializedName("deleted")
    var deleted: Any?,
    @SerializedName("deletedTime")
    var deletedTime: Any?,
    @SerializedName("details")
    var details: Any?,
    @SerializedName("discount")
    var discount: String?,
    @SerializedName("district")
    var district: Any?,
    @SerializedName("email")
    var email: Any?,
    @SerializedName("extraStr")
    var extraStr: Any?,
    @SerializedName("finishedTime")
    var finishedTime: Any?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("integral")
    var integral: String?,
    @SerializedName("integralMoney")
    var integralMoney: String?,
    @SerializedName("invoiceTitle")
    var invoiceTitle: Any?,
    @SerializedName("isDistribut")
    var isDistribut: Any?,
    @SerializedName("kmAppointmentRecord")
    var kmAppointmentRecord: Any?,
    @SerializedName("kmOrderGoods")
    var kmOrderGoods: List<KmOrderGood>,
    @SerializedName("logo")
    var logo: String?,
    @SerializedName("mobile")
    var mobile: Any?,
    @SerializedName("oilGrade")
    var oilGrade: Any?,
    @SerializedName("oilNum")
    var oilNum: Any?,
    @SerializedName("oilType")
    var oilType: Any?,
    @SerializedName("orderBagId")
    var orderBagId: Any?,
    @SerializedName("orderMoney")
    var orderMoney: String?,
    @SerializedName("orderSn")
    var orderSn: String?,
    @SerializedName("orderStatus")
    var orderStatus: String?,
    @SerializedName("packageId")
    var packageId: String?,
    @SerializedName("packageName")
    var packageName: String?,
    @SerializedName("parentSn")
    var parentSn: String?,
    @SerializedName("payCode")
    var payCode: Any?,
    @SerializedName("payMethod")
    var payMethod: String?,
    @SerializedName("payName")
    var payName: Any?,
    @SerializedName("payStatus")
    var payStatus: String?,
    @SerializedName("payTime")
    var payTime: Any?,
    @SerializedName("platformSettleMoney")
    var platformSettleMoney: String?,
    @SerializedName("promType")
    var promType: String?,
    @SerializedName("province")
    var province: Any?,
    @SerializedName("receiveType")
    var receiveType: Any?,
    @SerializedName("serviceFee")
    var serviceFee: String?,
    @SerializedName("settlePrice")
    var settlePrice: String?,
    @SerializedName("shippingCode")
    var shippingCode: Any?,
    @SerializedName("shippingName")
    var shippingName: Any?,
    @SerializedName("shippingPrice")
    var shippingPrice: String?,
    @SerializedName("shippingStatus")
    var shippingStatus: String?,
    @SerializedName("shippingTime")
    var shippingTime: Any?,
    @SerializedName("shopCommission")
    var shopCommission: String?,
    @SerializedName("shopId")
    var shopId: String?,
    @SerializedName("shopName")
    var shopName: String?,
    @SerializedName("shopPhone")
    var shopPhone: String?,
    @SerializedName("shopScoreList")
    var shopScoreList: Any?,
    @SerializedName("shopSettleMoney")
    var shopSettleMoney: String?,
    @SerializedName("subList")
    var subList: Any?,
    @SerializedName("summary")
    var summary: String?,
    @SerializedName("taxpayer")
    var taxpayer: Any?,
    @SerializedName("teamerCommission")
    var teamerCommission: String?,
    @SerializedName("technicianId")
    var technicianId: String?,
    @SerializedName("technicianName")
    var technicianName: String?,
    @SerializedName("transactionId")
    var transactionId: Any?,
    @SerializedName("userCars")
    var userCars: UserCars?,
    @SerializedName("userCommentList")
    var userCommentList: Any?,
    @SerializedName("userCommission")
    var userCommission: String?,
    @SerializedName("userId")
    var userId: String?,
    @SerializedName("userNote")
    var userNote: Any?
)
