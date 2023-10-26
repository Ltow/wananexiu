package com.bossed.waej.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants.BankCardUrl
import com.bossed.waej.http.UrlConstants.GetBankCardInfoUrl
import com.bossed.waej.javebean.BankCardInfoResponse
import com.bossed.waej.javebean.BankCardResponse
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_bank_card.*

class AddBankCardActivity : BaseActivity(), OnClickListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_add_bank_card
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_add_bank)
        tv_merchantNo.text = intent.getStringExtra("merchantNo")
        if (!TextUtils.isEmpty(intent.getStringExtra("id")))
            getInfo()
    }

    override fun initListener() {
        tb_add_bank.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.ctv_personal -> {//对私
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_personal.isChecked = true
                ctv_enterprise.isChecked = false
            }

            R.id.ctv_enterprise -> {//对公
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_personal.isChecked = false
                ctv_enterprise.isChecked = true
            }

            R.id.ctv_yes -> {//是
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_yes.isChecked = true
                ctv_no.isChecked = false
            }

            R.id.ctv_no -> {//否
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_yes.isChecked = false
                ctv_no.isChecked = true
            }

            R.id.iv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (!TextUtils.isEmpty(et_acctNo.text.toString()))
                    getBankCardInfo()
            }

            R.id.btn_commit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (isComplete())
                    if (TextUtils.isEmpty(intent.getStringExtra("id")))
                        add()
                    else
                        delete()
                else
                    ToastUtils.showShort("必填项不能为空")
            }
        }
    }

    /**
     * 新增银行卡
     */
    private fun add() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["merchantNo"] = intent.getStringExtra("merchantNo")
        params["cardNo"] = et_acctNo.text.toString()
        params["cardName"] = et_acctName.text.toString()
        params["bankCode"] = et_openningBankCode.text.toString()
        params["bankName"] = et_openningBankName.text.toString()
        params["clearingBankCode"] = et_clearingBankCode.text.toString()
        params["defaultFlag"] = if (ctv_yes.isChecked) 1 else 0
        params["cardType"] = if (ctv_personal.isChecked) 58 else 57
        params["remark"] = et_remark.text.toString()
        RetrofitUtils.get()
            .postJson(BankCardUrl, params, this, object : RetrofitUtils.OnCallBackListener {
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

//    /**
//     * 修改银行卡
//     */
//    private fun update() {
//        LoadingUtils.showLoading(this, "加载中...")
//        val params = HashMap<String, Any>()
//        params["merchantNo"] = intent.getStringExtra("merchantNo")
//        params["id"] = intent.getStringExtra("id")
//        params["cardNo"] = et_acctNo.text.toString()
//        params["cardName"] = et_acctName.text.toString()
//        params["bankCode"] = et_openningBankCode.text.toString()
//        params["bankName"] = et_openningBankName.text.toString()
//        params["clearingBankCode"] = et_clearingBankCode.text.toString()
//        params["defaultFlag"] = if (ctv_yes.isChecked) 1 else 0
//        params["cardType"] = if (ctv_personal.isChecked) 58 else 57
//        params["remark"] = et_remark.text.toString()
//        RetrofitUtils.get()
//            .putJson(BankCardUrl, params, this, object : RetrofitUtils.OnCallBackListener {
//                override fun onSuccess(s: String) {
//                    LogUtils.d("tag", s)
//                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
//                    if (t.code == 200)
//                        finish()
//                    ToastUtils.showShort(t.msg)
//                }
//
//                override fun onFailed(e: String) {
//                    ToastUtils.showShort(e)
//                }
//            })
//    }

    private fun delete() {
        PopupWindowUtils.get().showConfirmPop(this, "是否确认解除此银行卡？") {
            LoadingUtils.showLoading(this, "加载中...")
            Api.getInstance().getApiService()
                .deleteBankCard(intent.getStringExtra("id").toInt())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseResourceObserver<BaseResponse>() {
                    override fun onComplete() {
                        LoadingUtils.dismissLoading()
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: BaseResponse) {
                        ToastUtils.showShort(t.msg)
                        when (t.code) {
                            200 -> {
                                ToastUtils.showShort(t.msg)
                                finish()
                            }

                            401 -> {
                                PopupWindowUtils.get()
                                    .showLoginOutTimePop(this@AddBankCardActivity)
                            }

                            else -> {
                                ToastUtils.showShort(t.msg)
                            }
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        ToastUtils.showShort(throwable.message)
                        LoadingUtils.dismissLoading()
                    }
                })
        }
    }

    /**
     * 卡详情
     */
    private fun getInfo() {
        Api.getInstance().getApiService()
            .getBankCardInfo(intent.getStringExtra("id").toInt())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BankCardResponse::class.java)
                    when (t.code) {
                        200 -> {
                            when (t.data!!.cardType) {
                                "58" -> ctv_personal.isChecked
                                "57" -> ctv_enterprise.isChecked
                            }
                            et_acctNo.setText(t.data!!.cardNo)
                            et_acctName.setText(t.data!!.cardName)
                            et_openningBankCode.setText(t.data!!.bankCode)
                            et_openningBankName.setText(t.data!!.bankName)
                            et_clearingBankCode.setText(t.data!!.clearingBankCode)
                            et_remark.setText(t.data!!.remark)
                            when (t.data!!.defaultFlag) {
                                1 -> ctv_yes.isChecked
                                0 -> ctv_no.isChecked
                            }
                            ctv_personal.isEnabled = false
                            ctv_enterprise.isEnabled = false
                            et_acctNo.isEnabled = false
                            et_acctName.isEnabled = false
                            et_openningBankCode.isEnabled = false
                            et_openningBankName.isEnabled = false
                            et_clearingBankCode.isEnabled = false
                            et_remark.isEnabled = false
                            ctv_yes.isEnabled = false
                            ctv_no.isEnabled = false
                            iv_search.isEnabled = false
                            btn_commit.text = "解除绑定"
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@AddBankCardActivity)
                        }

                        else -> {
                            if (t.msg != null)
                                ToastUtils.showShort(t.msg)
                            else
                                ToastUtils.showShort("异常（代码：${t.code}）")
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    /**
     * 根据银行卡号查询卡信息
     */
    private fun getBankCardInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["cardNo"] = et_acctNo.text.toString()
        RetrofitUtils.get()
            .getJson(GetBankCardInfoUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BankCardInfoResponse::class.java)
                    et_acctName.setText(t.data!!.cardName)
                    et_openningBankCode.setText(t.data!!.bankCode)
                    et_openningBankName.setText(t.data!!.bankName)
                    et_clearingBankCode.setText(t.data!!.clearingBankCode)
                    ToastUtils.showShort(t.msg)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun isComplete(): Boolean {
        return when {
            TextUtils.isEmpty(et_acctNo.text.toString()) -> false
            TextUtils.isEmpty(et_acctName.text.toString()) -> false
            TextUtils.isEmpty(et_openningBankCode.text.toString()) -> false
            TextUtils.isEmpty(et_openningBankName.text.toString()) -> false
            TextUtils.isEmpty(et_clearingBankCode.text.toString()) -> false
            else -> true
        }
    }
}