package com.bossed.waej.javebean

data class CarListBean(
    val code: Int,
    val msg: String,
    val rows: List<CarListRow>,
    val total: Int
)

data class CarListRow(
    val authTime: String,
    val brandId: Int,
    val brandLogo: String,
    val brandName: String,
    val carId: Int,
    val carName: String,
    val cardNo: String,
    val createBy: String,
    val createTime: String,
    val customerId: Int,
    val customerName: String,
    val customerPhone: String,
    val displacement: String,
    val driverImg: String,
    val engineNo: String,
    val id: Int,
    val isAuth: Int,
    val isDefault: Int,
    val maintenanceDate: String,
    val maintenanceMileage: String,
    val mileage: String,
    val mjsid: String,
    val oilNum: String,
    val oilType: String,
    val remark: String,
    val seresId: Int,
    val shopId: Int,
    val tenantId: Int,
    val isVip: Int,
    val updateBy: String,
    val updateTime: String,
    val vnCode: String,
    val year: String
)