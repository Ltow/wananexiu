package com.bossed.waej.adapter

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import com.bossed.waej.R
import com.bossed.waej.javebean.BookDs1
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class BookAdapter(data: MutableList<BookDs1>) :
    BaseQuickAdapter<BookDs1, BaseViewHolder>(R.layout.layout_item_book, data) {
    override fun convert(helper: BaseViewHolder, item: BookDs1) {
        helper.setText(
            R.id.tv_name,
            if (TextUtils.isEmpty(item.stdMaintName)) "项目/公里数" else item.stdMaintName
        )
        helper.getView<View>(R.id.rl_item).setBackgroundColor(Color.parseColor("#f5f5f5"))
    }
}