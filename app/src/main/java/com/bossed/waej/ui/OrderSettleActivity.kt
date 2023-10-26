package com.bossed.waej.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.AddAccountAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.UrlConstants.OrderSettleUrl
import com.bossed.waej.javebean.AccountBean
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_order_settle.*
import java.math.BigDecimal

class OrderSettleActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: AddAccountAdapter
    private val list = ArrayList<AccountBean>()

    override fun getLayoutId(): Int {
        return R.layout.activity_order_settle
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_order_settle)
        rv_order_settle.layoutManager = LinearLayoutManager(this)
        list.add(AccountBean())
        adapter = AddAccountAdapter(list, 1)
        adapter.bindToRecyclerView(rv_order_settle)
        val footView = LayoutInflater.from(this).inflate(R.layout.layout_footer_view_item, null)
        footView.findViewById<TextView>(R.id.tv_add_item).setOnClickListener(this)
        footView.findViewById<TextView>(R.id.tv_add_item).text = "收款方式"
        adapter.setFooterView(footView)
        tv_total_money.text = "￥${intent.getStringExtra("total")}"
        tv_residue.text = "￥${intent.getStringExtra("moneyOwe")}"
        tv_balancePay.text = "余额抵扣：￥${intent.getStringExtra("balancePay")}"
        tv_moneyOwe.text = "剩余应收：￥${intent.getStringExtra("moneyOwe")}"
    }

    override fun initListener() {
        tb_order_settle.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        et_reduction.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (s == "-")
                    return
                tv_reduction.text =
                    "￥${
                        BigDecimal(if (TextUtils.isEmpty(s)) "0" else s).setScale(
                            2,
                            BigDecimal.ROUND_HALF_DOWN
                        )
                    }"
                countTotal()
            }
        })
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_name -> {
                    PopupWindowUtils.get().showSelAccountPop(this) {
                        list[position].accountType = it.methodName
                        list[position].accountId = it.id
                        list[position].accountName = it.name
                        adapter.setNewData(list)
                    }
                }

                R.id.iv_delete -> {
                    list.removeAt(position)
                    adapter.setNewData(list)
                    countTotal()
                }
            }
        }
        adapter.setOnTextChangeListener {
            countTotal()
        }
    }

    private fun countTotal() {
        var priceTotal = BigDecimal(0.0)
        for (bean: AccountBean in list) {
            priceTotal += BigDecimal(if (TextUtils.isEmpty(bean.money)) "0" else bean.money)
        }
        val total = BigDecimal(intent.getStringExtra("moneyOwe")).subtract(priceTotal)
            .subtract(BigDecimal(if (TextUtils.isEmpty(et_reduction.text.toString())) "0" else et_reduction.text.toString()))
            .setScale(2, BigDecimal.ROUND_HALF_DOWN)
        tv_collection_total.text = "￥${priceTotal.setScale(2, BigDecimal.ROUND_HALF_DOWN)}"
        tv_residue.text = "￥$total"
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_add_item -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                list.add(AccountBean())
                adapter.setNewData(list)
            }

            R.id.tv_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                finish()
            }

            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (list.isEmpty()) {
                    ToastUtils.showShort("请填写收款信息后确认结算")
                    return
                }
                val reduction =
                    if (TextUtils.isEmpty(et_reduction.text.toString())) 0.0 else et_reduction.text.toString()
                        .toDouble()
                if (reduction > intent.getStringExtra("moneyOwe").toDouble()) {
                    ToastUtils.showShort("减免金额不能大于应收金额")
                    return
                }
                PopupWindowUtils.get().showConfirmPop(
                    this, "本单总金额${
                        intent.getStringExtra("moneyOwe")
                    }，实收${
                        tv_collection_total.text.toString().substring(1)
                    }减免${
                        tv_reduction.text.toString().substring(1)
                    }元，本单剩余应收${tv_residue.text.toString().substring(1)}"
                ) {
                    val params = HashMap<String, Any>()
                    val accountList = ArrayList<HashMap<String, Any>>()
                    for (acc: AccountBean in list) {
                        if (TextUtils.isEmpty(acc.accountName)) {
                            ToastUtils.showShort("请选择或删除为空的收款方式")
                            return@showConfirmPop
                        }
                        if (TextUtils.isEmpty(acc.money)) {
                            ToastUtils.showShort("请输入 ${acc.accountName} 的收款金额")
                            return@showConfirmPop
                        }
                        val map = HashMap<String, Any>()
                        map["accountId"] = acc.accountId!!
                        map["money"] = acc.money
                        accountList.add(map)
                    }
                    params["orderId"] = intent.getIntExtra("orderId", -1)
                    params["summary"] = et_remark.text.toString()
                    params["discount"] =
                        if (TextUtils.isEmpty(et_reduction.text.toString())) "0" else et_reduction.text.toString()
                    params["accountList"] = accountList
                    LoadingUtils.showLoading(this, "加载中...")
                    RetrofitUtils.get().postJson(
                        OrderSettleUrl, params, this,
                        object : RetrofitUtils.OnCallBackListener {
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
        }
    }
}