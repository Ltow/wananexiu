package com.bossed.waej.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.CheckedTextView
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.bossed.waej.R
import com.bossed.waej.util.OnDialogListener
import java.util.*

/**
 * 提示dialog
 */
class MyAlertDialog(context: Context, private var msg: String) :
    Dialog(context, R.style.Dialog) {
    private lateinit var listener: OnDialogListener
    private var isShowCancel = true
    private var isShowCTV = false
    private var confirm = "确定"
    private var cancel = "取消"
    private var confirmColor = ""
    private var cancelColor = ""

    fun setOnDialogListener(listener: OnDialogListener.Builder.() -> Unit) {
        val builder = OnDialogListener.Builder()
        builder.listener()
        this.listener = builder
    }

    /**
     * 设置是否显示取消按钮
     * @param isShowCancel true/false
     */
    fun setShowCancel(isShowCancel: Boolean) {
        this.isShowCancel = isShowCancel
    }

    /**
     *设置确认按钮文本
     * @param string 内容
     */
    fun setConfirm(string: String) {
        this.confirm = string
    }

    /**
     * 设置确认按钮文字颜色
     * @param color
     */
    fun setConfirmTextColor(color: String) {
        this.confirmColor = color
    }

    /**
     *设置取消按钮文本
     * @param string 内容
     */
    fun setCancel(string: String) {
        this.cancel = string
    }

    /**
     * 设置取消按钮文字颜色
     * @param color
     */
    fun setCancelTextColor(color: String) {
        this.cancelColor = color
    }

    /**
     * 设置显示当天不提示CheckedTextView
     */
    fun setShowCTV(isShowCTV: Boolean) {
        this.isShowCTV = isShowCTV
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = View.inflate(context, R.layout.layout_pop_base_confirm, null)
        view.findViewById<TextView>(R.id.tv_pop_content).text = msg
        val ctv_no_alert = view.findViewById<CheckedTextView>(R.id.ctv_no_alert)
        ctv_no_alert.visibility = if (isShowCTV) View.VISIBLE else View.GONE
        val tv_confirm = view.findViewById<TextView>(R.id.tv_confirm)
        tv_confirm.text = confirm
        if (!TextUtils.isEmpty(confirmColor))
            tv_confirm.setTextColor(Color.parseColor(confirmColor))
        val tv_cancel = view.findViewById<TextView>(R.id.tv_cancel)
        tv_cancel.text = cancel
        if (!TextUtils.isEmpty(cancelColor))
            tv_cancel.setTextColor(Color.parseColor(cancelColor))
        if (!isShowCancel) {
            tv_cancel.visibility = View.GONE
            view.findViewById<View>(R.id.v_pop).visibility = View.GONE
        }
        tv_cancel.setOnClickListener {
            listener.onCancel()
        }
        tv_confirm.setOnClickListener {
            listener.onConfirm()
            if (ctv_no_alert.isChecked) {
                val long =
                    TimeUtils.getNowMills() - (TimeUtils.getNowMills() + TimeZone.getDefault().rawOffset) % (24 * 60 * 60 * 1000)
                SPUtils.getInstance()
                    .put("today", long + (24 * 60 * 60 * 1000))
            }
        }
        ctv_no_alert.setOnClickListener {
            ctv_no_alert.isChecked = !ctv_no_alert.isChecked
        }
        setContentView(view)
        window!!.attributes.windowAnimations = R.style.CenterAnimation
    }

}