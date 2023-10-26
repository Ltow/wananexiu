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

class CheckAdapter(mutableList: MutableList<PartListRow>) :
    BaseQuickAdapter<PartListRow, BaseViewHolder>(R.layout.layout_item_check, mutableList) {
    override fun convert(helper: BaseViewHolder, item: PartListRow) {
        helper.setText(R.id.tv_name, item.name)
            .setText(R.id.tv_code, "自编码：${item.code}")
            .setText(R.id.tv_model, "型号/规格：${item.model}")
            .setText(R.id.rv_brand, "品牌/产地：${item.brand}")
            .setText(R.id.tv_zm_kc, "账面库存：${item.partStore!!.quantity}")
            .setText(R.id.tv_zm_dj, "账面单价：${item.partStore!!.cost}")
            .setText(R.id.tv_zm_je, "账面金额：${item.partStore!!.amount}")
            .setText(R.id.et_num, item.partStore!!.adjustQuantity)
            .setText(R.id.et_price, item.partStore!!.adjustCost)
            .setText(R.id.tv_money, item.partStore!!.adjustAmount)
        val et_num = helper.getView<EditText>(R.id.et_num)
        val et_price = helper.getView<EditText>(R.id.et_price)
        et_num.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_num.isFocused) {
                    item.partStore!!.adjustQuantity =
                        if (s == "." || TextUtils.isEmpty(s)) "0.0" else s
                    val total = BigDecimal(
                        if (s == "." || TextUtils.isEmpty(s)) 0.0 else s.toDouble()
                    ).multiply(
                        BigDecimal(
                            if (et_price.text.toString() == "." || TextUtils.isEmpty(et_price.text.toString())) 0.0 else et_price.text.toString()
                                .toDouble()
                        )
                    ).setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    item.partStore!!.adjustAmount = total.toString()
                    helper.getView<TextView>(R.id.tv_money).text = total.toString()
                }
            }
        })
        et_price.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_price.isFocused) {
                    item.partStore!!.adjustCost = if (s == "." || TextUtils.isEmpty(s)) "0.0" else s
                    val total = BigDecimal(
                        if (et_num.text.toString() == "." || TextUtils.isEmpty(et_num.text.toString())) 0.0 else et_num.text.toString()
                            .toDouble()
                    ).multiply(BigDecimal(if (s == "." || TextUtils.isEmpty(s)) 0.0 else s.toDouble()))
                        .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    item.partStore!!.adjustAmount = total.toString()
                    helper.getView<TextView>(R.id.tv_money).text = total.toString()
                }
            }
        })
    }
}