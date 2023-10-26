package com.bossed.waej.adapter

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ConvertUtils
import com.bossed.waej.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class BookItem2Adapter(data: MutableList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.layout_item_book, data) {
    override fun convert(helper: BaseViewHolder, item: String) {
        val textView = helper.getView<TextView>(R.id.tv_name)
        helper.getView<View>(R.id.rl_item).setBackgroundColor(
            if (helper.adapterPosition == 0)
                Color.parseColor("#f5f5f5")
            else
                Color.parseColor("#ffffff")
        )
        when (item) {
            "更换" -> {
                textView.text = ""
                textView.setBackgroundResource(R.drawable.shape_circle_000000)
                val layoutParams: RelativeLayout.LayoutParams =
                    textView.layoutParams as RelativeLayout.LayoutParams
                layoutParams.width = ConvertUtils.dp2px(8f)
                layoutParams.height = ConvertUtils.dp2px(8f)
                textView.layoutParams = layoutParams
            }
            "检查" -> {
                textView.text = ""
                textView.setBackgroundResource(R.drawable.shape_circle_group_000000)
                val layoutParams: RelativeLayout.LayoutParams =
                    textView.layoutParams as RelativeLayout.LayoutParams
                layoutParams.width = ConvertUtils.dp2px(8f)
                layoutParams.height = ConvertUtils.dp2px(8f)
                textView.layoutParams = layoutParams
            }
            else -> {
                if (TextUtils.isEmpty(item))
                    textView.text = ""
                else
                    textView.text = item + "公里"
            }
        }
    }
}