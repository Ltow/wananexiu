package com.bossed.waej.adapter

import android.view.View
import com.bossed.waej.R
import com.bossed.waej.javebean.JieCheRow
import com.bossed.waej.util.DateFormatUtils
import com.bossed.waej.util.GlideUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ReceptionDraftAdapter(data: ArrayList<JieCheRow>) :
    BaseQuickAdapter<JieCheRow, BaseViewHolder>(
        R.layout.layout_item_jieche_draft, data
    ) {
//    private var date = ""

    override fun convert(helper: BaseViewHolder, item: JieCheRow) {
//        if (date != DateFormatUtils.get().formatDate(item.receiveTime)) {
//            date = DateFormatUtils.get().formatDate(item.receiveTime)
//            helper.getView<View>(R.id.tv_order_date).visibility = View.VISIBLE
//        } else {
//            helper.getView<View>(R.id.tv_order_date).visibility = View.GONE
//        }
        if (helper.adapterPosition == 0) {
            helper.getView<View>(R.id.tv_order_date).visibility = View.VISIBLE
        }
//        date = DateFormatUtils.get().formatDate(item.receiveTime)
        helper.setText(R.id.tv_che_no, item.cardNo)
            .setText(R.id.tv_che_type, item.carName)
            .setText(R.id.tv_contacts, item.customerName + "  " + item.customerPhone)
            .setText(R.id.tv_order_date, item.updateTime)
//            .setText(R.id.tv_money, "￥${item.realMoney}")
//            .setText(R.id.tv_total, "共${item.numItem}项")
            .addOnClickListener(R.id.tv_edit)
            .addOnClickListener(R.id.rl_delete)
        if (item.brandLogo != null)
            GlideUtils.get().loadCarLogo(mContext, item.brandLogo, helper.getView(R.id.iv_car_logo))
        else
            GlideUtils.get().loadCarLogo(mContext, "", helper.getView(R.id.iv_car_logo))
    }

//    fun onRefresh() {
//        date = ""
//    }
}