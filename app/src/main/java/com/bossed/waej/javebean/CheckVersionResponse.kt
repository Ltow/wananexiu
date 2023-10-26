package com.bossed.waej.javebean

data class CheckVersionResponse(
    val code: Int,
    val `data`: CheckVersionData,
    val msg: String
)

data class CheckVersionData(
    val appName: String,
    val forcedToup: Int,
    val id: Int,
    val isTrue: Int,
    val remark: String,
    val type: Int,
    val versionCode: Int,
    val versionName: String,
    val versionUrl: String
)