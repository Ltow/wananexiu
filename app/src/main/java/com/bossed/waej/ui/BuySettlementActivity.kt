package com.bossed.waej.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alipay.sdk.app.PayTask
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.SettlementProductAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.BuyUrl
import com.bossed.waej.javebean.*
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.OnNoRepeatClickListener
import com.bossed.waej.util.RetrofitUtils
import com.bossed.waej.util.wxpay.WeChatService
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_buy_settlement.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.math.BigDecimal
import com.bossed.waej.util.DoubleClicksUtils

class BuySettlementActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var buyProductAdapter: SettlementProductAdapter
    private val buyList = ArrayList<BuyProductData>()
    private val SDK_PAY_FLAG = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_buy_settlement
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).init()
        setMarginTop(tb_buy_settle)
        BarUtils.setStatusBarLightMode(window, true)
        rv_product.layoutManager = LinearLayoutManager(this)
        buyProductAdapter = SettlementProductAdapter(
            buyList,
            if (TextUtils.isEmpty(intent.getStringExtra("date"))) "" else intent.getStringExtra("date")
        )
        buyProductAdapter.bindToRecyclerView(rv_product)
        buyProductAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        ll_top.visibility =
            if (TextUtils.isEmpty(intent.getStringExtra("date"))) View.GONE else View.VISIBLE
        tv_date.text = intent.getStringExtra("date")
        tv_surplus.text = intent.getLongExtra("surplus", 0).toString()
        val t = GsonUtils.fromJson(intent.getStringExtra("selList"), SelProductBean::class.java)
        buyList.addAll(t)
        buyProductAdapter.setNewData(buyList)
        var total = BigDecimal(0.0)
        if (TextUtils.isEmpty(intent.getStringExtra("date")))
            for (data: BuyProductData in buyList) {
                total += BigDecimal(data.priceYear).multiply(BigDecimal(data.num))
            }
        else
            for (data: BuyProductData in buyList) {
                total += BigDecimal(data.priceDay).multiply(BigDecimal(data.num))
                    .multiply(BigDecimal(intent.getLongExtra("surplus", 0)))
            }
        tv_total.text = "￥$total"
    }

    override fun initListener() {
        tb_buy_settle.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.ctv_alli -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_alli.isChecked = !ctv_alli.isChecked
                ctv_wx.isChecked = !ctv_alli.isChecked
            }
            R.id.ctv_wx -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_wx.isChecked = !ctv_wx.isChecked
                ctv_alli.isChecked = !ctv_wx.isChecked
            }
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                LoadingUtils.showLoading(this, "加载中...")
                val params = HashMap<String, Any>()
                if (ctv_alli.isChecked)
                    params["payMethod"] = 2
                if (ctv_wx.isChecked)
                    params["payMethod"] = 1
                val prdocutList = ArrayList<HashMap<String, Any>>()
                for (i: Int in buyList.indices) {
                    val map = HashMap<String, Any>()
                    map["orderType"] = buyList[i].orderType
                    map["productId"] = buyList[i].id
                    map["num"] = buyList[i].num
                    prdocutList.add(map)
                }
                params["prdocutList"] = prdocutList
                RetrofitUtils.get()
                    .postJson(BuyUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                        override fun onSuccess(s: String) {
                            val t = GsonUtils.fromJson(s, SettlementResponse::class.java)
                            data = GsonUtils.toJson(t.data.order)
                            money = "￥${t.data.order.orderMoney}"
                            if (ctv_wx.isChecked) {
                                val payString =
                                    GsonUtils.fromJson(
                                        GsonUtils.toJson(t.data.payString),
                                        PayString::class.java
                                    )
                                WeChatService.GetInstance(this@BuySettlementActivity)
                                    .openWeChatPay(
                                        payString.appid,
                                        payString.partnerid,
                                        payString.prepayid,
                                        payString.noncestr,
                                        payString.timestamp,
                                        payString.sign
                                    )
                            }
                            if (ctv_alli.isChecked) {
                                val payRunnable = Runnable {
                                    val alipay = PayTask(this@BuySettlementActivity)
                                    val result = alipay.pay(t.data.payString.toString(), true)
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

                        override fun onFailed(e: String) {
                            ToastUtils.showShort(e)
                        }
                    })
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == SDK_PAY_FLAG) {
                val result = msg.obj.toString().split(";")
                LogUtils.d(result[0].substring(14..17))
//                val payResult = PayResult(msg.obj as Map<String, String>)
//                val resultInfo = payResult.result // 同步返回需要验证的信息
//                val resultStatus = payResult.resultStatus
                /**
                 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                // 判断resultStatus 为9000则代表支付成功
                when (result[0].substring(14..17)) {
                    "9000" -> {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        val intent =
                            Intent(this@BuySettlementActivity, SuccessActivity::class.java)
                        intent.putExtra("data", data)
                        intent.putExtra("money", money)
                        startActivity(intent)
                        finish()
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

    private var data = ""
    private var money = ""

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(eb: EBWxPayBean) {
        if (eb.payResponse) {
            val intent =
                Intent(this@BuySettlementActivity, SuccessActivity::class.java)
            intent.putExtra("data", data)
            intent.putExtra("money", money)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}