package com.bossed.waej.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.UrlConstants.AdjustUrl
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_journal_occur.*

class JournalOccurActivity : BaseActivity(), View.OnClickListener {
    private var accountId = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_journal_occur
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_journal_occur)
        tv_sel_date.text = TimeUtils.getNowString()
    }

    override fun initListener() {
        tb_journal_occur.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(accountId) -> ToastUtils.showShort("请选择账号")
                    TextUtils.isEmpty(et_money.text.toString()) -> ToastUtils.showShort("请输入调整金额")
                    else -> PopupWindowUtils.get()
                        .showConfirmPop(this, "本次收入${et_money.text}元，确定后将计入日记账内，计入日记账后将不可修改、删除。") {
                            journal()
                        }
                }
            }
            R.id.tv_sel_date -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 1) {
                    tv_sel_date.text = DateFormatUtils.get().formatDateAndTime(it)
                }
            }
            R.id.ctv_sy -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_sy.isChecked = !ctv_sy.isChecked
                ctv_zc.isChecked = !ctv_sy.isChecked
            }
            R.id.ctv_zc -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_zc.isChecked = !ctv_zc.isChecked
                ctv_sy.isChecked = !ctv_zc.isChecked
            }
            R.id.tv_account -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelAccountPop(this) {
                    tv_account.text = it.name
                    accountId = it.id.toString()
                }
            }
        }
    }

    private fun journal() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["money"] = et_money.text.toString()
        params["accountId"] = accountId
        params["accountName"] = tv_account.text.toString()
        params["billTime"] = tv_sel_date.text.toString()
        params["type"] = "0"
        params["billType"] = if (ctv_sy.isChecked) 1 else 2
        params["summary"] = et_remark.text.toString()
        RetrofitUtils.get()
            .postJson(AdjustUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    if (t.code == 200)
                        finish()
                    ToastUtils.showShort(t.msg)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}