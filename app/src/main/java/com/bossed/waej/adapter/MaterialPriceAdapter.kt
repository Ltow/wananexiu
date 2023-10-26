package com.bossed.waej.adapter

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.bossed.waej.R
import com.bossed.waej.javebean.JieCheItem
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MaterialPriceAdapter(data: ArrayList<JieCheItem>) :
    BaseQuickAdapter<JieCheItem, BaseViewHolder>(
        R.layout.layout_item_material_price_xm,
        data
    ) {
    override fun convert(helper: BaseViewHolder, item: JieCheItem) {
        helper.setText(R.id.tv_peij_name, item.itemName)
            .setText(R.id.tv_num, "X${item.num}")
            .setText(R.id.tv_oe, "OE号：${item.oem}")
            .setText(R.id.tv_bz, "备注：${item.remark}")
            .setText(R.id.tv_dj, "￥${item.unitPrice}")
            .setText(R.id.tv_gsf, "￥${item.serviceFee}")
            .setText(R.id.tv_xj, "￥${item.itemMoney}")
            .addOnClickListener(R.id.tv_supplier)
            .addOnClickListener(R.id.tv_new_supplier)
        if (item.supplyPriceList == null)
            return
        helper.getView<View>(R.id.rl_gys_price).visibility =
            if (item.supplyPriceList.isNotEmpty()) View.VISIBLE else View.GONE
        val price = helper.getView<EditText>(R.id.tv_dj_1)
        val num = helper.getView<EditText>(R.id.tv_gsf_1)
        val total = helper.getView<TextView>(R.id.tv_xj_1)
        if (item.supplyPriceList.isNotEmpty()) {
            helper.setText(R.id.tv_supplier, "供应商：${item.supplyPriceList[0].name}")
            price.setText(item.supplyPriceList[0].unitPrice.toString())
            num.setText(item.supplyPriceList[0].num.toString())
            total.text = item.supplyPriceList[0].supplyPrice.toString()
        }
        price.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s) || TextUtils.isEmpty(num.text.toString()))
                    return
//                item.supplyPriceList[0].unitPrice = s.toString().toDouble()
//                item.supplyPriceList[0].supplyPrice =
//                    s.toString().toDouble() * num.text.toString().toDouble()
                listener!!.onChange(helper.layoutPosition, num.text.toString(), s.toString())
                total.text =
                    String.format("%.2f", s.toString().toDouble() * num.text.toString().toDouble())

            }
        })
        num.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s) || TextUtils.isEmpty(price.text.toString()))
                    return
//                item.supplyPriceList[0].num = s.toString().toDouble()
//                item.supplyPriceList[0].supplyPrice =
//                    s.toString().toDouble() * price.text.toString().toDouble()
                listener!!.onChange(helper.layoutPosition, s.toString(), price.text.toString())
                total.text =
                    String.format(
                        "%.2f",
                        s.toString().toDouble() * price.text.toString().toDouble()
                    )
            }
        })
    }

    private var listener: OnNumChangeListener? = null

    fun setOnNumChangeListener(onNumChangeListener: OnNumChangeListener) {
        this.listener = onNumChangeListener
    }

    interface OnNumChangeListener {
        fun onChange(position: Int, num: String, price: String)
    }
}