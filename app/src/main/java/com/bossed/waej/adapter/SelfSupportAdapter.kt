package com.bossed.waej.adapter

import android.app.Activity
import android.graphics.Color
import android.graphics.Paint
import android.widget.TextView
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.SelfSupportRow
import com.bossed.waej.util.GlideUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kyleduo.switchbutton.SwitchButton
import kotlinx.android.synthetic.main.activity_self_support_info.et_description
import kotlinx.android.synthetic.main.activity_self_support_info.et_marketPrice
import kotlinx.android.synthetic.main.activity_self_support_info.et_memberPrice
import kotlinx.android.synthetic.main.activity_self_support_info.et_name
import kotlinx.android.synthetic.main.activity_self_support_info.et_summary
import kotlinx.android.synthetic.main.activity_self_support_info.et_virtualPrice

/**
 * @param type 0：私域 1：公域
 */
class SelfSupportAdapter(mutableList: MutableList<SelfSupportRow>, private val type: Int) :
    BaseQuickAdapter<SelfSupportRow, BaseViewHolder>(
        R.layout.layout_item_self_support,
        mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: SelfSupportRow) {
        helper.setText(R.id.tv_name, item.name)
            .setText(
                R.id.tv_sjj, when (type) {
                    0 -> "实际售价："
                    1 -> "平台售价："
                    else -> ""
                }
            )
            .setText(R.id.tv_content, "简介：${item.summary}")
            .setText(
                R.id.tv_retail_price, when (type) {//虚拟售价
                    0 -> "￥${item.virtualPrice}"
                    1 -> "￥${item.publicVirtualPrice}"
                    else -> ""
                }
            )
            .setText(
                R.id.tv_sj, when (type) {//实际售价
                    0 -> "￥${item.marketPrice}"
                    1 -> "￥${item.publicPrice}"
                    else -> ""
                }
            )
            .setText(R.id.tv_vip, "会员价:￥${item.memberPrice}")
            .setText(
                R.id.tv_status, when (type) {
                    0 -> if (item.status == 6) "已上架" else "已下架"
                    1 -> if (item.publicStatus == 6) "已上架" else "已下架"
                    else -> ""
                }
            )
            .setTextColor(
                R.id.tv_status,
                when (type) {
                    0 -> if (item.status == 6) Color.parseColor("#2c6ae0") else Color.RED
                    1 -> if (item.publicStatus == 6) Color.parseColor("#2c6ae0") else Color.parseColor("#999999")
                    else -> Color.parseColor("#999999")
                }
            )
            .setChecked(
                R.id.sb_person, when (type) {
                    0 -> item.status == 6
                    1 -> item.publicStatus == 6
                    else -> false
                }
            )
            .addOnClickListener(R.id.iv_edit)
        GlideUtils.get().loadGoodLogo(mContext, item.mainPicture, helper.getView(R.id.iv_head))
        val textView = helper.getView<TextView>(R.id.tv_retail_price)
        textView.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        val sb_person = helper.getView<SwitchButton>(R.id.sb_person)
        sb_person.setOnClickListener {
            if (!sb_person.isChecked)
                PopupWindowUtils.get().showConfirmAndCancelPop(
                    mContext as Activity,
                    "是否确定下架此商品？",
                    object : PopupWindowUtils.OnConfirmAndCancelListener {
                        override fun onConfirm() {
                            update(
                                item.id, when (type) {
                                    0 -> 7
                                    1 -> 7
                                    else -> 0
                                }, sb_person, helper.getView(R.id.tv_status)
                            )
                        }

                        override fun onCancel() {
                            sb_person.isChecked = true
                        }
                    })
            else
                PopupWindowUtils.get().showConfirmAndCancelPop(
                    mContext as Activity,
                    "是否确定上架此商品？",
                    object : PopupWindowUtils.OnConfirmAndCancelListener {
                        override fun onConfirm() {
                            update(
                                item.id, when (type) {
                                    0 -> 6
                                    1 -> 6
                                    else -> 0
                                }, sb_person, helper.getView(R.id.tv_status)
                            )
                        }

                        override fun onCancel() {
                            sb_person.isChecked = false
                        }
                    })
        }
    }

    private fun update(id: Int, status: Int, sb: SwitchButton, tv_status: TextView) {
        LoadingUtils.showLoading(mContext as Activity, "加载中...")
        val params = HashMap<String, Any>()
        params["id"] = id
        params["productType"] = "1"
        when (type) {
            0 -> params["status"] = status
            1 -> params["publicStatus"] = status
        }
        RetrofitUtils.get()
            .putJson(UrlConstants.GoodsUrl, params, mContext as Activity,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        LogUtils.d("tag", s)
                        val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                        ToastUtils.showShort(t.msg)
                        if (t.code != 200)
                            sb.isChecked = !sb.isChecked
                        else {
                            tv_status.setTextColor(
                                when (type) {
                                    0 -> if (status == 6) Color.parseColor("#2c6ae0") else Color.RED
                                    1 -> if (status == 6) Color.parseColor("#2c6ae0") else Color.parseColor(
                                        "#999999"
                                    )

                                    else -> Color.parseColor("#999999")
                                }
                            )
                            tv_status.text = when (type) {
                                0 -> if (status == 6) "已上架" else "已下架"
                                1 -> if (status == 6) "已上架" else "已下架"
                                else -> ""
                            }
                        }
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }
}