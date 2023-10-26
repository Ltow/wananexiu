package com.bossed.waej.util

import android.app.Activity
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.base.BaseApplication
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.ItemBean
import com.bossed.waej.javebean.JieCheResponse
import com.bossed.waej.javebean.PrintBodyBean
import com.bossed.waej.javebean.PrintSettingBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class BluetoothPrintUtils {

    companion object {
        private val headSettings = ArrayList<PrintSettingBean>()//表头
        private val bodySettings = ArrayList<PrintSettingBean>()//表体
        private val footSettings = ArrayList<PrintSettingBean>()//表尾

        init {
            headSettings.add(PrintSettingBean(States.PrintSettingType.COMPANY, true, "表头", "服务工单"))
            headSettings.add(PrintSettingBean(States.PrintSettingType.HEAD, true, "单号", ""))
            headSettings.add(PrintSettingBean(States.PrintSettingType.HEAD, true, "时间", ""))
            headSettings.add(PrintSettingBean(States.PrintSettingType.HEAD, true, "车牌号", ""))
            headSettings.add(PrintSettingBean(States.PrintSettingType.HEAD, false, "会员卡号", ""))
            headSettings.add(PrintSettingBean(States.PrintSettingType.CODE, false, "二维码", ""))

            bodySettings.add(PrintSettingBean(States.PrintSettingType.BODY, true, "服务项目", ""))
            bodySettings.add(PrintSettingBean(States.PrintSettingType.BODY, true, "数量", ""))
            bodySettings.add(PrintSettingBean(States.PrintSettingType.BODY, true, "单价", ""))
            bodySettings.add(PrintSettingBean(States.PrintSettingType.BODY, true, "金额", ""))
            bodySettings.add(PrintSettingBean(States.PrintSettingType.BODY, true, "工时费", ""))

            footSettings.add(PrintSettingBean(States.PrintSettingType.FOOT, false, "合计", ""))
            footSettings.add(PrintSettingBean(States.PrintSettingType.NOTE, true, "备注", ""))
            footSettings.add(PrintSettingBean(States.PrintSettingType.CODE, false, "二维码", ""))
        }

        fun printService(id: String, activity: Activity, printType: Int, orderType: Int) {
            when (printType) {
                States.OrderPrintType.frID -> Api.getInstance().getApiService()//按id
                    .getOrderMsg(id.toInt())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : BaseResourceObserver<String>() {
                        override fun onSubscribe(d: Disposable) {
                            LoadingUtils.dismissLoading()
                        }

                        override fun onNext(s: String) {
                            analysis(s, activity, orderType)
                        }

                        override fun onError(throwable: Throwable) {
                            ToastUtils.showShort(throwable.message)
                        }
                    })
                States.OrderPrintType.frSN -> Api.getInstance().getApiService()//按单号
                    .getOrderMsgFrSn(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : BaseResourceObserver<String>() {
                        override fun onComplete() {
                            LoadingUtils.dismissLoading()
                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(s: String) {
                            analysis(s, activity, orderType)
                        }

                        override fun onError(throwable: Throwable) {
                            ToastUtils.showShort(throwable.message)
                        }
                    })
            }

        }

       private fun analysis(s: String, activity: Activity, orderType: Int) {
            val t = GsonUtils.fromJson(s, JieCheResponse::class.java)
            when (t.code) {
                200 -> {
                    val printBodyData = ArrayList<PrintBodyBean>()
                    val totalPrintData = ArrayList<PrintSettingBean>()
                    for (head: PrintSettingBean in headSettings) {
                        if (head.text == "表头")
                            head.content = when (orderType) {
                                1 -> "服务工单"
                                2 -> "历史工单"
                                else -> ""
                            }
                        if (head.text == "单号")
                            head.content = t.data.orderSn
                        if (head.text == "时间")
                            head.content = t.data.createTime
                        if (head.text == "车牌号")
                            head.content = t.data.cardNo
                    }
                    for (item: ItemBean in t.data.itemList!!) {
                        val bodyBean = PrintBodyBean();
                        bodyBean.itemName = item.itemName
                        bodyBean.num = item.num.toString()
                        bodyBean.price = item.unitPrice.toString()
                        bodyBean.money = item.itemMoney.toString()
                        bodyBean.serviceFee = item.serviceFee.toString()
                        printBodyData.add(bodyBean)
                    }
//                                for (foot: PrintSettingBean in footSettings) {
//                                    if (foot.text == "合计")
//                                        foot.content = t.data.orderMoney.toString()
//                                }
                    totalPrintData.add(
                        PrintSettingBean(
                            States.PrintSettingType.TOTAL,
                            "合计", t.data.orderMoney.toString()
                        )
                    )
                    val mPrintFormat = PrintFormat(
                        activity,
                        (activity.application as BaseApplication).getSocket(),
                        headSettings,
                        bodySettings,
                        footSettings,
                        printBodyData,
                        totalPrintData
                    )
                    mPrintFormat.print()
                }
                401 -> {
                    PopupWindowUtils.get().showLoginOutTimePop(activity)
                }
                else -> {
                    ToastUtils.showShort(t.msg)
                }
            }
        }
    }

}