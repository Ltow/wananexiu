package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class FreeTestingResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var `data`: List<FreeTestingData>,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("succ")
    var succ: Boolean?
)

data class FreeTestingData(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("mark")
    var mark: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("parentId")
    var parentId: Int?,
    @SerializedName("isSelect")
    var isSelect: Boolean = false
)