package com.bossed.waej.adapter

import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.TimeUtils
import com.bossed.waej.R
import com.bossed.waej.javebean.ReplenishmentOrderBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.text.ParseException
import java.text.SimpleDateFormat

class ShopRepairAdapter(layoutId: Int, list: ArrayList<ReplenishmentOrderBean>) :
    BaseQuickAdapter<ReplenishmentOrderBean, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder, item: ReplenishmentOrderBean) {
        helper.setText(R.id.tv_name_delivery_order, item.shopName)
            .setText(R.id.tv_order_id, "订单号：" + item.orderId)
            .setText(R.id.tv_order_time, "下单时间：" + item.addTime)
            .setText(R.id.tv_confirm_time, "送达时间：" + item.confirmTime)
            .addOnClickListener(R.id.tv_check_detail)
        var diff: Long = 0
        try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date1 = format.format(TimeUtils.string2Millis(item.confirmTime))
            val date2 = format.format(TimeUtils.string2Millis(item.addTime))
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val d1 = sdf.parse(date1)
            val d2 = sdf.parse(date2)
            diff = d1.time - d2.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        helper.getView<TextView>(R.id.tv_count_down).text = if (diff > 1000 * 60 * 60 * 72)
            "已超时"
        else
            "准时送达"
        helper.getView<View>(R.id.tv_confirm_time).visibility = View.VISIBLE
        helper.getView<View>(R.id.tv_count_down_date).visibility = View.GONE
    }
}