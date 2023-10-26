package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName

data class TreeListBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<TreeListData>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("succ")
    val succ: Boolean
)

data class TreeListData(
    @SerializedName("menuId")
    val menuId: Int,
    @SerializedName("menuName")
    val menuName: String,
    @SerializedName("menuType")
    val menuType: String,
    @SerializedName("orderNum")
    val orderNum: Int,
    @SerializedName("parentId")
    val parentId: Int,
    @SerializedName("path")
    val path: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("updateTime")
    val updateTime: String,
    @SerializedName("visible")
    val visible: String,
    @SerializedName("isPayPerm")
    val isPayPerm: Boolean
)