package com.bossed.waej.util

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter

interface OnNoRepeatItemClickListener : BaseQuickAdapter.OnItemClickListener {
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if (DoubleClicksUtils.get().isFastDoubleClick) {
            return
        }
        onNoRepeatItemItemClick(adapter, view, position)
    }

    fun onNoRepeatItemItemClick(
        adapter: BaseQuickAdapter<*, *>?,
        view: View?,
        position: Int
    )
}