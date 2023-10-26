package com.bossed.waej.ui

import android.content.Intent
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
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_bank_rotation.*

class BankRotationActivity : BaseActivity(), View.OnClickListener {
    private var accountId_zc = ""
    private var accountId_zr = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_bank_rotation
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_bank_rotation)
    }

    override fun initListener() {
        tb_bank_rotation.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                startActivity(
                    Intent(
                        this@BankRotationActivity,
                        RotationHistoryActivity::class.java
                    )
                )
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_account_zc -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelAccountPop(this) {
                    tv_zc_bank.text = it.name
                    accountId_zc = it.id.toString()
                    tv_balance.text = it.balance
                }
            }
            R.id.tv_account_zr -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelAccountPop(this) {
                    if (it.id.toString() == accountId_zc) {
                        ToastUtils.showShort("收款账号和付款账号不能一致")
                        return@showSelAccountPop
                    }
                    tv_zr_bank.text = it.name
                    accountId_zr = it.id.toString()
                }
            }
            R.id.tv_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showConfirmPop(this, "是否确定清空已编辑内容？") {
                    accountId_zc = ""
                    accountId_zr = ""
                    tv_zc_bank.text = ""
                    tv_zr_bank.text = ""
                    tv_balance.text = ""
                    et_money.setText("")
                    et_remark.setText("")
                }
            }
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(accountId_zc) -> ToastUtils.showShort("请选择付款账号")
                    TextUtils.isEmpty(accountId_zr) -> ToastUtils.showShort("请选择收款账号")
                    TextUtils.isEmpty(et_money.text.toString()) -> ToastUtils.showShort("请输入转账金额")
                    else -> PopupWindowUtils.get().showConfirmPop(
                        this,
                        "确定将账号：${tv_zc_bank.text} 的${et_money.text}元，转入到账号：${tv_zr_bank.text} 吗？"
                    ) {
                        rotation()
                    }
                }
            }
        }
    }

    private fun rotation() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["money"] = et_money.text.toString()
        params["payAccountId"] = accountId_zc
        params["payAccountName"] = tv_zc_bank.text.toString()
        params["accountId"] = accountId_zr
        params["accountName"] = tv_zr_bank.text.toString()
        params["billTime"] = TimeUtils.getNowString()
        params["type"] = "2"
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