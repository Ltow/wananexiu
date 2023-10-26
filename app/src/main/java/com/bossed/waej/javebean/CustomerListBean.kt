package com.bossed.waej.javebean

data class CustomerListBean(
    val code: Int,
    val msg: String,
    val rows: List<CustomerListRow>,
    val total: Int
)

data class CustomerListRow(
    val balance: String,
    val carList: List<Car>,
    val createBy: String,
    val createTime: String,
    val customerName: String,
    val customerPhone: String,
    val id: Int,
    val remark: String,
    val shopId: Int,
    val tenantId: Int,
    val updateBy: String,
    val updateTime: String
)