package com.bossed.waej.adapter

import android.app.Activity
import android.graphics.Color
import android.widget.TextView
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.GoodsSortData
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kyleduo.switchbutton.SwitchButton

class SupportSortAdapter(mutableList: MutableList<GoodsSortData>) :
    BaseQuickAdapter<GoodsSortData, BaseViewHolder>(
        R.layout.layout_item_support_sort,
        mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: GoodsSortData) {
        helper.setText(R.id.tv_name, item.name)
            .setChecked(R.id.sb_person, item.status == 1)
            .addOnClickListener(R.id.iv_edit)
            .addOnClickListener(R.id.iv_delete)
        val sb = helper.getView<SwitchButton>(R.id.sb_person)
        sb.setOnClickListener {
            if (!sb.isChecked)
                PopupWindowUtils.get().showConfirmAndCancelPop(
                    mContext as Activity,
                    "确定禁用此类别？",
                    object : PopupWindowUtils.OnConfirmAndCancelListener {
                        override fun onConfirm() {
                            update(item.id, 0, sb)
                        }

                        override fun onCancel() {
                            sb.isChecked = true
                        }
                    })
            else
                PopupWindowUtils.get().showConfirmAndCancelPop(
                    mContext as Activity,
                    "确定启用此类别？",
                    object : PopupWindowUtils.OnConfirmAndCancelListener {
                        override fun onConfirm() {
                            update(item.id, 1, sb)
                        }

                        override fun onCancel() {
                            sb.isChecked = false
                        }
                    })
        }
    }

    private fun update(id: Int, status: Int, sb: SwitchButton) {
        LoadingUtils.showLoading(mContext as Activity, "加载中...")
        val params = HashMap<String, Any>()
        params["id"] = id
        params["status"] = status
        RetrofitUtils.get().putJson(
            UrlConstants.GoodsSortUrl, params, mContext as Activity,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    ToastUtils.showShort(t.msg)
                    if (t.code != 200)
                        sb.isChecked = !sb.isChecked
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}