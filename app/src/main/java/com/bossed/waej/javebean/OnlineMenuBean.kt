package com.bossed.waej.javebean

data class OnlineMenuBean(
    var backGround: Int?,
    var name: String?,
    var tag: String?,
) {
    constructor() : this(null, null, null)
}