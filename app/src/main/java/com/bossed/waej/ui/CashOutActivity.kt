package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.SelectBankCardAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.customview.BottomDialog
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.http.UrlConstants.WithdrawalUrl
import com.bossed.waej.javebean.RecordsAccountResponse
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_cash_out.*
import java.math.BigDecimal

class CashOutActivity : BaseActivity(), View.OnClickListener {
    private var bankId = ""//卡id
    private var acctNo = ""//卡号
    private var nbkName = ""//卡名称

    override fun getLayoutId(): Int {
        return R.layout.activity_cash_out
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_cash_out)
        tv_balance.text = intent.getStringExtra("balance")
    }

    override fun initListener() {
        tb_cash_out.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.tv_cardNo -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                getList()
            }

            R.id.tv_all -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                et_drawAmt.setText(tv_balance.text.toString())
            }

            R.id.btn_commit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(bankId) -> ToastUtils.showShort("请选择到账银行卡")
                    TextUtils.isEmpty(et_drawAmt.text.toString()) -> ToastUtils.showShort("请输入提现金额")
                    BigDecimal(et_drawAmt.text.toString()) <= BigDecimal(0) -> ToastUtils.showShort(
                        "提现金额不能为0"
                    )

                    else -> {
                        val popWindow =
                            PopWindow.Builder(this).setView(R.layout.layout_pop_cash_confirm)
                                .setWidthAndHeight(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                ).setOutsideTouchable(true)
                                .setBackGroundLevel(0.5f)
                                .setAnimStyle(R.style.CenterAnimation)
                                .setChildrenView { contentView, pop ->
                                    contentView.findViewById<TextView>(R.id.tv_money).text =
                                        "￥${et_drawAmt.text}"
                                    contentView.findViewById<View>(R.id.tv_confirm)
                                        .setOnClickListener {
                                            if (DoubleClicksUtils.get().isFastDoubleClick)
                                                return@setOnClickListener
                                            LoadingUtils.showLoading(this, "加载中...")
                                            val params = HashMap<String, String>()
                                            params["merchantNo"] =
                                                intent.getStringExtra("merchantNo")
                                            params["drawAmt"] = et_drawAmt.text.toString()
                                            params["bankId"] = bankId
                                            params["acctNo"] = acctNo
                                            params["nbkName"] = nbkName
                                            RetrofitUtils.get()
                                                .postJson(WithdrawalUrl, params, this,
                                                    object : RetrofitUtils.OnCallBackListener {
                                                        override fun onSuccess(s: String) {
                                                            LogUtils.d("tag", s)
                                                            val t = GsonUtils.fromJson(
                                                                s,
                                                                BaseResponse::class.java
                                                            )
                                                            if (t.code == 200) {
                                                                val intent = Intent(
                                                                    this@CashOutActivity,
                                                                    CashOutDetailsActivity::class.java
                                                                )
                                                                intent.putExtra("detail", s)
                                                                startActivity(intent)
                                                                finish()
                                                            } else
                                                                ToastUtils.showShort(t.msg)
                                                        }

                                                        override fun onFailed(e: String) {
                                                            ToastUtils.showShort(e)
                                                        }
                                                    })
                                            pop.dismiss()
                                        }
                                }.create()
                        popWindow.isClippingEnabled = false
                        popWindow.isFocusable = true
                        popWindow.showAtLocation(ll_content, Gravity.CENTER, 0, 0)
                    }
                }
            }
        }
    }

    /**
     * 获取银行卡列表
     */
    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["merchantNo"] = intent.getStringExtra("merchantNo")
        params["status"] = "1"
        RetrofitUtils.get().getJson(UrlConstants.BankCardListUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, RecordsAccountResponse::class.java)
                    if (t.rows.isEmpty())
                        PopupWindowUtils.get().showSetConfirmAlertDialog(
                            this@CashOutActivity,
                            "提现银行卡为空，请先添加银行卡",
                            "去添加",
                            "#3477fc"
                        ) {
                            val intent =
                                Intent(this@CashOutActivity, AddBankCardActivity::class.java)
                            intent.putExtra("merchantNo", getIntent().getStringExtra("merchantNo"))
                            startActivity(intent)
                        }
                    else {
                        BottomDialog(this@CashOutActivity).create(R.layout.layout_pop_made_type)
                            .setCanceledOnTouchOutside(true)
                            .setViewInterface { contentView, dialog ->
                                val rv_sel_role =
                                    contentView.findViewById<RecyclerView>(R.id.rv_sel_role)
                                val layoutParams =
                                    rv_sel_role.layoutParams as LinearLayout.LayoutParams
                                layoutParams.height = ConvertUtils.dp2px(200f)
                                rv_sel_role.layoutParams = layoutParams
                                contentView.findViewById<TextView>(R.id.tv_title).text =
                                    "到账银行卡"
                                if (BarUtils.isNavBarVisible(window)) {
                                    BarUtils.setNavBarLightMode(window, true)
                                    layoutParams.bottomMargin = BarUtils.getNavBarHeight()
                                    rv_sel_role.layoutParams = layoutParams
                                }
                                rv_sel_role.layoutManager =
                                    LinearLayoutManager(this@CashOutActivity)
                                val adapter = SelectBankCardAdapter(t.rows as ArrayList, bankId)
                                adapter.bindToRecyclerView(rv_sel_role)
                                adapter.emptyView =
                                    layoutInflater.inflate(R.layout.layout_empty_view, null)
                                adapter.setOnItemClickListener { _, _, position ->
                                    bankId = t.rows[position].bankId!!
                                    acctNo = t.rows[position].cardNo!!
                                    nbkName = t.rows[position].bankName!!
                                    tv_cardNo.text =
                                        "${t.rows[position].cardNo}（${t.rows[position].bankName}）"
                                    tv_cardType.text = when (t.rows[position].cardType) {
                                        "58" -> "个人银行卡"
                                        "57" -> "公司银行卡"
                                        else -> ""
                                    }
                                    dialog.dismiss()
                                }
                                contentView.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                                    dialog.dismiss()
                                }
                            }.show()
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}