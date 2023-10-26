package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.adapter.AccountAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.AccountResponse
import com.bossed.waej.javebean.User
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_account.*
import java.math.BigDecimal

class AccountActivity : BaseActivity(), View.OnClickListener {
    private lateinit var accountAdapter: AccountAdapter
    private val accountList = ArrayList<User>()

    override fun getLayoutId(): Int {
        return R.layout.activity_account
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        BarUtils.setStatusBarLightMode(window, true)
        setMarginTop(tb_account)
        rv_account.layoutManager = LinearLayoutManager(this)
        accountAdapter = AccountAdapter(accountList)
        accountAdapter.bindToRecyclerView(rv_account)
    }

    override fun initListener() {
        tb_account.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.tv_activate -> {
                if (DoubleClicksUtils.get().isFastDoubleClick) {
                    return
                }
                val intent = Intent(this, ActivateSiteActivity::class.java)
                intent.putExtra("termTime", termTime)
                intent.putExtra("siteNum", siteNum)
                intent.putExtra("site", site)
                intent.putExtra("siteId", siteId)
                startActivity(intent)
            }
            R.id.iv_alert -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showBuySiteAlert(this, iv_alert)
            }
        }
    }

    private var termTime = ""
    private var siteNum = ""
    private var site = ""
    private var siteId = ""
    private fun getAccountList() {
        RetrofitUtils.get().getJson(
            UrlConstants.AccountListUrl, HashMap(), this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, AccountResponse::class.java)
                    accountList.clear()
                    accountList.addAll(t.data.userList)
                    accountAdapter.setNewData(accountList)
                    tv_userNum.text = "${t.data.userNum}个"
                    tv_siteNum.text = "${t.data.siteNum}个"
                    tv_online.text = "${t.data.userOnlineNum}个"
                    if (!TextUtils.isEmpty(t.data.termTime)) {
                        tv_termTime.text = "站点统一到期时间：${t.data.termTime}"
                        termTime = t.data.termTime!!
                    } else
                        tv_termTime.visibility = View.GONE
                    siteId = t.data.site.id.toString()
                    siteNum = t.data.siteNum.toString()
                    site = GsonUtils.toJson(t.data.site)
                    tv_num.text =
                        "${BigDecimal(t.data.userNum).subtract(BigDecimal(t.data.siteNum))}个账号"
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        getAccountList()
    }
}