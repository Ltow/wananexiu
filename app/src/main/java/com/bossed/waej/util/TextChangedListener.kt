package com.bossed.waej.util

import android.text.Editable
import android.text.TextWatcher

interface TextChangedListener : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        afterTextChange(s.toString())
    }

    fun afterTextChange(s: String)
}