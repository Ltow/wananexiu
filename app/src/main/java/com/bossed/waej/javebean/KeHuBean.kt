package com.bossed.waej.javebean

data class KeHuBean(
    val code: Int,
    val `data`: List<KeHuData>,
    val msg: String
)

data class KeHuData(
    val authTime: String,
    val brandId: Int,
    val brandLogo: String,
    val brandName: String,
    val carId: Int,
    val carName: String,
    val jycarId: String,
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
    val updateBy: String,
    val updateTime: String,
    val vnCode: String,
    val year: String
)