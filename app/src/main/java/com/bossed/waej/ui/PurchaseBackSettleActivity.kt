package com.bossed.waej.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.AddAccountAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.UrlConstants.PurchaseSettleUrl
import com.bossed.waej.javebean.AccountBean
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_purchase_back_settle.*

class PurchaseBackSettleActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: AddAccountAdapter
    private val list = ArrayList<AccountBean>()
    private lateinit var footView: View

    override fun getLayoutId(): Int {
        return R.layout.activity_purchase_back_settle
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_purchase_back_settle)
        rv_back_settle.layoutManager = LinearLayoutManager(this)
        list.add(AccountBean())
        adapter = AddAccountAdapter(list, 1)
        adapter.bindToRecyclerView(rv_back_settle)
        footView = LayoutInflater.from(this).inflate(R.layout.layout_footer_view_item, null)
        footView.findViewById<TextView>(R.id.tv_add_item).setOnClickListener(this)
        footView.findViewById<TextView>(R.id.tv_add_item).text = "收款方式"
        adapter.addFooterView(footView)
        tv_money.text = intent.getStringExtra("amount")
    }

    override fun initListener() {
        tb_purchase_back_settle.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_name -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get().showSelAccountPop(this) {
                        list[position].accountType = it.methodName
                        list[position].accountId = it.id
                        list[position].accountName = it.name
                        adapter.setNewData(list)
                    }
                }
                R.id.iv_delete -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    list.removeAt(position)
                    adapter.setNewData(list)
                }
            }
        }
        adapter.setOnTextChangeListener { }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    ctv_sd.isChecked -> {
                        if (list.isEmpty()) {
                            ToastUtils.showShort("请添加收款帐号")
                            return
                        }
                        payment()
                    }
                    ctv_no_sd.isChecked -> {
                        finish()
                    }
                }
            }
            R.id.tv_add_item -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                list.add(AccountBean())
                adapter.setNewData(list)
            }
            R.id.ctv_sd -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                list.add(AccountBean())
                adapter.setNewData(list)
                adapter.addFooterView(footView)
                ctv_sd.isChecked = !ctv_sd.isChecked
                ctv_no_sd.isChecked = !ctv_sd.isChecked
            }
            R.id.ctv_no_sd -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                list.clear()
                adapter.setNewData(list)
                adapter.removeFooterView(footView)
                ctv_no_sd.isChecked = !ctv_no_sd.isChecked
                ctv_sd.isChecked = !ctv_no_sd.isChecked
            }
        }
    }

    private fun payment() {
        val params = HashMap<String, Any>()
        params["billTime"] = TimeUtils.getNowString()
        val accountList = ArrayList<HashMap<String, Any>>()
        for (acc: AccountBean in list) {
            if (TextUtils.isEmpty(acc.accountName)) {
                ToastUtils.showShort("请添加或删除收款账号为空的收款方式")
                return
            }
            if (TextUtils.isEmpty(acc.money)) {
                ToastUtils.showShort("请输入 ${acc.accountName} 的收款金额")
                return
            }
            val map = HashMap<String, Any>()
            map["accountId"] = acc.accountId!!
            map["money"] = acc.money
            accountList.add(map)
        }
        params["accountList"] = accountList
        val discount: Double =
            if (TextUtils.isEmpty(et_discount.text.toString())) 0.0 else et_discount.text.toString()
                .toDouble()
        if (discount > intent.getStringExtra("amount").toDouble()) {
            ToastUtils.showShort("减免金额不能大于应收金额")
            return
        }
        params["orderId"] = intent.getStringExtra("orderId")
        params["isPay"] = "true"// 0-否 1-是
        params["discount"] = discount
        LoadingUtils.showLoading(this, "加载中...")
        RetrofitUtils.get()
            .postJson(PurchaseSettleUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    if (t.code == 200) {
                        finish()
                    }
                    ToastUtils.showShort(t.msg)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}