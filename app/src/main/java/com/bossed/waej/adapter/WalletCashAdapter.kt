package com.bossed.waej.adapter

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.bossed.waej.R
import com.bossed.waej.javebean.CashBill
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class WalletCashAdapter(layoutId: Int, list: MutableList<CashBill>) :
    BaseQuickAdapter<CashBill, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: CashBill?) {
        helper?.setText(R.id.tv_name_wallet, item?.remark)
            ?.setText(R.id.tv_date_wallet, item?.createTime)
            ?.setText(R.id.tv_money_num_wallet, item?.applyMoney.toString())
        val textView = helper?.getView<TextView>(R.id.tv_status_cash)
        textView?.visibility = View.VISIBLE
        when (item?.status) {
            1 -> {
                textView?.text = "申请提现"
                textView?.setTextColor(Color.parseColor("#FC8A25"))
            }
            2 -> {
                textView?.text = "审批通过"
                textView?.setTextColor(Color.parseColor("#18D638"))
            }
            3 -> {
                textView?.text = "审批不通过"
                textView?.setTextColor(Color.parseColor("#FE3C40"))
            }
            else -> {
                textView?.visibility = View.GONE
            }
        }
    }
}