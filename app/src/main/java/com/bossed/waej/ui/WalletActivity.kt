package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.http.UrlConstants.WalletBalanceUrl
import com.bossed.waej.javebean.WalletResponse
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_wallet.*
import java.math.BigDecimal

class WalletActivity : BaseActivity(), View.OnClickListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_wallet
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_wallet)
    }

    override fun initListener() {
        tb_wallet.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                startActivity(Intent(this@WalletActivity, BalanceDetailsActivity::class.java))
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_commit -> {//提现
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (tv_balance.text.toString() == "0.00") {
                    ToastUtils.showShort("可提现金额为0")
                    return
                }
                val intent = Intent(this, CashOutActivity::class.java)
                intent.putExtra("merchantNo", getIntent().getStringExtra("merchantNo"))
                intent.putExtra("balance", tv_balance.text.toString())
                startActivity(intent)
            }

            R.id.tv_account -> {//银行账号
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, RecordsAccountActivity::class.java)
                intent.putExtra("merchantNo", getIntent().getStringExtra("merchantNo"))
                intent.putExtra("orgCode", getIntent().getStringExtra("orgCode"))
                startActivity(intent)
            }

            R.id.tv_records -> {//提现记录
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this, CashOutRecordsActivity::class.java))
            }

            R.id.tv_freeze -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val popWindow = PopWindow.Builder(this).setView(R.layout.layout_pop_wallet)
                    .setWidthAndHeight(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    ).setOutsideTouchable(true)
                    .setAnimStyle(R.style.CenterAnimation)
                    .setChildrenView { contentView, pop ->
                        contentView.setOnClickListener {
                            pop.dismiss()
                        }
                    }.create()
                popWindow.isClippingEnabled = false
                popWindow.isFocusable = true
                popWindow.showOnTargetBottom(
                    tv_freeze,
                    PopWindow.RIGHT_BOTTOM,
                    0,
                    0
                )
            }
        }
    }

    private fun getInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        RetrofitUtils.get()
            .getJson(WalletBalanceUrl, HashMap(), this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, WalletResponse::class.java)
                    tv_balanceFrozen.text = t.data!!.balanceFrozen
                    tv_balance.text = t.data!!.balance
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        getInfo()
    }
}