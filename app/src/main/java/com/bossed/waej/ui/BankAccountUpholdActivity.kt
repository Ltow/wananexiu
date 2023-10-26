package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.adapter.BankAccountAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.customview.BottomDialog
import com.bossed.waej.http.UrlConstants.AccountUrl
import com.bossed.waej.http.UrlConstants.SettleAccountListUrl
import com.bossed.waej.javebean.BankAccountResponse
import com.bossed.waej.javebean.BankAccountRow
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_bank_account_uphold.*
import java.math.BigDecimal

class BankAccountUpholdActivity : BaseActivity(), View.OnClickListener {
    private lateinit var wechatAdapter: BankAccountAdapter
    private val wechatList = ArrayList<BankAccountRow>()
    private lateinit var aliAdapter: BankAccountAdapter
    private val aliList = ArrayList<BankAccountRow>()
    private lateinit var bankAdapter: BankAccountAdapter
    private val bankList = ArrayList<BankAccountRow>()
    private lateinit var cashAdapter: BankAccountAdapter
    private val cashList = ArrayList<BankAccountRow>()
    private lateinit var spareAdapter: BankAccountAdapter
    private val spareList = ArrayList<BankAccountRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_bank_account_uphold
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_bank_account)
        if (intent.getStringExtra("type") == "1")
            ll_bottom.visibility = View.VISIBLE
        rv_wechat.layoutManager = LinearLayoutManager(this)
        wechatAdapter = BankAccountAdapter(wechatList)
        wechatAdapter.bindToRecyclerView(rv_wechat)
        rv_ali.layoutManager = LinearLayoutManager(this)
        aliAdapter = BankAccountAdapter(aliList)
        aliAdapter.bindToRecyclerView(rv_ali)
        rv_bank_card.layoutManager = LinearLayoutManager(this)
        bankAdapter = BankAccountAdapter(bankList)
        bankAdapter.bindToRecyclerView(rv_bank_card)
        rv_cash.layoutManager = LinearLayoutManager(this)
        cashAdapter = BankAccountAdapter(cashList)
        cashAdapter.bindToRecyclerView(rv_cash)
        rv_spare.layoutManager = LinearLayoutManager(this)
        spareAdapter = BankAccountAdapter(spareList)
        spareAdapter.bindToRecyclerView(rv_spare)
        getAccount()
    }

    override fun initListener() {
        tb_bank_account.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        wechatAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_edit_item -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    showAddAccountPop(1, true, wechatList[position])
                }
                R.id.sb_person -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get().showConfirmAndCancelPop(this,
                        "确定${if (wechatList[position].status == 0) "启用" else "禁用"}此账号？",
                        object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                update(
                                    wechatList[position].methodId,
                                    wechatList[position].methodName,
                                    wechatList[position].id,
                                    wechatList[position].name,
                                    wechatList[position].bank,
                                    wechatList[position].account,
                                    if (wechatList[position].status == 0) 1 else 0
                                )
                            }

                            override fun onCancel() {
                                getAccount()
                            }
                        }
                    )
                }
            }
        }
        aliAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_edit_item -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    showAddAccountPop(2, true, aliList[position])
                }
                R.id.sb_person -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get().showConfirmAndCancelPop(this,
                        "确定${if (aliList[position].status == 0) "启用" else "禁用"}此账号？",
                        object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                update(
                                    aliList[position].methodId,
                                    aliList[position].methodName,
                                    aliList[position].id,
                                    aliList[position].name,
                                    aliList[position].bank,
                                    aliList[position].account,
                                    if (aliList[position].status == 0) 1 else 0
                                )
                            }

                            override fun onCancel() {
                                getAccount()
                            }
                        }
                    )
                }
            }
        }
        bankAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_edit_item -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    showAddAccountPop(3, true, bankList[position])
                }
                R.id.sb_person -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get().showConfirmAndCancelPop(this,
                        "确定${if (bankList[position].status == 0) "启用" else "禁用"}此账号？",
                        object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                update(
                                    bankList[position].methodId,
                                    bankList[position].methodName,
                                    bankList[position].id,
                                    bankList[position].name,
                                    bankList[position].bank,
                                    bankList[position].account,
                                    if (bankList[position].status == 0) 1 else 0
                                )
                            }

                            override fun onCancel() {
                                getAccount()
                            }
                        }
                    )
                }
            }
        }
        cashAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_edit_item -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    showAddAccountPop(4, true, cashList[position])
                }
                R.id.sb_person -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get().showConfirmAndCancelPop(this,
                        "确定${if (cashList[position].status == 0) "启用" else "禁用"}此账号？",
                        object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                update(
                                    cashList[position].methodId,
                                    cashList[position].methodName,
                                    cashList[position].id,
                                    cashList[position].name,
                                    cashList[position].bank,
                                    cashList[position].account,
                                    if (cashList[position].status == 0) 1 else 0
                                )
                            }

                            override fun onCancel() {
                                getAccount()
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_wechat -> {
                rv_wechat.visibility = if (rv_wechat.isVisible) View.GONE else View.VISIBLE
                iv_wechat.setImageResource(if (rv_wechat.isVisible) R.mipmap.icon_close_down else R.mipmap.icon_open_up)
            }
            R.id.ll_ali -> {
                rv_ali.visibility = if (rv_ali.isVisible) View.GONE else View.VISIBLE
                iv_ali.setImageResource(if (rv_ali.isVisible) R.mipmap.icon_close_down else R.mipmap.icon_open_up)
            }
            R.id.ll_bank -> {
                rv_bank_card.visibility = if (rv_bank_card.isVisible) View.GONE else View.VISIBLE
                iv_bank.setImageResource(if (rv_bank_card.isVisible) R.mipmap.icon_close_down else R.mipmap.icon_open_up)
            }
            R.id.ll_cash -> {
                rv_cash.visibility = if (rv_cash.isVisible) View.GONE else View.VISIBLE
                iv_cash.setImageResource(if (rv_cash.isVisible) R.mipmap.icon_close_down else R.mipmap.icon_open_up)
            }
            R.id.tv_add_wechat -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                showAddAccountPop(1, false, null)
            }
            R.id.tv_add_ali -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                showAddAccountPop(2, false, null)
            }
            R.id.tv_add_bank -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                showAddAccountPop(3, false, null)
            }
            R.id.tv_add_cash -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                showAddAccountPop(4, false, null)
            }
            R.id.tv_add_spare -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
//                showAddAccountPop(5)
            }
            R.id.tv_skip -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this, RegisterSuccessActivity::class.java))
                finish()
            }
            R.id.tv_next -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, RegisterSuccessActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showAddAccountPop(type: Int, isEdit: Boolean, row: BankAccountRow?) {
        BottomDialog(this).create(R.layout.layout_pop_add_account)
            .setCanceledOnTouchOutside(true).setViewInterface { view, dialog ->
                val title = view.findViewById<TextView>(R.id.tv_title)
                val tv_type = view.findViewById<TextView>(R.id.tv_type)
                val rl_khh = view.findViewById<View>(R.id.rl_khh)
                val rl_yhk = view.findViewById<View>(R.id.rl_yhk)
                val rl_balance = view.findViewById<View>(R.id.rl_balance)
                val tv_account = view.findViewById<TextView>(R.id.tv_account)
                val tv_confirm = view.findViewById<TextView>(R.id.tv_confirm)
                val et_name = view.findViewById<EditText>(R.id.et_name)
                val et_bank = view.findViewById<EditText>(R.id.et_bank)
                val et_account = view.findViewById<EditText>(R.id.et_account)
                val et_balance = view.findViewById<EditText>(R.id.et_balance)
                if (isEdit){
                    et_name.setText(row!!.name)
                    et_bank.setText(row.bank)
                    et_account.setText(row.account)
                    et_balance.setText(row.balance)
                }
                when (type) {
                    1 -> {
                        title.text = "微信添加账号"
                        tv_type.text = "微信"
                        tv_account.text = "账号"
                        et_account.hint = "请输入账号"
                        rl_khh.visibility = View.GONE
                        if (isEdit) {
                            title.text = "微信账号编辑"
                            rl_balance.visibility = View.GONE
                            tv_confirm.text = "确定"
                        }
                    }
                    2 -> {
                        title.text = "支付宝添加账号"
                        tv_type.text = "支付宝"
                        tv_account.text = "账号"
                        et_account.hint = "请输入账号"
                        rl_khh.visibility = View.GONE
                        if (isEdit) {
                            title.text = "支付宝账号编辑"
                            rl_balance.visibility = View.GONE
                            tv_confirm.text = "确定"
                        }
                    }
                    3 -> {
                        title.text = "银行卡添加账号"
                        tv_type.text = "银行卡"
                        if (isEdit) {
                            title.text = "银行卡账号编辑"
                            rl_balance.visibility = View.GONE
                            tv_confirm.text = "确定"
                        }
                    }
                    4 -> {
                        title.text = "现金添加账号"
                        tv_type.text = "现金"
                        rl_khh.visibility = View.GONE
                        rl_yhk.visibility = View.GONE
                        if (isEdit) {
                            title.text = "现金账号编辑"
                            rl_balance.visibility = View.GONE
                            tv_confirm.text = "确定"
                        }
                    }
                    5 -> {
                        title.text = "备付金添加账号"
                        tv_type.text = "备付金"
                    }
                }
                val ll_bottom = view.findViewById<LinearLayout>(R.id.ll_bottom)
                val layoutParams = ll_bottom.layoutParams as RelativeLayout.LayoutParams
                SoftKeyBoardUtils.setOnKeyBoardChangeListener(this,
                    object : SoftKeyBoardUtils.OnSoftKeyBoardChangeListener {
                        override fun keyBoardShow(height: Int) {
                            layoutParams.bottomMargin = height
                            ll_bottom.layoutParams = layoutParams
                        }

                        override fun keyBoardHide(height: Int) {
                            layoutParams.bottomMargin =
                                if (BarUtils.isNavBarVisible(this@BankAccountUpholdActivity))
                                    BarUtils.getNavBarHeight()
                                else
                                    0
                            ll_bottom.layoutParams = layoutParams
                        }
                    })
                tv_confirm.setOnClickListener {
                    when (type) {
                        1 -> when {
                            TextUtils.isEmpty(et_name.text.toString()) -> {
                                ToastUtils.showShort("名称不能为空")
                            }
                            TextUtils.isEmpty(et_account.text.toString()) -> {
                                ToastUtils.showShort("账号不能为空")
                            }
                            TextUtils.isEmpty(et_balance.text.toString()) && !isEdit -> {
                                ToastUtils.showShort("余额不能为空")
                            }
                            else -> {
                                if (isEdit)
                                    update(
                                        row!!.methodId,
                                        row.methodName,
                                        row.id,
                                        et_name.text.toString(),
                                        row.bank,
                                        et_account.text.toString(),
                                        row.status
                                    )
                                else
                                    newAccount(
                                        type, "微信",
                                        et_name.text.toString(),
                                        et_bank.text.toString(),
                                        et_account.text.toString(),
                                        et_balance.text.toString()
                                    )
                                dialog.dismiss()
                            }
                        }
                        2 -> when {
                            TextUtils.isEmpty(et_name.text.toString()) -> {
                                ToastUtils.showShort("名称不能为空")
                            }
                            TextUtils.isEmpty(et_account.text.toString()) -> {
                                ToastUtils.showShort("账号不能为空")
                            }
                            TextUtils.isEmpty(et_balance.text.toString()) && !isEdit -> {
                                ToastUtils.showShort("余额不能为空")
                            }
                            else -> {
                                if (isEdit)
                                    update(
                                        row!!.methodId,
                                        row.methodName,
                                        row.id,
                                        et_name.text.toString(),
                                        row.bank,
                                        et_account.text.toString(),
                                        row.status
                                    )
                                else
                                    newAccount(
                                        type, "支付宝",
                                        et_name.text.toString(),
                                        et_bank.text.toString(),
                                        et_account.text.toString(),
                                        et_balance.text.toString()
                                    )
                                dialog.dismiss()
                            }
                        }
                        3 -> when {
                            TextUtils.isEmpty(et_name.text.toString()) -> {
                                ToastUtils.showShort("名称不能为空")
                            }
                            TextUtils.isEmpty(et_bank.text.toString()) -> {
                                ToastUtils.showShort("开户行不能为空")
                            }
                            TextUtils.isEmpty(et_account.text.toString()) -> {
                                ToastUtils.showShort("账号不能为空")
                            }
                            TextUtils.isEmpty(et_balance.text.toString()) && !isEdit -> {
                                ToastUtils.showShort("余额不能为空")
                            }
                            else -> {
                                if (isEdit)
                                    update(
                                        row!!.methodId,
                                        row.methodName,
                                        row.id,
                                        et_name.text.toString(),
                                        et_bank.text.toString(),
                                        et_account.text.toString(),
                                        row.status
                                    )
                                else
                                    newAccount(
                                        type, "银行卡",
                                        et_name.text.toString(),
                                        et_bank.text.toString(),
                                        et_account.text.toString(),
                                        et_balance.text.toString()
                                    )
                                dialog.dismiss()
                            }
                        }
                        4 -> when {
                            TextUtils.isEmpty(et_name.text.toString()) -> {
                                ToastUtils.showShort("名称不能为空")
                            }
                            TextUtils.isEmpty(et_balance.text.toString()) && !isEdit -> {
                                ToastUtils.showShort("余额不能为空")
                            }
                            else -> {
                                if (isEdit)
                                    update(
                                        row!!.methodId,
                                        row.methodName,
                                        row.id,
                                        et_name.text.toString(),
                                        row.bank,
                                        row.account,
                                        row.status
                                    )
                                else
                                    newAccount(
                                        type, "现金",
                                        et_name.text.toString(),
                                        et_bank.text.toString(),
                                        et_account.text.toString(),
                                        et_balance.text.toString()
                                    )
                                dialog.dismiss()
                            }
                        }
                        5 -> "备付金"
                        else -> ""
                    }
                }
                view.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                    dialog.dismiss()
                }
            }.show()
    }

    private fun newAccount(
        methodId: Int,
        methodName: String,
        name: String,
        bank: String,
        account: String,
        balance: String
    ) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["methodId"] = methodId
        params["methodName"] = methodName
        params["name"] = name
        params["bank"] = bank
        params["account"] = account
        params["balance"] = balance
        RetrofitUtils.get()
            .postJson(AccountUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    ToastUtils.showShort(t.msg)
                    getAccount()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun update(
        methodId: Int,
        methodName: String,
        id: Int,
        name: String,
        bank: String,
        account: String,
        status: Int
    ) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["methodId"] = methodId
        params["methodName"] = methodName
        params["id"] = id
        params["name"] = name
        params["bank"] = bank
        params["account"] = account
        params["status"] = status
        RetrofitUtils.get()
            .putJson(AccountUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    ToastUtils.showShort(t.msg)
                    getAccount()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getAccount() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        RetrofitUtils.get()
            .getJson(SettleAccountListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d(s)
                    val t = GsonUtils.fromJson(s, BankAccountResponse::class.java)
                    tv_total.text = t.total.toString()
                    wechatList.clear()
                    aliList.clear()
                    bankList.clear()
                    cashList.clear()
                    spareList.clear()
                    var total = BigDecimal(0.0)
                    for (row: BankAccountRow in t.rows as ArrayList) {
                        when (row.methodId) {
                            1 -> wechatList.add(row)
                            2 -> aliList.add(row)
                            3 -> bankList.add(row)
                            4 -> cashList.add(row)
                            5 -> spareList.add(row)
                        }
                        total += BigDecimal(row.balance)
                    }
                    tv_total_money.text = total.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString()
                    wechatAdapter.setNewData(wechatList)
                    aliAdapter.setNewData(aliList)
                    bankAdapter.setNewData(bankList)
                    cashAdapter.setNewData(cashList)
                    spareAdapter.setNewData(spareList)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}