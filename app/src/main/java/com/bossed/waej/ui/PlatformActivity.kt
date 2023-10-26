package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.RequestStatusUrl
import com.bossed.waej.http.UrlConstants.RequestUrl
import com.bossed.waej.javebean.PlatformResponse
import com.bossed.waej.javebean.RequestStatusResponse
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_platform.*

class PlatformActivity : BaseActivity(), OnClickListener {

    override fun getLayoutId(): Int {
        return R.layout.activity_platform
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_platform)
        when (intent.getIntExtra("waejStatus", 0)) {
            1 -> {
                ll_success.visibility = View.VISIBLE
                ll_content.visibility = View.GONE
                tv_alert.text = "欢迎您，加入《万鞍E家车服地图》平台"
                btn_commit.text = "进入"
            }

            3 -> {
                ll_success.visibility = View.VISIBLE
                ll_content.visibility = View.GONE
                btn_commit.text = "返回"
                getStatus()
            }
        }
    }

    override fun initListener() {
        tb_platform.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.ctv_agree -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_agree.isChecked = !ctv_agree.isChecked
            }

            R.id.btn_commit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (btn_commit.text == "进入")
                    startActivity(Intent(this, PublicGoodsActivity::class.java))
                else if (btn_commit.text == "返回")
                    onBackPressed()
                else
                    if (ctv_agree.isChecked) {
                        LoadingUtils.showLoading(this, "加载中...")
                        val params = HashMap<String, Any>()
                        RetrofitUtils.get().postJson(RequestUrl, params, this,
                            object : RetrofitUtils.OnCallBackListener {
                                override fun onSuccess(s: String) {
                                    LogUtils.d("tag", s)
                                    val t = GsonUtils.fromJson(s, PlatformResponse::class.java)
                                    ToastUtils.showShort(t.msg)
                                    if (t.code == 200) {
                                        ll_success.visibility = View.VISIBLE
                                        ll_content.visibility = View.GONE
                                        btn_commit.text = "进入"
                                    }
                                }

                                override fun onFailed(e: String) {
                                    ToastUtils.showShort(e)
                                }
                            })
                    } else
                        ToastUtils.showShort("请先阅读并同意第三方分账协议.")
            }
        }
    }

    private fun getStatus() {
        LoadingUtils.showLoading(this, "加载中...")
        RetrofitUtils.get()
            .getJson(RequestStatusUrl, HashMap(), this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        LogUtils.d("tag", s)
                        val t = GsonUtils.fromJson(s, RequestStatusResponse::class.java)
                        when (t.data!!.auditStatus) {
                            1 -> {
                                ll_success.visibility = View.VISIBLE
                                ll_content.visibility = View.GONE
                                tv_alert.text = "欢迎您，加入《万鞍E家车服地图》平台"
                                btn_commit.text = "进入"
                            }

                            3 -> {
                                ll_success.visibility = View.VISIBLE
                                ll_content.visibility = View.GONE
                                btn_commit.text = "返回"
                            }
                        }
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }
}