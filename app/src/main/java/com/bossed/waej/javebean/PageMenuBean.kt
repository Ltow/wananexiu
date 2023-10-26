package com.bossed.waej.javebean

data class PageMenuBean(
    var menu: String,
    var resource: Int,
    var subscript: String,
    var isBuy: Boolean = false
)
