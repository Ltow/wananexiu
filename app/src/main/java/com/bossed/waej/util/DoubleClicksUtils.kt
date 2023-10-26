package com.bossed.waej.util

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils

class DoubleClicksUtils private constructor() {
    companion object {
        private var instance: DoubleClicksUtils? = null
            get() {
                if (field == null)
                    instance = DoubleClicksUtils()
                return field
            }

        @Synchronized
        fun get(): DoubleClicksUtils {
            return instance!!
        }
    }

    private var lastClickTime: Long = 0
    val isFastDoubleClick: Boolean
        get() {
            val time = TimeUtils.getNowMills()
            val timeD = time - lastClickTime
            if (lastClickTime > 0 && timeD < 800) {
                LogUtils.d("isFastDoubleClick", "短时间内按钮多次触发")
                return true
            }
            lastClickTime = time
            return false
        }
}