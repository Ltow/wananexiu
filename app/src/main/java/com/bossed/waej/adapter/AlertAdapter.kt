package com.bossed.waej.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bossed.waej.R
import com.bossed.waej.javebean.AlertRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AlertAdapter(data: MutableList<AlertRow>, private var type: Int) :
    BaseQuickAdapter<AlertRow, BaseViewHolder>(R.layout.layout_item_by_alert, data) {

    override fun convert(helper: BaseViewHolder, item: AlertRow) {
        helper.setText(R.id.tv_name, item.customerName + "     " + item.customerPhone)
            .setText(R.id.tv_car_no, item.cardNo)
            .setText(R.id.tv_remark, "备注：${item.remark}")
            .setText(R.id.tv_createBy, "操作员：${item.updateBy}")
            .addOnClickListener(R.id.iv_alert)
            .addOnClickListener(R.id.tv_name)
        val tv_alert_1 = helper.getView<TextView>(R.id.tv_alert_1)
        val tv_alert_2 = helper.getView<TextView>(R.id.tv_alert_2)
        val iv_alert = helper.getView<ImageView>(R.id.iv_alert)
        iv_alert.setImageResource(
            when (item.status) {
                0 -> R.mipmap.icon_alert_w
                1 -> R.mipmap.icon_alert_y
                else -> R.mipmap.icon_alert_y
            }
        )
        helper.getView<View>(R.id.tv_createBy).visibility =
            if (item.status == 0) View.GONE else View.VISIBLE
        helper.getView<View>(R.id.tv_remark).visibility =
            if (item.status == 0) View.GONE else View.VISIBLE
        iv_alert.isEnabled = item.status == 0
        when (type) {
            0 -> {
                tv_alert_1.text = "本次保养提醒日期：${item.endDate}"
                tv_alert_2.visibility = View.GONE
            }
            1 -> {
                tv_alert_1.text = "交强险到期日期：${item.insure2Date}"
                tv_alert_2.text = "商业险到期日期：${item.endDate}"
            }
            2 -> {
                tv_alert_1.text = "年检到期日期：${item.endDate}"
                tv_alert_2.visibility = View.GONE
            }
        }
    }
}