package com.bossed.waej.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.bossed.waej.R
import com.bossed.waej.javebean.PricingAddGysBean
import com.bossed.waej.javebean.SupplierInfoData
import com.bossed.waej.javebean.SupplyPriceBean
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.TextChangedListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class PricingMsgAdapter(data: MutableList<SupplyPriceBean>) :
    BaseQuickAdapter<SupplyPriceBean, BaseViewHolder>(R.layout.layout_item_sel_pricing, data) {

    @SuppressLint("ClickableViewAccessibility")
    override fun convert(helper: BaseViewHolder, item: SupplyPriceBean) {
        helper.addOnClickListener(R.id.iv_delete)
            .setText(R.id.et_gys, item.name)
            .setText(R.id.et_pj_name, item.partName)
            .setText(R.id.et_num, if (item.num == null) "" else item.num.toString())
            .setText(R.id.et_price, if (item.unitPrice == null) "" else item.unitPrice.toString())
            .setText(R.id.et_remark, item.remark)
            .setText(
                R.id.tv_money, if (item.supplyPrice == null) "" else item.supplyPrice.toString()
            )
            .addOnClickListener(R.id.iv_select)
        val editText = helper.getView<EditText>(R.id.et_gys)
        val et_pj_name = helper.getView<EditText>(R.id.et_pj_name)
        val et_num = helper.getView<EditText>(R.id.et_num)
        val et_price = helper.getView<EditText>(R.id.et_price)
        val tv_money = helper.getView<TextView>(R.id.tv_money)
        if (item.name == "公司库存") {
            et_pj_name.isEnabled = false
            helper.getView<View>(R.id.iv_select).visibility = View.VISIBLE
            et_pj_name.hint = "请选择配件"
        } else {
            et_pj_name.isEnabled = true
            helper.getView<View>(R.id.iv_select).visibility = View.GONE
            et_pj_name.hint = "请输入配件名称"
        }
        editText.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                listener!!.invoke(editText, helper.layoutPosition)
                listener2!!.invoke(s, helper.layoutPosition)
            }
        })
        editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                listener!!.invoke(editText, helper.layoutPosition)
        }
        et_pj_name.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_pj_name.isFocused)
                    item.partName = s
            }
        })
        et_num.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_num.isFocused) {
                    if (TextUtils.isEmpty(s))
                        return
                    item.num = s.toDouble()
                    if (TextUtils.isEmpty(et_price.text.toString()))
                        return
                    item.supplyPrice =
                        BigDecimal(s).multiply(BigDecimal(et_price.text.toString()))
                            .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                            .toDouble()
                    tv_money.text = item.supplyPrice.toString()
                }
            }
        })
        et_price.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_price.isFocused) {
                    if (TextUtils.isEmpty(s))
                        return
                    item.unitPrice = s.toDouble()
                    if (TextUtils.isEmpty(et_num.text.toString()))
                        return
                    item.supplyPrice =
                        BigDecimal(s).multiply(BigDecimal(et_num.text.toString()))
                            .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                            .toDouble()
                    tv_money.text = item.supplyPrice.toString()
                }
            }
        })
        val et_remark = helper.getView<EditText>(R.id.et_remark)
        et_remark.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_remark.isFocused)
                    item.remark = s
            }
        })
    }

    private var listener: ((EditText, Int) -> Unit?)? = null
    private var listener2: ((String, Int) -> Unit?)? = null

    fun setOnChangeListener(listener: ((EditText, Int) -> Unit?)) {
        this.listener = listener
    }

    fun setOnChangeListener2(listener: ((String, Int) -> Unit?)) {
        this.listener2 = listener
    }
}