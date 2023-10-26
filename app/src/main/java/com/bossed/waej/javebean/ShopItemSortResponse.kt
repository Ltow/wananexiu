package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class ShopItemSortResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var `data`: List<ShopItemSortData>,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("succ")
    var succ: Boolean?
)

data class ShopItemSortData(
    @SerializedName("createBy")
    var createBy: String?,
    @SerializedName("createTime")
    var createTime: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("imgUrl")
    var imgUrl: String?,
    @SerializedName("level")
    var level: Int?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("parentId")
    var parentId: Int?,
    @SerializedName("remark")
    var remark: String?,
    @SerializedName("shopId")
    var shopId: Int?,
    @SerializedName("sort")
    var sort: Int?,
    @SerializedName("status")
    var status: Int?,
    @SerializedName("updateBy")
    var updateBy: String?,
    @SerializedName("updateTime")
    var updateTime: String?,
    @SerializedName("isSelected")
    var isSelected: Boolean = false
)