package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class AgentResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: AgentData?,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class AgentData(
    @SerializedName("address")
    var address: String,
    @SerializedName("agentName")
    var agentName: String,
    @SerializedName("agentType")
    var agentType: Int,
    @SerializedName("alipayAccount")
    var alipayAccount: Any,
    @SerializedName("alipyName")
    var alipyName: Any,
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("createBy")
    var createBy: String,
    @SerializedName("createDept")
    var createDept: String,
    @SerializedName("createTime")
    var createTime: String,
    @SerializedName("delFlag")
    var delFlag: String,
    @SerializedName("deptId")
    var deptId: Any,
    @SerializedName("id")
    var id: String,
    @SerializedName("inviteCode")
    var inviteCode: String,
    @SerializedName("level")
    var level: Int,
    @SerializedName("levelUp")
    var levelUp: Int,
    @SerializedName("loginDate")
    var loginDate: Any,
    @SerializedName("loginIp")
    var loginIp: String,
    @SerializedName("nickName")
    var nickName: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("phonenumber")
    var phonenumber: String,
    @SerializedName("profitMoney")
    var profitMoney: String,
    @SerializedName("remark")
    var remark: Any,
    @SerializedName("saleCommission")
    var saleCommission: String,
    @SerializedName("saleMoney")
    var saleMoney: String,
    @SerializedName("settleMoney")
    var settleMoney: String,
    @SerializedName("sex")
    var sex: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("tenantId")
    var tenantId: String,
    @SerializedName("updateBy")
    var updateBy: String,
    @SerializedName("updateTime")
    var updateTime: String,
    @SerializedName("userId")
    var userId: Any,
    @SerializedName("userName")
    var userName: String,
    @SerializedName("userType")
    var userType: String,
    @SerializedName("version")
    var version: Int
)