package com.bossed.waej.adapter

import android.app.Activity
import android.text.TextUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.PersonRow
import com.bossed.waej.util.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kyleduo.switchbutton.SwitchButton

class PersonAdapter(data: ArrayList<PersonRow>) :
    BaseQuickAdapter<PersonRow, BaseViewHolder>(R.layout.layout_item_person, data) {

    override fun convert(helper: BaseViewHolder, item: PersonRow) {
        helper.setText(R.id.tv_dispatch_name, item.name)
            .setText(R.id.tv_seniority, "工龄：${item.workingLife}年")
            .setText(R.id.tv_skill_msg, "描述：${item.skillDescription}")
            .setText(R.id.tv_phone, item.phone)
            .setText(R.id.tv_role, "角色：${item.roleName}")
            .setVisible(R.id.tv_phone, !TextUtils.isEmpty(item.phone))
            .addOnClickListener(R.id.rl_delete)
            .addOnClickListener(R.id.tv_phone)
            .addOnClickListener(R.id.ll_content)
            .setChecked(R.id.sb_person, item.disableFlag == 0)
        if (!TextUtils.isEmpty(item.avatar))
            GlideUtils.get().loadHead(mContext, item.avatar, helper.getView(R.id.iv_person))
        val sb_person = helper.getView<SwitchButton>(R.id.sb_person)
        sb_person.setOnClickListener {
            if (!sb_person.isChecked)
                PopupWindowUtils.get()
                    .showConfirmAndCancelPop(
                        mContext as Activity, "是否禁用此员工？",
                        object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                update(item.id, 1, sb_person, item.roleName, item.userRole)
                            }

                            override fun onCancel() {
                                sb_person.isChecked = true
                            }
                        })
            else
                PopupWindowUtils.get()
                    .showConfirmAndCancelPop(
                        mContext as Activity, "是否启用此员工？",
                        object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                update(item.id, 0, sb_person, item.roleName, item.userRole)
                            }

                            override fun onCancel() {
                                sb_person.isChecked = false
                            }
                        })
        }
    }

    private val update =
        { id: Int, disableFlag: Int, sb_person: SwitchButton, roleName: String, userRole: Int ->
            LoadingUtils.showLoading(mContext as Activity, "加载中...")
            val params = HashMap<String, Any>()
            params["id"] = id
            params["disableFlag"] = disableFlag
            params["roleName"] = roleName
            params["userRole"] = userRole
            RetrofitUtils.get().putJson(
                UrlConstants.UpdatePersonUrl, params, mContext as Activity,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                        ToastUtils.showShort(t.msg)
                        if (t.code != 200)
                            sb_person.isChecked = !sb_person.isChecked
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
        }
}