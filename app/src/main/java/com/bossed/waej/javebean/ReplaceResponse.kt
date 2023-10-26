package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class ReplaceResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String,
    @SerializedName("result")
    val result: ReplaceResult
)

data class ReplaceResult(
    @SerializedName("ds1")
    val ds1: List<ReplaceDs1>
)

data class ReplaceDs1(
    @SerializedName("brandId")
    val brandId: String,
    @SerializedName("guidePrice")
    val guidePrice: String,
    @SerializedName("OE")
    val oE: String,
    @SerializedName("oeId")
    val oeId: String,
    @SerializedName("oeIn")
    val oeIn: String,
    @SerializedName("oeName")
    val oeName: String,
    @SerializedName("oeOrder")
    val oeOrder: String,
    @SerializedName("replacerTypeName")
    val replacerTypeName: String
)