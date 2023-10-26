package com.bossed.waej.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.util.OnNoRepeatClickListener
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_recharge.*
import com.alipay.sdk.app.PayTask
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.javebean.PayResult
import com.bossed.waej.util.DoubleClicksUtils


class RechargeActivity : BaseActivity(), OnNoRepeatClickListener {
    private val SDK_PAY_FLAG = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_recharge
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_recharge)
        switchMoney(0)
    }

    override fun initListener() {
        tb_recharge.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.cb_100 -> switchMoney(0)
            R.id.cb_200 -> switchMoney(1)
            R.id.cb_300 -> switchMoney(2)
            R.id.cb_500 -> switchMoney(3)
            R.id.ctv_alli -> switchType(0)
            R.id.ctv_wx -> switchType(1)
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val orderInfo: String = "info" // 订单信息
                val payRunnable = Runnable {
                    val alipay = PayTask(this)
                    val result = alipay.pay(orderInfo, true)
                    val msg = Message()
                    msg.what = SDK_PAY_FLAG
                    msg.obj = result
                    mHandler.sendMessage(msg)
                }
                // 必须异步调用
                val payThread = Thread(payRunnable)
                payThread.start()
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == SDK_PAY_FLAG) {
                val payResult = PayResult(msg.obj as Map<String, String>)
                /**
                 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                val resultInfo: String = payResult.result // 同步返回需要验证的信息
                val resultStatus: String = payResult.resultStatus
                LogUtils.i(
                    "tag",
                    "payResult*****$payResult"
                )
                // 判断resultStatus 为9000则代表支付成功
                when (resultStatus) {
                    "9000" -> {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        showAlertPop()
                    }
                    "8000" -> {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showShort("支付正在处理中")
                    }
                    "4000" -> {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showShort("支付失败")
                    }
                    "5000" -> {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showShort("重复支付")
                    }
                    "6001" -> {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showShort("取消支付")
                    }
                    "6002" -> {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showShort("网络链接错误")
                    }
                    "6004" -> {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showShort("支付正在处理中")
                    }
                    else -> {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showShort("其他支付错误")
                    }
                }
            }
        }
    }

    private fun switchType(type: Int) {
        ctv_alli.isChecked = false
        ctv_wx.isChecked = false
        when (type) {
            0 -> ctv_alli.isChecked = true
            1 -> ctv_wx.isChecked = true
        }
    }

    private fun switchMoney(num: Int) {
        cb_100.isChecked = false
        cb_100.setTextColor(Color.parseColor("#333333"))
        cb_200.isChecked = false
        cb_200.setTextColor(Color.parseColor("#333333"))
        cb_300.isChecked = false
        cb_300.setTextColor(Color.parseColor("#333333"))
        cb_500.isChecked = false
        cb_500.setTextColor(Color.parseColor("#333333"))
        when (num) {
            0 -> {
                cb_100.isChecked = true
                cb_100.setTextColor(Color.parseColor("#ffffff"))
            }
            1 -> {
                cb_200.isChecked = true
                cb_200.setTextColor(Color.parseColor("#ffffff"))
            }
            2 -> {
                cb_300.isChecked = true
                cb_300.setTextColor(Color.parseColor("#ffffff"))
            }
            3 -> {
                cb_500.isChecked = true
                cb_500.setTextColor(Color.parseColor("#ffffff"))
            }
        }
    }
}