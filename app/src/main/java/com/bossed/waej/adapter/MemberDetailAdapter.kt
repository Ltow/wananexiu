package com.bossed.waej.adapter

import android.annotation.SuppressLint
import android.text.Html
import android.text.TextUtils
import com.bossed.waej.R
import com.bossed.waej.javebean.KmOrderGood
import com.bossed.waej.util.GlideUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MemberDetailAdapter(data: MutableList<KmOrderGood>) :
    BaseQuickAdapter<KmOrderGood, BaseViewHolder>(R.layout.layout_item_member_detail, data) {

    @SuppressLint("SetJavaScriptEnabled")
    override fun convert(helper: BaseViewHolder, item: KmOrderGood) {
        helper.setText(R.id.tv_name, item.goodsName)
            .setText(
                R.id.tv_remark,
                "简介：${Html.fromHtml(if (TextUtils.isEmpty(item.summary)) "" else item.summary)}"
            )
            .setText(R.id.tv_num, "x${item.goodsNum}")
        GlideUtils.get().loadItemPic(
            mContext,
            if (TextUtils.isEmpty(item.goodsLogo)) "" else item.goodsLogo!!,
            helper.getView(R.id.iv_item)
        )
    }
}