package com.bossed.waej.adapter

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.TimeUtils
import com.bossed.waej.R
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.javebean.Car
import com.bossed.waej.util.CalendarUtils
import com.bossed.waej.util.DateFormatUtils
import com.bossed.waej.util.GlideUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.util.*

class CarListAdapter(data: MutableList<Car>) :
    BaseQuickAdapter<Car, BaseViewHolder>(R.layout.layout_item_car_list, data) {
    override fun convert(helper: BaseViewHolder, item: Car) {
        helper.setText(R.id.tv_car_no, item.cardNo)
            .setText(R.id.tv_model, item.carName)
        if (TextUtils.isEmpty(item.brandLogo))
            GlideUtils.get().loadCarLogo(mContext, "", helper.getView(R.id.iv_car_logo))
        else
            GlideUtils.get().loadCarLogo(mContext, item.brandLogo, helper.getView(R.id.iv_car_logo))
        val bx = helper.getView<View>(R.id.tv_alert_bx)
        val by = helper.getView<View>(R.id.tv_alert_by)
        val nj = helper.getView<View>(R.id.tv_alert_nj)
        bx.visibility = if (item.remindInsure != 0) View.VISIBLE else View.GONE
        by.visibility = if (item.remindMaintenance != 0) View.VISIBLE else View.GONE
        nj.visibility = if (item.remindDue != 0) View.VISIBLE else View.GONE
        bx.setOnClickListener {
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
            val location = IntArray(2)
            bx.getLocationOnScreen(location)
            if (ScreenUtils.getScreenHeight() - location[1] < popWindow.height)
                popWindow.showOnTargetTop(bx, PopWindow.CENTER_BOTTOM, ConvertUtils.dp2px(-36f), -5)
            else
                popWindow.showOnTargetBottom(
                    bx,
                    PopWindow.CENTER_BOTTOM,
                    ConvertUtils.dp2px(36f),
                    5
                )
        }
        by.setOnClickListener {
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
            val location = IntArray(2)
            by.getLocationOnScreen(location)
            if (ScreenUtils.getScreenHeight() - location[1] < popWindow.height)
                popWindow.showOnTargetTop(
                    by,
                    PopWindow.CENTER_BOTTOM,
                    ConvertUtils.dp2px(-45f),
                    -5
                )
            else
                popWindow.showOnTargetBottom(
                    by,
                    PopWindow.CENTER_BOTTOM,
                    ConvertUtils.dp2px(25f),
                    5
                )
        }
        nj.setOnClickListener {
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
            val location = IntArray(2)
            nj.getLocationOnScreen(location)
            if (ScreenUtils.getScreenHeight() - location[1] < popWindow.height)
                popWindow.showOnTargetTop(
                    nj,
                    PopWindow.CENTER_BOTTOM,
                    ConvertUtils.dp2px(-45f),
                    -5
                )
            else
                popWindow.showOnTargetBottom(
                    nj,
                    PopWindow.CENTER_BOTTOM,
                    ConvertUtils.dp2px(25f),
                    5
                )
        }
    }
}