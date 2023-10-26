package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName

data class RoleInfoResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: RoleInfoData,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("succ")
    val succ: Boolean
)

data class RoleInfoData(
    @SerializedName("appMenuIds")
    val appMenuIds: List<Int>,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("dataScope")
    val dataScope: String,
    @SerializedName("deptCheckStrictly")
    val deptCheckStrictly: Boolean,
    @SerializedName("deptId")
    val deptId: Int,
    @SerializedName("flag")
    val flag: Boolean,
    @SerializedName("ispFlag")
    val ispFlag: Any,
    @SerializedName("menuCheckStrictly")
    val menuCheckStrictly: Boolean,
    @SerializedName("remark")
    val remark: Any,
    @SerializedName("roleId")
    val roleId: Int,
    @SerializedName("roleKey")
    val roleKey: String,
    @SerializedName("roleName")
    val roleName: String,
    @SerializedName("roleSort")
    val roleSort: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("superAdmin")
    val superAdmin: Boolean
)