package com.bossed.waej.javebean

class PrintSettingBean {
    var type = 0
    var isChecked = false
    var isEnable = true
    var text: String? = null
    var content: String? = null
    var dataList: List<String>? = null

    constructor() {}
    constructor(type: Int, text: String?, content: String?) : this(
        type,
        false,
        true,
        text,
        content
    ) {
    }

    constructor(type: Int, isChecked: Boolean, text: String?, content: String?) : this(
        type,
        isChecked,
        true,
        text,
        content
    ) {
    }

    constructor(type: Int, isChecked: Boolean, isEnable: Boolean, text: String?, content: String?) {
        this.isEnable = isEnable
        this.type = type
        this.isChecked = isChecked
        this.text = text
        this.content = content
    }
}