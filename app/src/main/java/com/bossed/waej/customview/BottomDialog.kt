package com.bossed.waej.customview

import android.content.Context
import android.content.DialogInterface
import com.bossed.waej.R
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomDialog(private val context: Context) {
    private val dialog: BottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetStyle)
    private var view: View? = null//contentView

    fun create(layoutResId: Int): BottomDialog {
        view = LayoutInflater.from(context).inflate(layoutResId, null)
        dialog.setContentView(view!!)
        dialog.window!!.attributes.windowAnimations = R.style.BottomAnimation//动画效果
        return this
    }

    fun dismiss() {
        dialog.dismiss()
    }

    fun show() {
        dialog.show()
    }

    fun isShowing(): Boolean {
        return dialog.isShowing
    }

    fun setCanceledOnTouchOutside(boolean: Boolean): BottomDialog {
        dialog.setCanceledOnTouchOutside(boolean)
        dialog.setCancelable(boolean)
        return this
    }

    fun setViewInterface(listener: (view: View, dialog: BottomDialog) -> Unit): BottomDialog {
//        this.viewInterfaceListener = listener
        listener.invoke(view!!, this)
        return this
    }

    interface ViewInterface {
        fun viewInterface(view: View, dialog: BottomDialog)
    }

    fun setViewInterface(viewinterface: ViewInterface): BottomDialog {
        viewinterface.viewInterface(view!!, this)
        return this
    }
}