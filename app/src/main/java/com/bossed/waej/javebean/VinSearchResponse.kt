package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName

data class VinSearchResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String,
    @SerializedName("result")
    val result: VinSearchResult
)

data class VinSearchResult(
    @SerializedName("ds1")
    val ds1: List<VinSearchDs1>
)

data class VinSearchDs1(
    @SerializedName("上市时间")
    val 上市时间: String,
    @SerializedName("供油方式")
    val 供油方式: String,
    @SerializedName("停产日期")
    val 停产日期: String,
    @SerializedName("前轮胎规格")
    val 前轮胎规格: String,
    @SerializedName("动力类型")
    val 动力类型: String,
    @SerializedName("发动机型号")
    val 发动机型号: String,
    @SerializedName("发动机生产企业")
    val 发动机生产企业: String,
    @SerializedName("变速器档数")
    val 变速器档数: String,
    @SerializedName("变速箱类型")
    val 变速箱类型: String,
    @SerializedName("后轮胎规格")
    val 后轮胎规格: String,
    @SerializedName("品牌名称")
    val 品牌名称: String,
    @SerializedName("国产进口")
    val 国产进口: String,
    @SerializedName("外形尺寸")
    val 外形尺寸: String,
    @SerializedName("年款")
    val 年款: String,
    @SerializedName("总质量")
    val 总质量: String,
    @SerializedName("排放标准")
    val 排放标准: String,
    @SerializedName("排量")
    val 排量: String,
    @SerializedName("整备质量")
    val 整备质量: String,
    @SerializedName("新车购置价")
    val 新车购置价: String,
    @SerializedName("气缸")
    val 气缸: String,
    @SerializedName("燃油喷射形式")
    val 燃油喷射形式: String,
    @SerializedName("燃油标号")
    val 燃油标号: String,
    @SerializedName("车型ID")
    val 车型ID: String,
    @SerializedName("车系名称")
    val 车系名称: String,
    @SerializedName("车组名称")
    val 车组名称: String,
    @SerializedName("车身颜色")
    val 车身颜色: String,
    @SerializedName("销售车型名称")
    val 销售车型名称: String,
    @SerializedName("驱动形式")
    val 驱动形式: String
)