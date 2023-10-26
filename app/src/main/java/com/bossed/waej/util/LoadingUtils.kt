package com.bossed.waej.util

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import com.bossed.waej.customview.LoadingDialog
import java.lang.ref.WeakReference

class LoadingUtils {
    companion object {
        private var waitDialog: WeakReference<LoadingDialog?>? = null

        /**
         * 自定义用于等待的dialog,有动画和message提示
         * 调用stopWaitDialog()方法来取消
         *
         * @param activity
         * @param message
         */
        fun showLoading(activity: Activity, message: String) {
            if (waitDialog != null && waitDialog!!.get() != null && waitDialog!!.get()!!.isShowing) {
                waitDialog!!.get()!!.dismiss()
            }
            if (activity.isFinishing) {
                return
            }
            val mThreadActivityRef = WeakReference<Context>(activity)
            waitDialog = WeakReference(
                LoadingDialog(mThreadActivityRef.get()!!, message)
            )
            if (waitDialog!!.get() != null && !waitDialog!!.get()!!.isShowing) {
                waitDialog!!.get()!!.setCanceledOnTouchOutside(false)
                waitDialog!!.get()!!.setCancelable(true)
                waitDialog!!.get()!!.show()
            }
        }

        /**
         * 取消等待dialog
         */
        fun dismissLoading() {
            if (waitDialog != null && waitDialog!!.get() != null && waitDialog!!.get()!!.isShowing) {
                waitDialog!!.get()!!.dismiss()
            }
        }

        /**
         * 当前dialog状态
         */
        fun isShowing(): Boolean {
            return waitDialog!!.get()!!.isShowing
        }
    }


}