package com.bossed.waej.util

import java.math.RoundingMode
import java.text.DecimalFormat

class NumberFormatUtils {
    companion object {
        fun getFloatTwoDigits(number: Float): String {
            val format = DecimalFormat("#.##")
            format.roundingMode = RoundingMode.HALF_UP
            return format.format(number)
        }

        fun getStringTwoDigits(number: String): String {
            return getFloatTwoDigits(number.toFloat())
        }
    }
}