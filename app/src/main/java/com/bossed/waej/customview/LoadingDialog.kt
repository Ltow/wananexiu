package com.bossed.waej.customview

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.bossed.waej.R

/**
 * 自定义Dialog
 */
class LoadingDialog : Dialog {
    /**
     * 跟随Dialog 一起显示的message 信息！
     */
    private var msg: String

    constructor(context: Context, theme: Int, msg: String) : super(context, theme) {
        this.msg = msg
    }

    constructor(context: Context, msg: String) : super(context, R.style.loadingDialog) {
        this.msg = msg
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = View.inflate(context, R.layout.custom_view_loading, null)
        val textView = view.findViewById<View>(R.id.tv_message) as TextView
        if (!TextUtils.isEmpty(msg)) {
            textView.text = msg
        }
        setContentView(view)
    }

    override fun onStop() {
        super.onStop()
    }
}