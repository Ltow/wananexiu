package com.bossed.waej.adapter

import android.view.View
import com.bossed.waej.R
import com.bossed.waej.javebean.BankAccountRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class BankAccountAdapter(data: MutableList<BankAccountRow>) :
    BaseQuickAdapter<BankAccountRow, BaseViewHolder>(R.layout.layout_item_bank_account, data) {
    override fun convert(helper: BaseViewHolder, item: BankAccountRow) {
        helper.setText(R.id.tv_name, item.name)
            .setText(R.id.tv_amount, "账面金额：${item.balance}")
            .setText(
                R.id.tv_account,
                if (item.methodName == "银行卡") "卡号：${item.account}" else "账号：${item.account}"
            )
            .setText(R.id.tv_kaihuhang, "开户行：${item.bank}")
            .setChecked(R.id.sb_person, item.status == 1)
            .addOnClickListener(R.id.iv_edit_item)
            .addOnClickListener(R.id.sb_person)
        helper.getView<View>(R.id.tv_kaihuhang).visibility =
            if (item.methodName == "银行卡") View.VISIBLE else View.GONE
        helper.getView<View>(R.id.tv_account).visibility =
            if (item.methodName == "现金") View.GONE else View.VISIBLE
    }
}