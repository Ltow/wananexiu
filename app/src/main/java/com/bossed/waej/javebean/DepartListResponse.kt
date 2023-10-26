package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName

data class DepartListResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<DepartListData>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("succ")
    val succ: Boolean
)

data class DepartListData(
    @SerializedName("ancestors")
    val ancestors: String,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("deptId")
    val deptId: Int,
    @SerializedName("deptName")
    val deptName: String,
    @SerializedName("email")
    val email: Any,
    @SerializedName("leader")
    val leader: String,
    @SerializedName("orderNum")
    val orderNum: Int,
    @SerializedName("parentId")
    val parentId: Int,
    @SerializedName("parentName")
    val parentName: Any,
    @SerializedName("phone")
    val phone: Any,
    @SerializedName("status")
    val status: String
)