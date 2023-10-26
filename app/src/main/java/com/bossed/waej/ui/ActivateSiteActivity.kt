package com.bossed.waej.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.SettlementResponse
import com.bossed.waej.javebean.Site
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_activate_site.*
import java.math.BigDecimal

class ActivateSiteActivity : BaseActivity(), OnNoRepeatClickListener {
    private var price = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_activate_site
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        setMarginTop(tb_activate_site)
        BarUtils.setStatusBarLightMode(window, true)
        if (TextUtils.isEmpty(intent.getStringExtra("termTime"))) {
            ll_top.visibility = View.GONE
        } else {
            tv_termTime.text = "统一到期日期：${intent.getStringExtra("termTime")}"
            val surplusMills =
                TimeUtils.string2Millis(intent.getStringExtra("termTime")) - TimeUtils.getNowMills()
            tv_surplus.text = (surplusMills / (1000 * 60 * 60 * 24)).toString()
        }
        tv_siteNum.text = "当前已开通站点：${intent.getStringExtra("siteNum")}"
        val t = GsonUtils.fromJson(intent.getStringExtra("site"), Site::class.java)
        tv_name.text = t.packageName
        tv_price_year.text = "${t.priceYear}/年"
        tv_remark.text = t.remark
        tv_count.text =
            if (TextUtils.isEmpty(intent.getStringExtra("termTime"))) "${t.priceYear}元1年" else "${t.priceDay}元x${tv_surplus.text}天"
        tv_day_price.text = "${t.priceDay}元/天"
        price =
            if (TextUtils.isEmpty(intent.getStringExtra("termTime"))) t.priceYear else BigDecimal(t.priceDay).multiply(
                BigDecimal(tv_surplus.text.toString())
            ).toString()
        tv_price.text = "￥${price}"
    }

    override fun initListener() {
        tb_activate_site.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        et_num.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (TextUtils.isEmpty(s))
                    return
                val total = BigDecimal(price).multiply(BigDecimal(s))
                tv_total.text = "￥$total"
            }
        })
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.ctv_alli -> {
                if (DoubleClicksUtils.get().isFastDoubleClick) {
                    return
                }
                ctv_alli.isChecked = !ctv_alli.isChecked
                ctv_wx.isChecked = !ctv_alli.isChecked
            }
            R.id.ctv_wx -> {
                if (DoubleClicksUtils.get().isFastDoubleClick) {
                    return
                }
                ctv_wx.isChecked = !ctv_wx.isChecked
                ctv_alli.isChecked = !ctv_wx.isChecked
            }
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick) {
                    return
                }
                if (TextUtils.isEmpty(et_num.text.toString())) {
                    ToastUtils.showShort("请输入要开通的数量")
                    return
                }
                LoadingUtils.showLoading(this, "加载中...")
                val params = HashMap<String, Any>()
                if (ctv_alli.isChecked)
                    params["payMethod"] = 2
                if (ctv_wx.isChecked)
                    params["payMethod"] = 1
                val map = HashMap<String, Any>()
                val prdocutList = ArrayList<HashMap<String, Any>>()
                map["orderType"] = 2
                map["productId"] = intent.getStringExtra("siteId")
                map["num"] = et_num.text.toString()
                prdocutList.add(map)
                params["prdocutList"] = prdocutList
                RetrofitUtils.get()
                    .postJson(
                        UrlConstants.BuyUrl, params, this,
                        object : RetrofitUtils.OnCallBackListener {
                            override fun onSuccess(s: String) {
                                val t = GsonUtils.fromJson(s, SettlementResponse::class.java)
                            }

                            override fun onFailed(e: String) {
                                ToastUtils.showShort(e)
                            }
                        })
            }
        }
    }
}