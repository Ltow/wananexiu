package com.bossed.waej.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.eventbus.EBUpdateUser
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.http.UrlConstants.UpdateShopMsgUrl
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.RetrofitUtils
import com.bossed.waej.util.TextChangedListener
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_set_name.*
import org.greenrobot.eventbus.EventBus

class SetNameActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_set_name
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        BarUtils.setStatusBarLightMode(window, true)
        setMarginTop(tb_set_name)
        tb_set_name.title = intent.getStringExtra("title")
        rl_phone.visibility =
            if (intent.getStringExtra("title") == "设置电话") View.VISIBLE else View.GONE
        rl_name.visibility =
            if (intent.getStringExtra("title") == "设置姓名") View.VISIBLE else View.GONE
        rl_code.visibility =
            if (intent.getStringExtra("title") == "邀请码") View.VISIBLE else View.GONE
        et_phone.setText(intent.getStringExtra("phone"))
        et_name.setText(intent.getStringExtra("name"))
    }

    override fun initListener() {
        et_name.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                iv_clear.visibility = if (TextUtils.isEmpty(s)) View.GONE else View.VISIBLE
            }
        })
        iv_clear.setOnClickListener {
            if (DoubleClicksUtils.get().isFastDoubleClick)
                return@setOnClickListener
            et_name.setText("")
        }
        et_phone.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                iv_clear2.visibility = if (TextUtils.isEmpty(s)) View.GONE else View.VISIBLE
            }
        })
        iv_clear2.setOnClickListener {
            if (DoubleClicksUtils.get().isFastDoubleClick)
                return@setOnClickListener
            et_phone.setText("")
        }
        et_code.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                iv_clear3.visibility = if (TextUtils.isEmpty(s)) View.GONE else View.VISIBLE
            }
        })
        iv_clear3.setOnClickListener {
            if (DoubleClicksUtils.get().isFastDoubleClick)
                return@setOnClickListener
            et_code.setText("")
        }
        tb_set_name.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                when (intent.getStringExtra("title")) {
                    "设置电话" -> {
                        if (TextUtils.isEmpty(et_phone.text.toString())) {
                            ToastUtils.showShort("电话不能为空")
                            return
                        }
                    }
                    "设置姓名" -> {
                        if (TextUtils.isEmpty(et_name.text.toString())) {
                            ToastUtils.showShort("姓名不能为空")
                            return
                        }
                    }
                    "邀请码" -> {
                        if (TextUtils.isEmpty(et_code.text.toString())) {
                            ToastUtils.showShort("邀请码不能为空")
                            return
                        }
                    }
                }
                LoadingUtils.showLoading(this@SetNameActivity, "加载中...")
                if (intent.getStringExtra("title") == "邀请码") {
                    val params = HashMap<String, Any>()
                    params["id"] = SPUtils.getInstance().getInt("shopId")
                    params["fullname"] = SPUtils.getInstance().getString("fullname")
                    params["inviteCode"] = et_code.text.toString()
                    RetrofitUtils.get()
                        .putJson(UpdateShopMsgUrl, params, this@SetNameActivity,
                            object : RetrofitUtils.OnCallBackListener {
                                override fun onSuccess(s: String) {
                                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                                    if (t.code == 200) {
                                        SPUtils.getInstance()
                                            .put("inviteCode", et_code.text.toString())
                                        finish()
                                        EventBus.getDefault().post(EBUpdateUser(true))
                                    }
                                    ToastUtils.showShort(t.msg)
                                }

                                override fun onFailed(e: String) {
                                    ToastUtils.showShort(e)
                                }
                            })
                } else {
                    val params = HashMap<String, Any>()
                    params["userId"] = SPUtils.getInstance().getInt("userId")
                    params["nickName"] = et_name.text.toString()
                    params["phonenumber"] = et_phone.text.toString()
                    RetrofitUtils.get().postJson(
                        UrlConstants.UpdateUserUrl, params, this@SetNameActivity,
                        object : RetrofitUtils.OnCallBackListener {
                            override fun onSuccess(s: String) {
                                val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                                if (t.code == 200) {
                                    finish()
                                    EventBus.getDefault().post(EBUpdateUser(true))
                                }
                                ToastUtils.showShort(t.msg)
                            }

                            override fun onFailed(e: String) {
                                ToastUtils.showShort(e)
                            }
                        })
                }
            }
        })
    }
}