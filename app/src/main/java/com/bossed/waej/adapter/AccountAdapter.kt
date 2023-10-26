package com.bossed.waej.adapter

import android.widget.ImageView
import android.widget.TextView
import com.bossed.waej.R
import com.bossed.waej.javebean.User
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AccountAdapter(data: MutableList<User>) :
    BaseQuickAdapter<User, BaseViewHolder>(R.layout.layout_item_account, data) {
    override fun convert(helper: BaseViewHolder, item: User) {
        helper.setText(R.id.tv_account, "账号：${item.userName}")
            .setText(R.id.tv_user, "使用员工：${item.nickName}")
        val tv_status = helper.getView<TextView>(R.id.tv_status)
        val iv_status = helper.getView<ImageView>(R.id.iv_status)
        when (item.status) {
            "0" -> {
                tv_status.text = "离线"
                iv_status.setImageResource(R.mipmap.icon_off_line)
            }
            "1" -> {
                tv_status.text = "禁用"
                iv_status.setImageResource(R.mipmap.icon_disabled)
            }
            "2" -> {
                tv_status.text = "在线"
                iv_status.setImageResource(R.drawable.shape_circle_00ff00)
            }
        }
    }
}