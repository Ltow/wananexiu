package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName

data class AlertListResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("rows")
    val rows: List<AlertRow>,
    @SerializedName("total")
    val total: Int
)

data class AlertRow(
    @SerializedName("cardNo")
    val cardNo: String,
    @SerializedName("customerId")
    val customerId: Int,
    @SerializedName("customerName")
    val customerName: String,
    @SerializedName("customerPhone")
    val customerPhone: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("insure2Date")
    val insure2Date: String,
    @SerializedName("isRemind")
    val isRemind: Int,
    @SerializedName("policyName")
    val policyName: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("shopId")
    val shopId: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("maintenanceMileageNext")
    val maintenanceMileageNext: Int,
    @SerializedName("maintenanceMileage")
    val maintenanceMileage: Int,
    @SerializedName("maintenanceMileageDay")
    val maintenanceMileageDay: Int,
    @SerializedName("updateBy")
    val updateBy: String,
    @SerializedName("createBy")
    val createBy: String,
    @SerializedName("updateTime")
    val updateTime: String,
    @SerializedName("userCarId")
    val userCarId: Int,
    @SerializedName("maintenanceType")
    val maintenanceType: Int
)