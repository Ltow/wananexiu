package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class RoleResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("rows")
    val rows: List<RoleRow>,
    @SerializedName("total")
    val total: Int
)

data class RoleRow(
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("dataScope")
    val dataScope: String,
    @SerializedName("deptCheckStrictly")
    val deptCheckStrictly: Boolean,
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