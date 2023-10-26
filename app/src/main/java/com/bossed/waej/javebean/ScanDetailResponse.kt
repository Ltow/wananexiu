package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class ScanDetailResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var `data`: ScanDetailData?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("succ")
    var succ: Boolean?
)

data class ScanDetailData(
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
    var appointment: Any?,
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
    var cusName: Any?,
    @SerializedName("cusPhone")
    var cusPhone: Any?,
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
    var kmAppointmentRecord: KmAppointmentRecord?,
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
    var shippingTime: String?,
    @SerializedName("finishedTime")
    var finishedTime: String?,
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
    var technicianId: Any?,
    @SerializedName("technicianName")
    var technicianName: Any?,
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

data class KmOrderGood(
    @SerializedName("barCode")
    var barCode: Any?,
    @SerializedName("costPrice")
    var costPrice: String?,
    @SerializedName("deliveryId")
    var deliveryId: Any?,
    @SerializedName("giveIntegral")
    var giveIntegral: Any?,
    @SerializedName("goodsId")
    var goodsId: String?,
    @SerializedName("goodsLogo")
    var goodsLogo: String?,
    @SerializedName("goodsName")
    var goodsName: String?,
    @SerializedName("goodsNum")
    var goodsNum: String?,
    @SerializedName("goodsSn")
    var goodsSn: Any?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("isComment")
    var isComment: Any?,
    @SerializedName("isPackage")
    var isPackage: String?,
    @SerializedName("isSend")
    var isSend: Any?,
    @SerializedName("marketPrice")
    var marketPrice: String?,
    @SerializedName("orderId")
    var orderId: Any?,
    @SerializedName("orderSn")
    var orderSn: String?,
    @SerializedName("promType")
    var promType: String?,
    @SerializedName("remark")
    var remark: Any?,
    @SerializedName("serviceFee")
    var serviceFee: String?,
    @SerializedName("settlePrice")
    var settlePrice: String?,
    @SerializedName("specId")
    var specId: Any?,
    @SerializedName("specName")
    var specName: Any?,
    @SerializedName("summary")
    var summary: String?
) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}

data class UserCars(
    @SerializedName("authTime")
    var authTime: String?,
    @SerializedName("brandId")
    var brandId: Int?,
    @SerializedName("brandName")
    var brandName: String?,
    @SerializedName("carId")
    var carId: Int?,
    @SerializedName("carName")
    var carName: String?,
    @SerializedName("carType")
    var carType: Int?,
    @SerializedName("carTypeName")
    var carTypeName: String?,
    @SerializedName("cardNo")
    var cardNo: String?,
    @SerializedName("cardNoColor")
    var cardNoColor: String?,
    @SerializedName("compulsory")
    var compulsory: String?,
    @SerializedName("compulsoryCity")
    var compulsoryCity: String?,
    @SerializedName("compulsoryInsuranceDate")
    var compulsoryInsuranceDate: String?,
    @SerializedName("compulsoryTransfer")
    var compulsoryTransfer: String?,
    @SerializedName("driverImg")
    var driverImg: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("isAuth")
    var isAuth: Int?,
    @SerializedName("isDefault")
    var isDefault: Int?,
    @SerializedName("logo")
    var logo: String?,
    @SerializedName("maintenanceDate")
    var maintenanceDate: String?,
    @SerializedName("maintenanceMileage")
    var maintenanceMileage: String?,
    @SerializedName("mjsid")
    var mjsid: String?,
    @SerializedName("oem")
    var oem: String?,
    @SerializedName("oilGrade")
    var oilGrade: String?,
    @SerializedName("oilNum")
    var oilNum: String?,
    @SerializedName("oilType")
    var oilType: String?,
    @SerializedName("policyId")
    var policyId: Int?,
    @SerializedName("userId")
    var userId: Int?,
    @SerializedName("vnCode")
    var vnCode: String?
)

data class KmAppointmentRecord(
    @SerializedName("beginTime")
    var beginTime: String?,
    @SerializedName("cardNo")
    var cardNo: String?,
    @SerializedName("createdTime")
    var createdTime: String?,
    @SerializedName("driverId")
    var driverId: Int?,
    @SerializedName("driverName")
    var driverName: String?,
    @SerializedName("driverPhone")
    var driverPhone: String?,
    @SerializedName("endTime")
    var endTime: String?,
    @SerializedName("extraStr")
    var extraStr: String?,
    @SerializedName("face")
    var face: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("nickname")
    var nickname: String?,
    @SerializedName("operator")
    var `operator`: String?,
    @SerializedName("operatorId")
    var operatorId: Int?,
    @SerializedName("order")
    var order: Order?,
    @SerializedName("orderNo")
    var orderNo: String?,
    @SerializedName("planId")
    var planId: Int?,
    @SerializedName("remarks")
    var remarks: String?,
    @SerializedName("shopId")
    var shopId: Int?,
    @SerializedName("shopName")
    var shopName: String?,
    @SerializedName("shopPhone")
    var shopPhone: String?,
    @SerializedName("status")
    var status: Int?,
    @SerializedName("technicianId")
    var technicianId: Int?,
    @SerializedName("technicianNickName")
    var technicianNickName: String?,
    @SerializedName("updatedTime")
    var updatedTime: String?,
    @SerializedName("userId")
    var userId: Int?,
    @SerializedName("username")
    var username: String?
)
