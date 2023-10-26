package com.bossed.waej.adapter

import android.app.Activity
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.widget.TextView
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.SelfPackageRow
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kyleduo.switchbutton.SwitchButton
import kotlinx.android.synthetic.main.activity_add_shop_item.et_briefIntroduction
import kotlinx.android.synthetic.main.activity_add_shop_item.et_details
import kotlinx.android.synthetic.main.activity_add_shop_item.et_marketPrice
import kotlinx.android.synthetic.main.activity_add_shop_item.et_memberPrice
import kotlinx.android.synthetic.main.activity_add_shop_item.et_name
import kotlinx.android.synthetic.main.activity_add_shop_item.et_platformSettlePrice

/**
 * @param type 0:私域 1:公域
 */
class SelfPackageAdapter(mutableList: MutableList<SelfPackageRow>, private val type: Int) :
    BaseQuickAdapter<SelfPackageRow, BaseViewHolder>(
        R.layout.layout_item_self_support,
        mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: SelfPackageRow) {
        when (type) {
            0 -> {
                helper.setText(
                    R.id.tv_content,
                    "套餐简介：${if (TextUtils.isEmpty(item.briefIntroduction)) item.name else item.briefIntroduction}"
                )
                    .setText(R.id.tv_name, item.name)
                    .setText(R.id.tv_retail_price, "￥${item.virtualPrice}")
                    .setText(R.id.tv_sj, "￥${item.marketPrice}")
                    .setText(R.id.tv_vip, "会员价:￥${item.memberPrice}")
                    .setChecked(R.id.sb_person, item.status == 1)
                    .setText(R.id.tv_status, if (item.status == 1) "已上架" else "已下架")
                    .setTextColor(
                        R.id.tv_status,
                        if (item.status == 1) Color.parseColor("#2c6ae0") else Color.RED
                    )
                    .addOnClickListener(R.id.iv_edit)
            }

            1 -> {
                helper.setText(
                    R.id.tv_content,
                    "套餐简介：${if (TextUtils.isEmpty(item.briefIntroduction)) item.name else item.briefIntroduction}"
                )
                    .setText(R.id.tv_name, item.name)
                    .setText(R.id.tv_retail_price, "￥${item.publicVirtualPrice}")
                    .setText(R.id.tv_sj, "￥${item.publicPrice}")
                    .setText(R.id.tv_vip, "会员价:￥${item.memberPrice}")
                    .setChecked(R.id.sb_person, item.publicStatus == 1)
                    .setText(R.id.tv_status, if (item.publicStatus == 1) "已上架" else "已下架")
                    .setTextColor(
                        R.id.tv_status,
                        if (item.publicStatus == 1) Color.parseColor("#2c6ae0") else Color.parseColor(
                            "#999999"
                        )
                    )
                    .addOnClickListener(R.id.iv_edit)
            }
        }
        val textView = helper.getView<TextView>(R.id.tv_retail_price)
        textView.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        Glide.with(mContext).load(
            if (!TextUtils.isEmpty(item.mainPicture)) item.mainPicture
            else {
                when (item.cateId) {
                    2 -> R.mipmap.icon_mrl
                    1 -> R.mipmap.icon_byl
                    3 -> R.mipmap.icon_ltl
                    else -> R.mipmap.icon_car_logo
                }
            }
        ).into(helper.getView(R.id.iv_head))
        val sb_person = helper.getView<SwitchButton>(R.id.sb_person)
        val tv_status = helper.getView<TextView>(R.id.tv_status)
        sb_person.setOnClickListener {
            if (!sb_person.isChecked)
                PopupWindowUtils.get().showConfirmAndCancelPop(
                    mContext as Activity,
                    "是否确定下架此项目？",
                    object : PopupWindowUtils.OnConfirmAndCancelListener {
                        override fun onConfirm() {
                            update(item.id!!, 0, sb_person, tv_status)
                        }

                        override fun onCancel() {
                            sb_person.isChecked = true
                        }
                    })
            else
                PopupWindowUtils.get().showConfirmAndCancelPop(
                    mContext as Activity,
                    "是否确定上架此项目？",
                    object : PopupWindowUtils.OnConfirmAndCancelListener {
                        override fun onConfirm() {
                            update(item.id!!, 1, sb_person, tv_status)
                        }

                        override fun onCancel() {
                            sb_person.isChecked = false
                        }
                    })
        }
    }

    private fun update(id: String, status: Int, sb: SwitchButton, tv_status: TextView) {
        LoadingUtils.showLoading(mContext as Activity, "加载中...")
        val params = HashMap<String, Any>()
        params["id"] = id
        when (type) {
            0 -> params["status"] = status
            1 -> params["publicStatus"] = status
        }
        RetrofitUtils.get()
            .putJson(UrlConstants.FreeItemUrl, params, mContext as Activity,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        LogUtils.d("tag", s)
                        val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                        if (t.code != 200)
                            sb.isChecked = !sb.isChecked
                        else {
                            when (type) {
                                0 -> tv_status.setTextColor(if (status == 1) Color.parseColor("#2c6ae0") else Color.RED)
                                1 -> tv_status.setTextColor(
                                    if (status == 1) Color.parseColor("#2c6ae0") else Color.parseColor(
                                        "#999999"
                                    )
                                )
                            }

                            tv_status.text = if (status == 1) "已上架" else "已下架"
                        }
                        ToastUtils.showShort(t.msg)
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }
}