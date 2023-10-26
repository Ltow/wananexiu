package com.bossed.waej.adapter

import android.text.TextUtils
import android.widget.EditText
import android.widget.TextView
import com.bossed.waej.R
import com.bossed.waej.javebean.PartListRow
import com.bossed.waej.util.TextChangedListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class PurchaseAdapter(data: ArrayList<PartListRow>) :
    BaseQuickAdapter<PartListRow, BaseViewHolder>(R.layout.layout_item_purchase_part, data) {
    override fun convert(helper: BaseViewHolder, item: PartListRow) {
        helper.setText(R.id.tv_name, item.name)
            .setText(R.id.tv_oe, "OE号：${item.oe}")
            .setText(
                R.id.tv_money,
                "￥${BigDecimal(item.purchasePrice).multiply(BigDecimal(item.num!!))}"
            )
            .setText(R.id.tv_content, "自编码：${item.code}  规格/型号：${item.model}  品牌/产地：${item.brand}")
            .setText(R.id.et_num, "${item.num}")
            .setText(R.id.et_price, item.purchasePrice)
            .addOnClickListener(R.id.iv_delete)
        val et_num = helper.getView<EditText>(R.id.et_num)
        val et_price = helper.getView<EditText>(R.id.et_price)
        val tv_money = helper.getView<TextView>(R.id.tv_money)
        et_num.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_num.isFocused) {
                    item.num = if (TextUtils.isEmpty(s)) 0.0 else s.toDouble()
                    tv_money.text =
                        "￥${BigDecimal(item.purchasePrice).multiply(BigDecimal(item.num!!))}"
                    listener!!.invoke()
                }
            }
        })
        et_price.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_price.isFocused) {
                    item.purchasePrice = if (TextUtils.isEmpty(s)) "0.0" else s
                    tv_money.text =
                        "￥${BigDecimal(item.purchasePrice).multiply(BigDecimal(item.num!!))}"
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