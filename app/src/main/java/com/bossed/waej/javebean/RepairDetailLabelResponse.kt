package com.bossed.waej.javebean

data class RepairDetailLabelResponse(
    val code: Int,
    val `data`: List<LabelData>,
    val message: String
)

data class LabelData(
    val brandId: Int,
    val cateDesc: String,
    val cateTitle: String,
    val createAt: String,
    val id: Int,
    val isDeleted: Int,
    val pid: Int,
    val sort: Int,
    val status: Int,
    var isSelected: Boolean = false
)