package com.bossed.waej.adapter

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.bossed.waej.R
import com.bossed.waej.javebean.PartListRow
import com.bossed.waej.util.TextChangedListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SelectPartAdapter(mutableList: MutableList<PartListRow>, private val type: Int) :
    BaseQuickAdapter<PartListRow, BaseViewHolder>(R.layout.layout_item_sel_part, mutableList) {
    override fun convert(helper: BaseViewHolder, item: PartListRow) {
        helper.setText(R.id.tv_name, item.name)
            .setText(R.id.tv_oe, "OE号：${item.oe}")
            .setText(R.id.tv_code, "自编码：${item.code}")
            .setText(R.id.tv_brand, "品牌/产地：${item.brand}")
            .setText(R.id.tv_model, "规格/型号：${item.model}")
            .setText(
                R.id.tv_price,
                if (type == 1) "上次进货价：${item.purchasePrice}" else "当前库存：${item.partStore!!.quantity}"
            )
            .setText(R.id.et_num, item.num.toString())
            .addOnClickListener(R.id.iv_add)
            .addOnClickListener(R.id.iv_reduce)
        helper.getView<View>(R.id.iv_add).visibility = if (type == 1) View.VISIBLE else View.GONE
        helper.getView<View>(R.id.iv_sel).visibility = if (type == 2) View.VISIBLE else View.GONE
        val iv_reduce = helper.getView<ImageView>(R.id.iv_reduce)
        val et_num = helper.getView<EditText>(R.id.et_num)
        iv_reduce.visibility = if (item.num!! > 0) View.VISIBLE else View.GONE
        et_num.visibility = if (item.num!! > 0) View.VISIBLE else View.GONE
        et_num.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_num.isFocused) {
                    item.num = if (TextUtils.isEmpty(s)) 0.0 else s.toDouble()
                    listener!!.invoke()
                }
            }
        })
    }

    private var listener: (() -> Unit)? = null

    fun setOnChangeListener(listener: (() -> Unit)) {
        this.listener = listener
    }
}