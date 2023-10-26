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
import kotlinx.android.synthetic.main.activity_cost_entry.*

class CostEntryActivity : BaseActivity(), View.OnClickListener {
    private var expenseId = ""
    private var accountId = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_cost_entry
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_cost_entry)
        tv_date_sel.text = DateFormatUtils.get().formatDate(TimeUtils.getNowDate())
    }

    override fun initListener() {
        tb_cost_entry.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {

            }

            override fun onRightClick(view: View?) {

            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(expenseId) -> ToastUtils.showShort("请选择费用类别")
                    TextUtils.isEmpty(accountId) -> ToastUtils.showShort("请选择银行账号")
                    TextUtils.isEmpty(tv_date_sel.text.toString()) -> ToastUtils.showShort("请选择付款日期")
                    TextUtils.isEmpty(et_money.text.toString()) -> ToastUtils.showShort("请输入付款金额")
                    else -> entry()
                }
            }
            R.id.tv_cost_type -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelCostTypePop(this) {
                    tv_cost_type.text = it.name
                    expenseId = it.id
                }
            }
            R.id.tv_account -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelAccountPop(this) {
                    tv_account.text = it.name
                    accountId = it.id.toString()
                }
            }
            R.id.tv_date_sel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_date_sel.text = DateFormatUtils.get().formatDate(it)
                }
            }
        }
    }

    private fun entry() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["money"] = et_money.text.toString()
        params["expenseName"] = tv_cost_type.text.toString()
        params["expenseId"] = expenseId
        params["accountId"] = accountId
        params["accountName"] = tv_account.text.toString()
        params["billTime"] = tv_date_sel.text.toString()
        params["type"] = "3"
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