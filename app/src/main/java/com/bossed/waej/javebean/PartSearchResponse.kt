package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class PartSearchResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String,
    @SerializedName("result")
    val result: PartSearchResult
)

data class PartSearchResult(
    @SerializedName("ds1")
    val ds1: List<PartSearchDs1>
)

data class PartSearchDs1(
    @SerializedName("brandId")
    val brandId: String,
    @SerializedName("hasBrandPart")
    val hasBrandPart: String,
    @SerializedName("hasPartPic")
    val hasPartPic: String,
    @SerializedName("oe")
    val oe: String,
    @SerializedName("oeDetails")
    val oeDetails: String,
    @SerializedName("oeId")
    val oeId: String,
    @SerializedName("oeName")
    val oeName: String,
    @SerializedName("orderNo")
    val orderNo: String,
    @SerializedName("partGroupId")
    val partGroupId: String,
    @SerializedName("partGroupName")
    val partGroupName: String,
    @SerializedName("partNum")
    val partNum: String,
    @SerializedName("picNo")
    val picNo: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("stdPartId")
    val stdPartId: String,
    @SerializedName("stdPartName")
    val stdPartName: String,
    @SerializedName("vehicleId")
    val vehicleId: String,
    @SerializedName("isSel")
    var isSel: Boolean = false
)