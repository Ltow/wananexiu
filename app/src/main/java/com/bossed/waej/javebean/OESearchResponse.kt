package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class OESearchResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String,
    @SerializedName("result")
    val result: OESearchResult
)

data class OESearchResult(
    @SerializedName("ds1")
    val ds1: List<OESearchDs1>
)

data class OESearchDs1(
    @SerializedName("brandCode")
    val brandCode: String,
    @SerializedName("brandID")
    val brandID: String,
    @SerializedName("oe")
    val oe: String,
    @SerializedName("上海市场价")
    val 上海市场价: String,
    @SerializedName("东北区市场价")
    val 东北区市场价: String,
    @SerializedName("华东区市场价")
    val 华东区市场价: String,
    @SerializedName("华北区市场价")
    val 华北区市场价: String,
    @SerializedName("华南区市场价")
    val 华南区市场价: String,
    @SerializedName("原厂零件名称")
    val 原厂零件名称: String,
    @SerializedName("指导价格")
    val 指导价格: String,
    @SerializedName("替换号")
    val 替换号: String,
    @SerializedName("标准零件名称")
    val 标准零件名称: String,
    @SerializedName("西部区市场价")
    val 西部区市场价: String,
    @SerializedName("车型品牌名称")
    val 车型品牌名称: String,
    @SerializedName("适配车型")
    val 适配车型: String,
    @SerializedName("isSel")
    var isSel:Boolean=false
)