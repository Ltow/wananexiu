package com.bossed.waej.javebean

data class AddItemResponse(
    val code: Int,
    val `data`: List<JieCheItem>,
    val msg: String
)
