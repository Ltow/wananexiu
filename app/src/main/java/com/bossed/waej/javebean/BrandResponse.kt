package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class BrandResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String,
    @SerializedName("result")
    val result: BrandResult
)

data class BrandResult(
    @SerializedName("ds1")
    val ds1: List<BrandDs1>
)

data class BrandDs1(
    @SerializedName("brandIcon")
    val brandIcon: String,
    @SerializedName("brandId")
    val brandId: String,
    @SerializedName("brandCode")
    val brandCode: String,
    @SerializedName("brandName")
    val brandName: String,
    @SerializedName("clzl")
    val clzl: String,
    @SerializedName("letter")
    val letter: String
)