package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName

data class AccountResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: AccountData,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("succ")
    val succ: Boolean
)

data class AccountData(
    @SerializedName("siteNum")
    val siteNum: Int,
    @SerializedName("userOnlineNum")
    val userOnlineNum: Int,
    @SerializedName("userList")
    val userList: List<User>,
    @SerializedName("userNum")
    val userNum: Int,
    @SerializedName("termTime")
    val termTime: String?,
    @SerializedName("site")
    val site: Site
)

data class Site(
    @SerializedName("id")
    val id: Int,
    @SerializedName("packageName")
    val packageName: String,
    @SerializedName("priceDay")
    val priceDay: String,
    @SerializedName("priceYear")
    val priceYear: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("updateTime")
    val updateTime: String
)

data class User(
    @SerializedName("address")
    val address: Any,
    @SerializedName("area")
    val area: Any,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("balance")
    val balance: String,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("dataScope")
    val dataScope: String,
    @SerializedName("dept")
    val dept: Any,
    @SerializedName("deptId")
    val deptId: Any,
    @SerializedName("email")
    val email: String,
    @SerializedName("loginDate")
    val loginDate: Any,
    @SerializedName("loginIp")
    val loginIp: String,
    @SerializedName("nickName")
    val nickName: String,
    @SerializedName("numCar")
    val numCar: Any,
    @SerializedName("numCarTeamer")
    val numCarTeamer: Any,
    @SerializedName("phonenumber")
    val phonenumber: String,
    @SerializedName("postIds")
    val postIds: Any,
    @SerializedName("remark")
    val remark: Any,
    @SerializedName("roleId")
    val roleId: Any,
    @SerializedName("roleIds")
    val roleIds: Any,
    @SerializedName("roles")
    val roles: Any,
    @SerializedName("sex")
    val sex: String,
    @SerializedName("shopId")
    val shopId: Int,
    @SerializedName("staffId")
    val staffId: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("userType")
    val userType: String
)