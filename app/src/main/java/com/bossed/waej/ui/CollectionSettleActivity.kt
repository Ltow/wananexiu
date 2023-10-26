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
import com.bossed.waej.eventbus.CollectionFinishBean
import com.bossed.waej.http.UrlConstants.ReceivableUrl
import com.bossed.waej.javebean.AccountBean
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_collection_settle.*
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal

class CollectionSettleActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: AddAccountAdapter
    private val list = ArrayList<AccountBean>()
    private var money: Double = 0.0

    override fun getLayoutId(): Int {
        return R.layout.activity_collection_settle
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_collection_settle)
        rv_collection_settle.layoutManager = LinearLayoutManager(this)
        list.add(AccountBean())
        adapter = AddAccountAdapter(list, 1)
        adapter.bindToRecyclerView(rv_collection_settle)
        val footView = LayoutInflater.from(this).inflate(R.layout.layout_footer_view_item, null)
        footView.findViewById<TextView>(R.id.tv_add_item).setOnClickListener(this)
        footView.findViewById<TextView>(R.id.tv_add_item).text = "收款方式"
        adapter.setFooterView(footView)
        tv_date_sel.text = DateFormatUtils.get().formatDate(TimeUtils.getNowDate())
        money = intent.getDoubleExtra("money", 0.0)
        tv_money_total.text = "￥${money}"
        tv_moneyOwe.text = intent.getStringExtra("moneyOwe")
        et_remark.setText("${intent.getStringExtra("customerName")}${intent.getStringExtra("cardNo")}收款")
    }

    override fun initListener() {
        tb_collection_settle.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.tv_add_item -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                list.add(AccountBean())
                adapter.setNewData(list)
            }

            R.id.tv_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }

            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (TextUtils.isEmpty(tv_date_sel.text.toString())) {
                    ToastUtils.showShort("请选择收款日期")
                    return
                }
                val discount: Double =
                    if (TextUtils.isEmpty(et_discount.text.toString())) 0.0 else et_discount.text.toString()
                        .toDouble()
                if (discount > money) {
                    ToastUtils.showShort("减免金额不能大于应收金额")
                    return
                }
                val accountList = ArrayList<HashMap<String, Any>>()
                var total = BigDecimal(0.0)
                for (acc: AccountBean in list) {
                    if (TextUtils.isEmpty(acc.accountName)) {
                        ToastUtils.showShort("请选择或删除收款账号为空的收款方式")
                        return
                    }
                    if (TextUtils.isEmpty(acc.money)) {
                        ToastUtils.showShort("请输入 ${acc.accountName} 的收款金额")
                        return
                    }
                    val map = HashMap<String, Any>()
                    map["accountId"] = acc.accountId!!
                    map["money"] = acc.money
                    total = total.add(BigDecimal(acc.money)).setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    accountList.add(map)
                }
                PopupWindowUtils.get().showConfirmPop(
                    this,
                    "本次应收${money}元，实收${total}元，减免${discount}元，剩余应收${
                        BigDecimal(money).subtract(BigDecimal(discount)).subtract(total)
                            .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    }元。"
                ) {
                    collection(accountList)
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

    private fun collection(accountList: ArrayList<HashMap<String, Any>>) {
        val params = HashMap<String, Any>()
        params["customerId"] = intent.getIntExtra("customerId", -1)
        params["money"] = money
        params["billTime"] = tv_date_sel.text.toString()
        params["accountList"] = accountList
        params["orderIds"] = intent.getIntegerArrayListExtra("sel")
        params["summary"] = et_remark.text.toString()
        params["discount"] =
            if (TextUtils.isEmpty(et_discount.text.toString())) "0" else et_discount.text.toString()
        params["type"] = "1"
        LoadingUtils.showLoading(this, "加载中...")
        RetrofitUtils.get()
            .postJson(ReceivableUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    if (t.code == 200) {
                        finish()
                        EventBus.getDefault().post(CollectionFinishBean(true))
                    }
                    ToastUtils.showShort(t.msg)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}