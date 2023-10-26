package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class BalanceResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String,
    @SerializedName("result")
    val result: BalanceResult
)

data class BalanceResult(
    @SerializedName("ds1")
    val ds1: List<BalanceDs1>
)

data class BalanceDs1(
    @SerializedName("充值金额|剩余")
    val 充值金额剩余: String,
    @SerializedName("充值金额|已用")
    val 充值金额已用: String,
    @SerializedName("充值金额|总额")
    val 充值金额总额: String,
    @SerializedName("免费金额|剩余")
    val 免费金额剩余: String,
    @SerializedName("免费金额|已用")
    val 免费金额已用: String,
    @SerializedName("免费金额|总额")
    val 免费金额总额: String,
    @SerializedName("公司名称")
    val 公司名称: String,
    @SerializedName("无忧卡号")
    val 无忧卡号: String,
    @SerializedName("电话")
    val 电话: String,
    @SerializedName("联系人")
    val 联系人: String,
    @SerializedName("套餐|车型解析")
    val 车型解析: String,
    @SerializedName("套餐|保养解析")
    val 保养解析: String,
    @SerializedName("套餐|配件解析")
    val 配件解析: String,
)