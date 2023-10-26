package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.UrlConstants.AdjustUrl
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_balance_adj.*
import java.math.BigDecimal

class BalanceAdjActivity : BaseActivity(), View.OnClickListener {
    private var accountId = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_balance_adj
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_balance_adj)
    }

    override fun initListener() {
        tb_balance_adj.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                startActivity(Intent(this@BalanceAdjActivity, AdjRecordActivity::class.java))
            }
        })
        et_adj.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_adj.isFocused) {
                    if (TextUtils.isEmpty(accountId)) {
                        ToastUtils.showShort("请先选择要调整的账号")
                        return
                    }
                    if (TextUtils.isEmpty(s) || s == "-" || s == "+")
                        return
                    tv_total.text =
                        BigDecimal(tv_dq_balance.text.toString()).add(BigDecimal(s))
                            .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                            .toString()
                }
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
                    TextUtils.isEmpty(accountId) -> ToastUtils.showShort("请先选择要调整的账号")
                    TextUtils.isEmpty(et_adj.text.toString()) -> ToastUtils.showShort("请输入调整金额")
                    else -> PopupWindowUtils.get()
                        .showConfirmPop(
                            this,
                            "当前余额${tv_dq_balance.text}元，调整后余额${tv_total.text}元确定后将计入日记账内计入日记账后将不可修改、删除。"
                        ) {
                            adjust()
                        }
                }
            }

            R.id.tv_account -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelAccountPop(this) {
                    tv_account.text = it.name
                    accountId = it.id.toString()
                    tv_dq_balance.text = it.balance
                }
            }
        }
    }

    private fun adjust() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["money"] = et_adj.text.toString()
        params["accountId"] = accountId
        params["accountName"] = tv_account.text.toString()
        params["billTime"] = TimeUtils.getNowString()
        params["type"] = "1"
        params["summary"] = et_zy.text.toString()
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