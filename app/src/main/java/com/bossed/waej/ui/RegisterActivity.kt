package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.CompoundButton
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.base.BaseMVPActivity
import com.bossed.waej.contract.RegisterContract
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants.AgreementUrl
import com.bossed.waej.http.UrlConstants.PolicyUrl
import com.bossed.waej.javebean.AgentResponse
import com.bossed.waej.javebean.RegisterBVDCResponse
import com.bossed.waej.presenter.RegisterPresenter
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseMVPActivity<RegisterPresenter>(), OnNoRepeatClickListener,
    RegisterContract.RegisterView, CompoundButton.OnCheckedChangeListener {
    private var mCountDownTimeUtil: CountDownTimeUtil? = null
    private var agentID = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_register
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        setTitleBarMarginTop(tb_register)
        BarUtils.setStatusBarLightMode(window, false)
    }

    override fun initListener() {
//        cb_shop_register.setOnCheckedChangeListener(this)
//        cb_staff_register.setOnCheckedChangeListener(this)
        cb_agree.setOnCheckedChangeListener(this)
        tb_register.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        et_invitation.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (s.length == 6) {
                    LoadingUtils.showLoading(this@RegisterActivity, "加载中...")
                    Api.getInstance().getApiService().getAgentInfo(s)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : BaseResourceObserver<String>() {
                            override fun onComplete() {
                                LoadingUtils.dismissLoading()
                            }

                            override fun onSubscribe(d: Disposable) {
                            }

                            override fun onNext(s: String) {
                                LogUtils.d("tag", s)
                                val t =
                                    GsonUtils.fromJson(s, AgentResponse::class.java)
                                when (t.code) {
                                    200 -> {
                                        if (t.data != null) {
                                            tv_name.visibility = View.VISIBLE
                                            tv_phone.visibility = View.VISIBLE
                                            tv_name.text =
                                                "代理商：${t.data!!.agentName}"
                                            tv_phone.text =
                                                "电话：${t.data!!.phonenumber}"
                                            agentID = t.data!!.id
                                        } else {
                                            tv_name.visibility = View.VISIBLE
                                            tv_name.text = "邀请码输入错误!"
                                            tv_name.setTextColor(resources.getColor(R.color.red_color))
                                        }
                                    }

                                    401 -> PopupWindowUtils.get()
                                        .showLoginOutTimePop(this@RegisterActivity)

                                    else -> ToastUtils.showShort(t.msg)
                                }
                            }

                            override fun onError(throwable: Throwable) {
                                ToastUtils.showShort(throwable.message)
                                LoadingUtils.dismissLoading()
                            }
                        })
                } else {
                    tv_name.visibility = View.GONE
                    tv_phone.visibility = View.GONE
                    agentID = ""
                }
            }
        })
    }

    override fun onRepeatClick(v: View?) {
        when (v!!.id) {
            R.id.tv_agreement -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get()
                    .showHtmlPop(AgreementUrl, this, rl_content)
            }

            R.id.tv_policy -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get()
                    .showHtmlPop(PolicyUrl, this, rl_content)
            }

            R.id.tv_get_verification -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(et_phone_register.text.toString()) -> ToastUtils.showShort("请输入手机号")
                    et_phone_register.text.toString().length < 11 -> ToastUtils.showShort("请输入正确的手机号")
                    else -> mPresenter!!.getVerificationCode(
                        this,
                        et_phone_register.text.toString()
                    )
                }
            }

            R.id.tv_register -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (!TextUtils.isEmpty(et_invitation.text.toString()) && TextUtils.isEmpty(agentID)) {
                    ToastUtils.showShort("请输入正确的邀请码")
                    return
                }
                if (!cb_agree.isChecked) {
                    ToastUtils.showShort("请勾选并同意用户协议")
                    return
                }
                if (TextUtils.isEmpty(et_name_register.text.toString())) {
                    ToastUtils.showShort("请输入企业信息")
                    return
                }
                SPUtils.getInstance().put("authorization", "")
                val params = HashMap<String, Any>()
                params["code"] = et_verification.text.toString()
                params["company"] = et_name_register.text.toString()
                params["password"] = et_pass_register.text.toString()
                params["username"] = et_phone_register.text.toString()
                params["inviteCode"] = et_invitation.text.toString()
                params["nickName"] = et_nickName.text.toString()
                mPresenter!!.register(this, params)
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.cb_agree -> {
                buttonClickState(isChecked, tv_register)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mCountDownTimeUtil != null)
            mCountDownTimeUtil!!.cancel()
    }

    override fun onRegisterSuccess(data: String) {
        registerBVDC()
    }

    override fun onRegisterError(e: String) {
        ToastUtils.showShort(e)
    }

    override fun onGetVerificationSuccess(s: String) {
        mCountDownTimeUtil = CountDownTimeUtil(tv_get_verification)
        mCountDownTimeUtil!!.runTimer()
    }

    override fun showError(e: String) {
        ToastUtils.showShort(e)
    }

    override fun getPresenter(): RegisterPresenter {
        return RegisterPresenter()
    }

    /**
     * 注册bvdc账号
     * http://bvdcapi.bsd128.com/bvdcPro.ashx?method=CreateUser&cardFix=w&kehu_Pwd=123456&kehu_mc=哈哈哈&kehu_xm=18034212965&kehu_sj=18034212965&kehu_dz=18034212965&kehu_AreaCode=030001
     */
    private fun registerBVDC() {
        val params = HashMap<String, String>()
        params["method"] = "CreateUser"
        params["kehu_Pwd"] = et_pass_register.text.toString()
        params["cardFix"] = "w"
        params["kehu_mc"] = et_name_register.text.toString()
        params["kehu_xm"] = et_phone_register.text.toString()
        params["kehu_sj"] = et_phone_register.text.toString()
        params["kehu_dz"] = et_phone_register.text.toString()
        params["kehu_AreaCode"] = "030001"
        RetrofitUtils.get().getBVDCJson(
            "http://bvdcapi.bsd128.com/bvdcPro.ashx", params,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s, params)
                    val t = GsonUtils.fromJson(s, RegisterBVDCResponse::class.java)
                    if (t.code == "1") {
                        SPUtils.getInstance().put("bvdcUserID", t.outStr)
                        val intent =
                            Intent(this@RegisterActivity, RegisterSuccessActivity::class.java)
//                    intent.putExtra("type", "register")
                        startActivity(intent)
                        SPUtils.getInstance().put("remember", true)
                        SPUtils.getInstance().put("username", et_phone_register.text.toString())
                        SPUtils.getInstance().put("password", et_pass_register.text.toString())
                        finish()
                    } else
                        ToastUtils.showShort(t.message)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}