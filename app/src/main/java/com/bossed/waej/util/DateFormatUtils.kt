package com.bossed.waej.util

import android.annotation.SuppressLint
import com.blankj.utilcode.util.TimeUtils
import java.text.SimpleDateFormat
import java.util.*

class DateFormatUtils private constructor() {
    companion object {
        private var instance: DateFormatUtils? = null
            get() {
                if (field == null)
                    instance = DateFormatUtils()
                return field
            }

        @Synchronized
        fun get(): DateFormatUtils {
            return instance!!
        }
    }

    /**
     * 格式化自己想要的日期格式
     * @param millis 时间戳
     * @return 不带时间的日期字符串
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDate(millis: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(TimeUtils.millis2Date(millis))
    }

    fun formatDate(string: String): String {
        return formatDate(TimeUtils.string2Millis(string))
    }

    fun formatDate(date: Date): String {
        return formatDate(TimeUtils.date2Millis(date))
    }

    /**
     * 格式化自己想要的日期格式
     * @param millis 时间戳
     * @return 带时间的日期字符串
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDateAndTime(millis: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(TimeUtils.millis2Date(millis))
    }

    fun formatDateAndTime(time: String): String {
        return formatDateAndTime(TimeUtils.string2Millis(time))
    }

    /**
     *格式化自己想要的时间格式
     * @param mills 时间戳
     * @return 时间格式
     */
    @SuppressLint("SimpleDateFormat")
    fun formatTime(mills: Long): String {
        val format = SimpleDateFormat("HH:mm:ss")
        return format.format(TimeUtils.millis2Date(mills))
    }

    fun formatTime(time: String): String {
        return formatTime(TimeUtils.string2Millis(time))
    }
}