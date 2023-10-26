package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class GoodsSortResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<GoodsSortData>,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class GoodsSortData(
    @SerializedName("id")
    var id: Int,
    @SerializedName("image")
    var image: String,
    @SerializedName("logo")
    var logo: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("parentId")
    var parentId: Int,
    @SerializedName("shopId")
    var shopId: Int,
    @SerializedName("status")
    var status: Int,
    @SerializedName("isSelected")
    var isSelected: Boolean,
)