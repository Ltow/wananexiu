package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class VinSearchBean(
    @SerializedName("content")
    val content: String,
    @SerializedName("name")
    val name: String
)