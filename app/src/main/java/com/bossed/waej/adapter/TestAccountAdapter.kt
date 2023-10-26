package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.TestAccountData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class TestAccountAdapter(mutableList: MutableList<TestAccountData>) :
    BaseQuickAdapter<TestAccountData, BaseViewHolder>(
        R.layout.layout_item_test_account,
        mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: TestAccountData) {
        helper.setText(R.id.tv_name, item.nickName)
            .setText(R.id.tv_phone, item.userName)
            .setText(R.id.tv_status, if (item.isOnline) "在线" else "离线")
            .setImageResource(
                R.id.iv_status,
                if (item.isOnline) R.drawable.shape_circle_11ea3a else R.drawable.shape_circle_999999
            )
    }
}