package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName

data class FlowResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String,
    @SerializedName("result")
    val result: FlowResult
)

data class FlowResult(
    @SerializedName("ds1")
    val ds1: List<FlowDs1>
)

data class FlowDs1(
    @SerializedName("id")
    val id: String,
    @SerializedName("PostMode")
    val postMode: String,
    @SerializedName("UseKind")
    val useKind: String,
    @SerializedName("备注")
    val 备注: String,
    @SerializedName("外网IP")
    val 外网IP: String,
    @SerializedName("操作员")
    val 操作员: String,
    @SerializedName("时间")
    val 时间: String,
    @SerializedName("查询信息")
    val 查询信息: String,
    @SerializedName("设备名")
    val 设备名: String,
    @SerializedName("费用")
    val 费用: String,
    @SerializedName("辅助信息")
    val 辅助信息: String
)