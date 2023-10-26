package com.bossed.waej.adapter

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.javebean.ItemDispatchBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class DispatchTiChengAdapter(
    data: List<ItemDispatchBean>,
    itemMoney: Double,
    grossProfitMoney: Double
) :
    BaseQuickAdapter<ItemDispatchBean, BaseViewHolder>(R.layout.layout_item_dispatch_tc, data) {
    private var itemMoney = 0.0
    private var grossProfitMoney = 0.0

    init {
        this.grossProfitMoney = grossProfitMoney
        this.itemMoney = itemMoney
    }

    override fun convert(helper: BaseViewHolder, item: ItemDispatchBean) {
        helper.setText(R.id.tv_item_name, item.staffName)
            .setText(R.id.et_part_scale, item.madeRate.toString())
            .setText(R.id.et_part_money, item.madeMoney.toString())
            .setText(R.id.et_hours_scale, item.madeFeeRate.toString())
            .setText(R.id.et_hours_money, item.madeFee.toString())
            .addOnClickListener(R.id.iv_delete_tc)
        var xmCommission = 0.0 //项目总提成金额
        var gsCommission = 0.0 //工时费总提成金额
        when (SPUtils.getInstance().getInt("madeType")) {
            1 -> {//1-按销售额
                xmCommission = itemMoney * SPUtils.getInstance().getFloat("madeRate")
                gsCommission = itemMoney * SPUtils.getInstance().getFloat("madeFeeRate")
            }
            2 -> {//2-按利润
                xmCommission = grossProfitMoney * SPUtils.getInstance().getFloat("madeRate")
                gsCommission = grossProfitMoney * SPUtils.getInstance().getFloat("madeFeeRate")
            }
        }
        val xmScale = helper.getView<EditText>(R.id.et_part_scale)
        val xmMoney = helper.getView<EditText>(R.id.et_part_money)
        val gsScale = helper.getView<EditText>(R.id.et_hours_scale)
        val gsMoney = helper.getView<EditText>(R.id.et_hours_money)
        xmScale.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s) || !xmScale.isFocused)
                    return
                xmMoney.setText(String.format("%.2f", xmCommission * s.toString().toFloat()))
                item.madeRate = s.toString().toDouble()
                item.madeMoney = xmCommission * s.toString().toFloat()
                onTextChangeListener?.invoke(helper.layoutPosition)
            }
        })
        xmMoney.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s) || !xmMoney.isFocused || s.toString().toFloat() <= 0.0)
                    return
                if (s.toString().toDouble() > xmCommission) {
                    ToastUtils.showShort("提成金额不能大于配件总提成金额")
                    return
                }
                xmScale.setText(String.format("%.2f", xmCommission / s.toString().toFloat()))
                item.madeRate = xmCommission / s.toString().toFloat()
                item.madeMoney = s.toString().toDouble()
                onTextChangeListener?.invoke(helper.layoutPosition)
            }
        })
        gsScale.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s) || !gsScale.isFocused)
                    return
                gsMoney.setText(String.format("%.2f", gsCommission * s.toString().toFloat()))
                item.madeFeeRate = s.toString().toDouble()
                item.madeFee = gsCommission * s.toString().toFloat()
                onTextChangeListener?.invoke(helper.layoutPosition)
            }
        })
        gsMoney.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s) || !gsMoney.isFocused || s.toString().toFloat() <= 0.0)
                    return
                if (s.toString().toDouble() > gsCommission) {
                    ToastUtils.showShort("提成金额不能大于工时费总提成金额")
                    return
                }
                gsScale.setText(String.format("%.2f", gsCommission / s.toString().toFloat()))
                item.madeFeeRate = gsCommission / s.toString().toFloat()
                item.madeFee = s.toString().toDouble()
                onTextChangeListener?.invoke(helper.layoutPosition)
            }
        })
    }

    private var onTextChangeListener: ((position: Int) -> Unit)? = null

    fun setOnTextChangeListener(onTextChangeListener: ((position: Int) -> Unit)) {
        this.onTextChangeListener = onTextChangeListener
    }
}