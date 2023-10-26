package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.ItemAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.ItemBean
import com.bossed.waej.javebean.ItemDispatchBean
import com.bossed.waej.javebean.JieCheResponse
import com.bossed.waej.util.*
import com.bossed.waej.util.BluetoothUtils.Companion.bluetoothUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_order_history_msg.*
import java.lang.StringBuilder
import java.math.BigDecimal

class OrderHistoryMsgActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var itemAdapter: ItemAdapter
    private val items = ArrayList<ItemBean>()
    private var discount = ""
    private var orderMoney = 0.0
    private var realMoney = ""
    private var moneyOwe = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_order_history_msg
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_order_history)
        rv_history_order.layoutManager = LinearLayoutManager(this)
        itemAdapter = ItemAdapter(States.HISTORY, items)
        itemAdapter.bindToRecyclerView(rv_history_order)
        itemAdapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        when (intent.getStringExtra("type")) {
            "1" -> getOrderMsg()
            "2" -> getOrderMsgFrSn()
        }
    }

    override fun initListener() {
        tb_order_history.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                bluetoothUtils(
                    this@OrderHistoryMsgActivity,
                    when (intent.getStringExtra("type")) {
                        "2" -> States.OrderPrintType.frSN
                        else -> States.OrderPrintType.frID
                    },
                    when (intent.getStringExtra("type")) {
                        "2" -> intent.getStringExtra("id")
                        else -> intent.getIntExtra("id", 0).toString()
                    }, 2
                ).print()
            }
        })
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_finished -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                finish()
            }

            R.id.tv_total_item -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (items.isEmpty())
                    return
                PopupWindowUtils.get().showSelectedItemPop(
                    items, this, tv_total_item.text.toString(), tv_price.text.toString(),
                    "￥${BigDecimal(discount).setScale(2, BigDecimal.ROUND_HALF_DOWN)}",
                    "￥${BigDecimal(realMoney).setScale(2, BigDecimal.ROUND_HALF_DOWN)}",
                    "￥${BigDecimal(moneyOwe).setScale(2, BigDecimal.ROUND_HALF_DOWN)}"
                )
            }

            R.id.tv_pics -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, WorkPicActivity::class.java)
                intent.putExtra("id", getIntent().getIntExtra("id", 0))
                intent.putExtra("type", "history")
                startActivity(intent)
            }
        }
    }

    private fun getOrderMsg() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getOrderMsg(intent.getIntExtra("id", 0))
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
                    val t = GsonUtils.fromJson(s, JieCheResponse::class.java)
                    when (t.code) {
                        200 -> {
                            tv_order_no.text = t.data.orderSn
                            et_kh_name.text = t.data.customerName
                            et_kh_phone.text = t.data.customerPhone
                            tv_car_no.text = t.data.cardNo
                            et_car_type.text = t.data.carName
                            et_car_vin.text = t.data.vnCode
                            et_jdr.text = t.data.receiveBy
                            tv_jd_time.text = t.data.receiveTime
                            et_lc.text = t.data.mileage
                            tv_price.text = "￥${t.data.orderMoney}"
                            discount = t.data.billVoList.discount!!
                            realMoney = t.data.billVoList.payable!!
                            moneyOwe = t.data.billVoList.moneyOwe!!
                            orderMoney = t.data.orderMoney
                            val sb = StringBuilder()
                            for (row: ItemDispatchBean in t.data.dispatchVoList) {
                                if (row.type == "1") {
                                    if (sb.isNotEmpty())
                                        sb.append(",")
                                    sb.append(row.staffName)
                                }
                            }
                            tv_dispatch.text = sb.toString()
                            if (t.data.itemList != null) {
                                items.addAll(t.data.itemList)
                                itemAdapter.setNewData(items)
                            }
                            tv_total_item.text = "共${items.size}项"
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@OrderHistoryMsgActivity)
                        }

                        else -> {
                            ToastUtils.showShort(t.msg)
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                }
            })
    }

    private fun getOrderMsgFrSn() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getOrderMsgFrSn(intent.getStringExtra("id"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(s: String) {
                    val t = GsonUtils.fromJson(s, JieCheResponse::class.java)
                    when (t.code) {
                        200 -> {
                            tv_order_no.text = t.data.orderSn
                            et_kh_name.text = t.data.customerName
                            et_kh_phone.text = t.data.customerPhone
                            tv_car_no.text = t.data.cardNo
                            et_car_type.text = t.data.carName
                            et_car_vin.text = t.data.vnCode
                            et_jdr.text = t.data.receiveBy
                            tv_jd_time.text = t.data.receiveTime
                            et_lc.text = t.data.mileage
                            orderMoney = t.data.orderMoney
                            discount = t.data.billVoList.discount!!
                            realMoney = t.data.billVoList.payable!!
                            moneyOwe = t.data.billVoList.moneyOwe!!
//                            var total = BigDecimal(0.0)
//                            for (item: ItemBean in t.data.itemList!!) {
//                                total = total.add(
//                                    BigDecimal(item.num).multiply(BigDecimal(item.unitPrice)).add(
//                                        BigDecimal(item.serviceFee).setScale(
//                                            2,
//                                            BigDecimal.ROUND_HALF_DOWN
//                                        )
//                                    )
//                                )
//                            }
                            tv_price.text = "￥${t.data.orderMoney}"
                            val sb = StringBuilder()
                            for (row: ItemDispatchBean in t.data.dispatchVoList) {
                                if (row.type == "1") {
                                    if (sb.isNotEmpty())
                                        sb.append(",")
                                    sb.append(row.staffName)
                                }
                            }
                            tv_dispatch.text = sb.toString()
                            if (t.data.itemList != null) {
                                items.addAll(t.data.itemList)
                                itemAdapter.setNewData(items)
                            }
                            tv_total_item.text = "共${items.size}项"
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@OrderHistoryMsgActivity)
                        }

                        else -> {
                            ToastUtils.showShort(t.msg)
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                }
            })
    }
}