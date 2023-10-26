package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class SetOfResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String,
    @SerializedName("result")
    val result: SetOfResult
)

data class SetOfResult(
    @SerializedName("ds1")
    val ds1: List<SetOfDs1>
)

data class SetOfDs1(
    @SerializedName("clzl")
    val clzl: String,
    @SerializedName("groupId")
    val groupId: String,
    @SerializedName("groupName")
    val groupName: String
)