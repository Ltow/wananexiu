package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.ProfitBill
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class WalletProfitAdapter(layoutId: Int, list: MutableList<ProfitBill>) :
    BaseQuickAdapter<ProfitBill, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: ProfitBill?) {
        helper?.setText(R.id.tv_name_wallet, item?.remark)
            ?.setText(R.id.tv_date_wallet, item?.createTime)
            ?.setText(R.id.tv_money_num_wallet, item?.amount.toString())
    }
}