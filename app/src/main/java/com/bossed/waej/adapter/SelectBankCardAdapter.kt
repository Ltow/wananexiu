package com.bossed.waej.adapter

import android.graphics.Color
import com.bossed.waej.R
import com.bossed.waej.javebean.RecordsAccountRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SelectBankCardAdapter(
    mutableList: MutableList<RecordsAccountRow>,
    private var bankId: String
) :
    BaseQuickAdapter<RecordsAccountRow, BaseViewHolder>(
        R.layout.layout_item_weelview,
        mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: RecordsAccountRow) {
        helper.setText(R.id.tv_item_weelview, "${item.cardNo}（${item.bankName}）")
            .setTextColor(
                R.id.tv_item_weelview,
                Color.parseColor(if (item.bankId == bankId) "#3477fc" else "#333333")
            )
    }
}