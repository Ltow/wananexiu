package com.bossed.waej.adapter

import android.text.TextUtils
import android.widget.EditText
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.javebean.PurchaseDetail
import com.bossed.waej.util.TextChangedListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class PurchaseBackAdapter(mutableList: MutableList<PurchaseDetail>) :
    BaseQuickAdapter<PurchaseDetail, BaseViewHolder>(
        R.layout.layout_item_purchase_back, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: PurchaseDetail) {
        helper.setText(R.id.tv_name, item.part.name)
            .setText(R.id.tv_oe, "OE号：${item.part.oe}")
            .setText(R.id.tv_quantity, "x${item.quantity}")
            .setText(R.id.tv_price, item.cost)
            .setText(R.id.tv_amount, item.amount)
            .setText(R.id.tv_balance_quantity, item.balanceQuantity)
            .setText(R.id.et_num, item.num ?: "0")
            .setText(
                R.id.tv_content,
                "自编码：${item.part.code}  规格/型号：${item.part.model}  品牌/产地：${item.part.brand}"
            )
            .setText(R.id.tv_back_money, item.backAmount)
        val editText = helper.getView<EditText>(R.id.et_num)
        editText.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (editText.isFocused) {
                    listener!!.invoke(s, helper.layoutPosition)
                    if (TextUtils.isEmpty(s))
                        return
                    helper.setText(
                        R.id.tv_back_money, BigDecimal(s).multiply(BigDecimal(item.cost))
                            .setScale(2, BigDecimal.ROUND_HALF_DOWN).toString()
                    )
                }
            }
        })
    }

    private var listener: ((String, Int) -> Unit)? = null

    fun setOnChangeListener(listener: ((String, Int) -> Unit)) {
        this.listener = listener
    }
}