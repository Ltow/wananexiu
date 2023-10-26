package com.bossed.waej.adapter

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.bossed.waej.R
import com.bossed.waej.javebean.ItemDispatchBean
import com.bossed.waej.javebean.OrderRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class OrderListAdapter(orderStatus: Int, data: ArrayList<OrderRow>) :
    BaseQuickAdapter<OrderRow, BaseViewHolder>(R.layout.layout_item_service_order, data) {
    private var orderStatus = 0//0是服务中，1是已完工
//    private var date = ""

    init {
        this.orderStatus = orderStatus
    }

    override fun convert(helper: BaseViewHolder, item: OrderRow) {
//        if (date != DateFormatUtils.get().formatDate(item.createTime)) {
//            date = DateFormatUtils.get().formatDate(item.createTime)
//            helper.getView<View>(R.id.tv_order_date).visibility = View.VISIBLE
//        } else {
//            helper.getView<View>(R.id.tv_order_date).visibility = View.GONE
//        }
        if (helper.adapterPosition == 0) {
            helper.getView<View>(R.id.tv_order_date).visibility = View.VISIBLE
        }
        val sbFZ = StringBuilder()
        for (row: ItemDispatchBean in item.dispatchVoList) {
            if (row.type == "1") {
                if (sbFZ.isNotEmpty())
                    sbFZ.append(",")
                sbFZ.append(row.staffName)
            }
        }
        helper.setText(
            R.id.tv_order_date,
            if (orderStatus == 0) item.updateTime else item.receiveTime
        )
            .setText(R.id.tv_che_no, item.cardNo)
            .setText(R.id.tv_model_item, item.carName)
            .setText(R.id.tv_person_ll, "负责人：$sbFZ")
            .setText(R.id.tv_name, "${item.customerName}\b${item.customerPhone}")
            .setText(R.id.tv_list_no, "订单号：${item.orderSn}")
            .setText(R.id.tv_total, "￥${item.orderMoney}")
            .addOnClickListener(R.id.tv_edit)
        when (orderStatus) {
            0 -> {
                helper.getView<TextView>(R.id.tv_status).text = "服务中"
                helper.getView<TextView>(R.id.tv_status).setTextColor(Color.parseColor("#fc8a25"))
//                helper.getView<View>(R.id.tv_list_no).visibility = View.VISIBLE
            }
            1 -> {
                helper.getView<TextView>(R.id.tv_status).text = "已完成"
                helper.getView<TextView>(R.id.tv_status).setTextColor(Color.parseColor("#999999"))
//                helper.getView<View>(R.id.tv_list_no).visibility = View.VISIBLE
            }
        }
    }

//    fun onRefresh() {
//        date = ""
//    }
}