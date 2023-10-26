package com.bossed.waej.ui

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.CashOutResponse
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cash_out_details.*

class CashOutDetailsActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_cash_out_details
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_cash_out_details)
        if (TextUtils.isEmpty(intent.getStringExtra("detail")))
            getInfo()
        else {
            binding(intent.getStringExtra("detail"))
        }
    }

    override fun initListener() {
        tb_cash_out_details.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    private fun getInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getWithdrawalInfo(intent.getStringExtra("id").toInt())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    when (t.code) {
                        200 -> binding(s)
                        401 -> PopupWindowUtils.get()
                            .showLoginOutTimePop(this@CashOutDetailsActivity)

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

    private fun binding(s: String) {
        val t = GsonUtils.fromJson(s, CashOutResponse::class.java)
        tv_money.text = t.data!!.drawAmt//金额
        tv_money1.text = "￥${t.data!!.drawAmt}"//金额
        tv_orderNo.text = t.data!!.orderNo
        tv_acctNo.text = t.data!!.acctNo
        tv_submit_date.text = t.data!!.createTime//提交日期
        tv_result_date.text = t.data!!.updateTime//更新日期
        tv_failCause.text = t.data!!.failCause//失败原因
        when (t.data!!.status!!) {//提现状态 0提现中 1成功 2失败
            0 -> {
                v_result.visibility = View.GONE
                iv_result.visibility = View.GONE
                tv_result.visibility = View.GONE
                tv_result_date.visibility = View.GONE
            }

            1 -> {
                v_result.setBackgroundColor(Color.parseColor("#22C134"))
                iv_result.setImageResource(R.mipmap.icon_success_tx)
                tv_result.setTextColor(Color.parseColor("#22C134"))
                tv_result_date.setTextColor(Color.parseColor("#22C134"))
                tv_result.text = "提现成功"
                v_result.visibility = View.VISIBLE
                iv_result.visibility = View.VISIBLE
                tv_result.visibility = View.VISIBLE
                tv_result_date.visibility = View.VISIBLE
            }

            2 -> {
                rl_failCause.visibility = View.VISIBLE
                v_result.visibility = View.VISIBLE
                iv_result.visibility = View.VISIBLE
                tv_result.visibility = View.VISIBLE
                tv_result_date.visibility = View.VISIBLE
            }
        }
    }
}