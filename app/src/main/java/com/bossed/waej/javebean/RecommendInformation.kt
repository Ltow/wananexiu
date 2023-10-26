package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName

data class RecommendInformation(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String,
    @SerializedName("result")
    val result: RecommendInformationResult
)

data class RecommendInformationResult(
    @SerializedName("ds1")
    val ds1: List<RecommendInformationDs1>,
    @SerializedName("ds2")
    val ds2: List<RecommendInformationDs2>
)

data class RecommendInformationDs1(
    @SerializedName("guidePrice")
    val guidePrice: String,
    @SerializedName("oe")
    val oe: String,
    @SerializedName("oeId")
    val oeId: String,
    @SerializedName("oeName")
    val oeName: String,
    @SerializedName("partMaterialsName")
    val partMaterialsName: String,
    @SerializedName("partNum")
    val partNum: String,
    @SerializedName("partRemark")
    val partRemark: String,
    @SerializedName("stdMaintId")
    val stdMaintId: String,
    @SerializedName("vehicleId")
    val vehicleId: String
)

data class RecommendInformationDs2(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("icePoint")
    val icePoint: String,
    @SerializedName("model")
    val model: String,
    @SerializedName("oilGrade")
    val oilGrade: String,
    @SerializedName("oilType")
    val oilType: String,
    @SerializedName("partMaterialsName")
    val partMaterialsName: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("stdMaintId")
    val stdMaintId: String,
    @SerializedName("unit")
    val unit: String,
    @SerializedName("unitPrice")
    val unitPrice: String,
    @SerializedName("vehicleId")
    val vehicleId: String,
    @SerializedName("viscosityGrade")
    val viscosityGrade: String
)