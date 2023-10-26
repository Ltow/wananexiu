package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class BookResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String,
    @SerializedName("result")
    val result: BookResult
)

data class BookResult(
    @SerializedName("ds1")
    val ds1: List<BookDs1>
)

data class BookDs1(
    @SerializedName("checkList")
    val checkList: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("maintMileageTitle")
    val maintMileageTitle: String,
    @SerializedName("mustList")
    val mustList: String,
    @SerializedName("repairPrice")
    val repairPrice: String,
    @SerializedName("stdMaintId")
    val stdMaintId: String,
    @SerializedName("stdMaintName")
    val stdMaintName: String,
    @SerializedName("stdMaintTypeName")
    val stdMaintTypeName: String,
    @SerializedName("vehicleId")
    val vehicleId: String
)