package com.bossed.waej.adapter

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.ConvertUtils
import com.bossed.waej.R
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.javebean.Car
import com.bossed.waej.util.CalendarUtils
import com.bossed.waej.util.DateFormatUtils
import com.bossed.waej.util.TextChangedListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import wang.relish.widget.vehicleedittext.VehicleKeyboardHelper

class CarAdapter(data: MutableList<Car>) :
    BaseQuickAdapter<Car, BaseViewHolder>(R.layout.layout_item_customer_car, data) {

    override fun convert(helper: BaseViewHolder, item: Car) {
        helper.setText(R.id.tv_car_no, item.cardNo)
            .setText(R.id.tv_car_model, item.carName)
            .setText(R.id.tv_car_model1, item.carName)
            .setText(R.id.et_car_vin, item.vnCode)
            .setText(R.id.et_mileage, item.mileage)
            .setText(R.id.et_remark, item.remark)
            .setText(R.id.et_policyName, item.policyName)
            .setText(R.id.tv_sy_date, item.insureDate)
            .setText(R.id.tv_jq_date, item.insure2Date)
            .setText(R.id.tv_nj_date, item.dueDate)
            .addOnClickListener(R.id.rl_model2)
            .addOnClickListener(R.id.tv_car_no)
            .addOnClickListener(R.id.tv_car_msg)
            .addOnClickListener(R.id.iv_delete)
            .addOnClickListener(R.id.iv_scan_vin)
            .addOnClickListener(R.id.tv_search)
            .addOnClickListener(R.id.tv_sy_date)
            .addOnClickListener(R.id.iv_set_alert)
            .addOnClickListener(R.id.tv_jq_date)
            .addOnClickListener(R.id.tv_nj_date)
            .setVisible(R.id.tv_alert_bx, item.remindInsure != 0)
            .setVisible(R.id.tv_alert_by, item.remindMaintenance != 0)
            .setVisible(R.id.tv_alert_nj, item.remindDue != 0)
            .setVisible(R.id.tv_search, !TextUtils.isEmpty(item.vnCode))
        val editText = helper.getView<EditText>(R.id.tv_car_no)
        VehicleKeyboardHelper.bind(editText)
        editText.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (editText.isFocused) {
                    item.cardNo = s
                    listener!!.onEdit(helper.adapterPosition)
                }
            }
        })
        val etVin = helper.getView<EditText>(R.id.et_car_vin)
        etVin.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (etVin.isFocused) {
                    item.vnCode = s
                    helper.getView<View>(R.id.tv_search).visibility =
                        if (TextUtils.isEmpty(s)) View.GONE else View.VISIBLE
                }
            }
        })
        helper.getView<EditText>(R.id.et_mileage)
            .addTextChangedListener(object : TextChangedListener {
                override fun afterTextChange(s: String) {
                    item.mileage = s
                }
            })
        helper.getView<EditText>(R.id.et_remark)
            .addTextChangedListener(object : TextChangedListener {
                override fun afterTextChange(s: String) {
                    item.remark = s
                }
            })
        helper.getView<EditText>(R.id.et_policyName)
            .addTextChangedListener(object : TextChangedListener {
                override fun afterTextChange(s: String) {
                    item.policyName = s
                }
            })
        helper.getView<View>(R.id.tv_alert_bx).setOnClickListener {
            val popWindow = PopWindow.Builder(mContext).setView(R.layout.layout_pop_alert_bx)
                .setWidthAndHeight(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ).setOutsideTouchable(true)
                .setChildrenView { contentView, pop ->
                    contentView.findViewById<TextView>(R.id.tv_jq_date).text =
                        "交强险到期日期：${item.insure2Date}"
                    contentView.findViewById<TextView>(R.id.tv_sy_date).text =
                        "商业险到期日期：${item.insureDate}"
                    contentView.findViewById<TextView>(R.id.tv_front).text =
                        "保险到期前 ${item.advanceInsure} 天开始提醒"
                    contentView.findViewById<TextView>(R.id.tv_front_sy).text =
                        "保险到期前 ${item.advanceInsure} 天开始提醒"
                    contentView.findViewById<TextView>(R.id.tv_alert_date).text =
                        "提醒日期：${
                            DateFormatUtils.get()
                                .formatDate(
                                    CalendarUtils.get()
                                        .setDayOfAddDays(item.insure2Date, item.advanceInsure)
                                )
                        }"
                    contentView.findViewById<TextView>(R.id.tv_alert_date2).text =
                        "提醒日期：${
                            DateFormatUtils.get()
                                .formatDate(
                                    CalendarUtils.get()
                                        .setDayOfAddDays(item.insureDate, item.advanceInsure)
                                )
                        }"
                    contentView.setOnClickListener {
                        pop.dismiss()
                    }
                }.create()
            popWindow.isClippingEnabled = false
            popWindow.isFocusable = true
            popWindow.showOnTargetBottom(
                helper.getView(R.id.tv_alert_bx),
                PopWindow.CENTER_BOTTOM,
                ConvertUtils.dp2px(40f),
                0
            )
        }
        helper.getView<View>(R.id.tv_alert_by).setOnClickListener {
            val popWindow = PopWindow.Builder(mContext).setView(R.layout.layout_pop_alert_by)
                .setWidthAndHeight(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ).setOutsideTouchable(true)
                .setChildrenView { contentView, pop ->
                    contentView.findViewById<TextView>(R.id.tv_date).text =
                        "保养到期日期：${item.maintenanceDate}"
                    contentView.findViewById<TextView>(R.id.tv_front).text =
                        "到期前 ${item.advanceMaintenance} 天开始提醒"
                    contentView.findViewById<TextView>(R.id.tv_alert_date).text =
                        "提醒日期：${
                            DateFormatUtils.get().formatDate(
                                CalendarUtils.get()
                                    .setDayOfAddDays(item.maintenanceDate, item.advanceMaintenance)
                            )
                        }"
                    contentView.setOnClickListener {
                        pop.dismiss()
                    }
                }.create()
            popWindow.isClippingEnabled = false
            popWindow.isFocusable = true
            popWindow.showOnTargetBottom(
                helper.getView(R.id.tv_alert_by),
                PopWindow.CENTER_BOTTOM,
                ConvertUtils.dp2px(36f),
                0
            )
        }
        helper.getView<View>(R.id.tv_alert_nj).setOnClickListener {
            val popWindow = PopWindow.Builder(mContext).setView(R.layout.layout_pop_alert_nj)
                .setWidthAndHeight(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ).setOutsideTouchable(true)
                .setChildrenView { contentView, pop ->
                    contentView.findViewById<TextView>(R.id.tv_date).text =
                        "年检到期日期：${item.dueDate}"
                    contentView.findViewById<TextView>(R.id.tv_front).text =
                        "到期前 ${item.advanceDue} 天开始提醒"
                    contentView.findViewById<TextView>(R.id.tv_alert_date).text =
                        "提醒日期：${
                            DateFormatUtils.get()
                                .formatDate(
                                    CalendarUtils.get()
                                        .setDayOfAddDays(item.dueDate, item.advanceDue)
                                )
                        }"
                    contentView.setOnClickListener {
                        pop.dismiss()
                    }
                }.create()
            popWindow.isClippingEnabled = false
            popWindow.isFocusable = true
            popWindow.showOnTargetBottom(
                helper.getView(R.id.tv_alert_nj),
                PopWindow.CENTER_BOTTOM,
                ConvertUtils.dp2px(5f),
                0
            )
        }
    }

    private var listener: OnCarNoEditListener? = null

    fun setOnCarNoEditListener(listener: OnCarNoEditListener) {
        this.listener = listener
    }

    interface OnCarNoEditListener {
        fun onEdit(position: Int)
    }
}