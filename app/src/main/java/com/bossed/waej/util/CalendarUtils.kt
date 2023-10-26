package com.bossed.waej.util

import java.util.*

class CalendarUtils private constructor() {
    companion object {
        private var instance: CalendarUtils? = null
            get() {
                if (field == null)
                    instance = CalendarUtils()
                return field
            }

        @Synchronized
        fun get(): CalendarUtils {
            return instance!!
        }
    }

    /**
     * 在当前系统时间基础上增加天数
     */
    fun nowDateAddDays(days: Int): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, days)
        return cal.time
    }

    /**
     * @param dateString 日期
     * @param day 天数
     */
    val setDayOfAddDays = { dateString: String, day: Int ->
        val c: Calendar = Calendar.getInstance()
        c.set(Calendar.YEAR, dateString.substring(0, 4).toInt())
        c.set(Calendar.MONTH, dateString.substring(5, 7).toInt() - 1)
        c.set(Calendar.DAY_OF_MONTH, dateString.substring(8, 10).toInt())
        c.add(Calendar.DATE, -day)
        c.time
    }
}