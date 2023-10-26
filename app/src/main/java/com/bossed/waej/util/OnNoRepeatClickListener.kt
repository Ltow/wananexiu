package com.bossed.waej.util

import android.view.View

/**
 * 防止View重复点击
 */
interface OnNoRepeatClickListener : View.OnClickListener {

    override fun onClick(v: View) {
        onRepeatClick(v)
    }

    fun onRepeatClick(v: View?)
}