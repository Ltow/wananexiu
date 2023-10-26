package com.bossed.waej.adapter

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import com.bossed.waej.R
import com.bossed.waej.javebean.CustomerBillRow
import com.bossed.waej.util.DateFormatUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ReceivableInfoAdapter(data: MutableList<CustomerBillRow>) :
    BaseQuickAdapter<CustomerBillRow, BaseViewHolder>(R.layout.layout_item_cope_with_info, data) {
    override fun convert(helper: BaseViewHolder, item: CustomerBillRow) {
        helper.setText(R.id.tv_name, item.summary)
            .setText(R.id.tv_moneyPay, "￥${item.money}")
            .setText(
                R.id.tv_order_id,
                "单号：${if (TextUtils.isEmpty(item.businessOrderSn)) item.orderSn else item.businessOrderSn}"
            )
            .setText(R.id.tv_date, DateFormatUtils.get().formatDate(item.billTime))
            .setText(R.id.tv_balance, "期末余额：￥${item.balance}")
            .setText(R.id.tv_discount, "(包含减免金额${item.discount}元)")
            .addOnClickListener(R.id.tv_order_id)
        helper.getView<View>(R.id.tv_discount).visibility =
            if (item.discount.toDouble() == 0.0) View.GONE else View.VISIBLE
        helper.setTextColor(
            R.id.tv_moneyPay,
            if (item.money.toDouble() < 0) Color.parseColor("#FF9600") else Color.parseColor("#333333")
        )
            .setTextColor(
                R.id.tv_discount,
                if (item.money.toDouble() < 0) Color.parseColor("#FF9600") else Color.parseColor("#333333")
            )
    }
}