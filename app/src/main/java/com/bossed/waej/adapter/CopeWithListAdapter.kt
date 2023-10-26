package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.Ledger
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CopeWithListAdapter(data: MutableList<Ledger>, private val type: Int) :
    BaseQuickAdapter<Ledger, BaseViewHolder>(R.layout.layout_item_cope_with_list, data) {
    override fun convert(helper: BaseViewHolder, item: Ledger) {
        helper.setText(R.id.tv_name, if (type == 1) item.supplierName else item.name)
            .setText(R.id.tv_lxr, "姓名：${item.name}")
            .setText(R.id.tv_phone, "电话：${item.phone}")
            .setText(R.id.tv_moneyOwe, "￥${item.moneyOwe}")
            .setText(R.id.tv_moneyAdd, "本期增加：￥${item.moneyAdd}")
            .setText(
                R.id.tv_moneyPay,
                if (type == 1) "本期付款：￥${item.moneyPay}" else "本期收款：￥${item.moneyPay}"
            )
            .setText(R.id.tv_discount, "本期减免：￥${item.discount}")
            .setText(R.id.tv_balance, "期末余额：￥${item.balance}")
    }
}