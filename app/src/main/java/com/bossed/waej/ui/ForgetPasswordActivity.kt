package com.bossed.waej.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.UpDateBVDCResponse
import com.bossed.waej.util.CountDownTimeUtil
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_forget_password.*

class ForgetPasswordActivity : BaseActivity(), View.OnClickListener {
    private var mCountDownTimeUtil: CountDownTimeUtil? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_forget_password
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_forget_password)
    }

    override fun initListener() {
        tb_forget_password.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        et_phone_forget.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                changeButtonState()
            }
        })
        et_verification.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                changeButtonState()
            }
        })
        et_new_pass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                changeButtonState()
            }
        })
        et_new_pass_again.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                changeButtonState()
            }
        })
    }

    private fun changeButtonState() {
        if (TextUtils.isEmpty(et_phone_forget.text.toString()) || TextUtils.isEmpty(et_verification.text.toString()) ||
            TextUtils.isEmpty(et_new_pass.text.toString()) || TextUtils.isEmpty(et_new_pass_again.text.toString())
        ) {
            buttonClickState(false, tv_sure_forget)
        } else {
            buttonClickState(true, tv_sure_forget)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_get_verification -> {
                if (TextUtils.isEmpty(et_phone_forget.text.toString()))
                    ToastUtils.showShort("请输入手机号")
                else
                    getVerification()
            }
            R.id.tv_sure_forget -> {
                if (et_new_pass.text.toString() != et_new_pass_again.text.toString())
                    ToastUtils.showShort("两次密码输入不一致")
                else
                    postNewPass()
            }
        }
    }

    private fun postNewPass() {
        val params = HashMap<String, String>()
        params["code"] = et_verification.text.toString()
        params["newPassword"] = et_new_pass_again.text.toString()
        params["phone"] = et_phone_forget.text.toString()
        RetrofitUtils.get().postJson(
            UrlConstants.ForgetPassUrl,
            params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    if (t.code == 200) {
                        SPUtils.getInstance().put("password", et_new_pass_again.text.toString())
                        upDateBVDCPwd()
                    } else
                        ToastUtils.showShort(t.msg)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    /**
     * http://bvdcapi.bsd128.com/bvdcPro.ashx?method=GetBVData&bvdcUserID=id&inKind=5201&inStr1=旧密码&inStr2=新密码
     */
    private fun upDateBVDCPwd() {
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "5201"
        params["inStr1"] = "重置"
        params["inStr2"] = et_new_pass_again.text.toString()
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, UpDateBVDCResponse::class.java)
                    if (t.code == "1") {
                        finish()
                        SPUtils.getInstance().put("password", "")
                    }
                    ToastUtils.showShort(t.message)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getVerification() {
        val params = HashMap<String, String>()
        params["phone"] = et_phone_forget.text.toString()
        params["codeType"] = "3"
        RetrofitUtils.get().getJson(
            UrlConstants.VerificationUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    mCountDownTimeUtil = CountDownTimeUtil(tv_get_verification)
                    mCountDownTimeUtil!!.runTimer()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mCountDownTimeUtil != null)
            mCountDownTimeUtil!!.cancel()
    }
}