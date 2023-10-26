package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class RechargeFlowResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String,
    @SerializedName("result")
    val result: RechargeFlowResult
)

data class RechargeFlowResult(
    @SerializedName("ds1")
    val ds1: List<RechargeFlowDs1>
)

data class RechargeFlowDs1(
    @SerializedName("交易单号")
    val 交易单号: String,
    @SerializedName("备注")
    val 备注: String,
    @SerializedName("支付时间")
    val 支付时间: String,
    @SerializedName("时间")
    val 时间: String,
    @SerializedName("订单号")
    val 订单号: String,
    @SerializedName("金额")
    val 金额: String
)