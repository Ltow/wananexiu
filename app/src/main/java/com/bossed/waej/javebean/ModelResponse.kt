package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName

data class ModelResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String,
    @SerializedName("result")
    val result: ModelResult
)

data class ModelResult(
    @SerializedName("ds1")
    val ds1: List<ModelDs1>
)

data class ModelDs1(
    @SerializedName("groupId")
    val groupId: String,
    @SerializedName("vehicleId")
    val vehicleId: String,
    @SerializedName("发动机型号")
    val 发动机型号: String,
    @SerializedName("变速箱类型")
    val 变速箱类型: String,
    @SerializedName("品牌名称")
    val 品牌名称: String,
    @SerializedName("备注")
    val 备注: String,
    @SerializedName("年款")
    val 年款: String,
    @SerializedName("排量")
    val 排量: String,
    @SerializedName("燃油喷射形式")
    val 燃油喷射形式: String,
    @SerializedName("车型名称")
    val 车型名称: String,
    @SerializedName("车系名称")
    val 车系名称: String,
    @SerializedName("车组名称")
    val 车组名称: String,
    @SerializedName("驱动形式")
    val 驱动形式: String
)