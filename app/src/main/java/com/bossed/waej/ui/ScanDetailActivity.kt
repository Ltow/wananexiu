package com.bossed.waej.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.MemberDetailAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.http.UrlConstants.ActivateUrl
import com.bossed.waej.http.UrlConstants.FinishUrl
import com.bossed.waej.javebean.KmOrderGood
import com.bossed.waej.javebean.MemberDetailBean
import com.bossed.waej.javebean.ScanDetailResponse
import com.bossed.waej.util.BluetoothPrintUtils
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_scan_detail.*

class ScanDetailActivity : BaseActivity(), View.OnClickListener {
    private lateinit var memberDetailAdapter: MemberDetailAdapter
    private val beans = ArrayList<KmOrderGood>()
    private var orderId = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_scan_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_scan_detail)
        rv_member_detail.layoutManager = LinearLayoutManager(this)
        memberDetailAdapter = MemberDetailAdapter(beans)
        memberDetailAdapter.bindToRecyclerView(rv_member_detail)
        memberDetailAdapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        when (intent.getIntExtra("type", -1)) {
            0 -> {
                tv_activate.text = "服务完毕"
                tv_activate.setBackgroundResource(R.drawable.shape_blue_gradient_bg)
                getOrderInfoForId()
            }

            1 -> {
                getOrderInfo()
            }
        }
    }

    override fun initListener() {
        tb_scan_detail.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.tv_activate -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when (intent.getIntExtra("type", -1)) {
                    0 -> PopupWindowUtils.get().showConfirmPop(this, "是否确认服务完毕？") {
                        finished()
                    }

                    1 -> PopupWindowUtils.get().showConfirmPop(this, "是否确认进厂施工？") {
                        activate()
                    }
                }
            }
        }
    }

    private fun finished() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["orderId"] = orderId
        RetrofitUtils.get()
            .post(FinishUrl, params, this, object : RetrofitUtils.OnCallBackListener {
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

    private fun activate() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["orderId"] = orderId
        RetrofitUtils.get()
            .post(ActivateUrl, params, this, object : RetrofitUtils.OnCallBackListener {
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

    /**
     * 列表详情
     */
    private fun getOrderInfoForId() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()//按id
            .getOnlineOrderMsg(intent.getStringExtra("id").toInt())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onSubscribe(d: Disposable) {
                    LoadingUtils.dismissLoading()
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, ScanDetailResponse::class.java)
                    when (t.code) {
                        200 -> {
                            orderId = intent.getStringExtra("id")
                            tv_orderSn.text = t.data!!.orderSn
                            tv_serviceFee.text = "￥${t.data!!.orderMoney}"
                            if (t.data!!.userCars != null) {
                                tv_cardNo.text = t.data!!.userCars!!.cardNo
                                tv_vnCode.text = t.data!!.userCars!!.vnCode
                                tv_carName.text = t.data!!.userCars!!.carName
                            }
                            if (t.data!!.kmAppointmentRecord != null) {
                                tv_driverName.text = t.data!!.kmAppointmentRecord!!.driverName
                                tv_driverPhone.text = t.data!!.kmAppointmentRecord!!.driverPhone
                            }
                            if (TextUtils.isEmpty(t.data!!.packageId)) {
                                beans.addAll(t.data!!.kmOrderGoods)
                            } else {
                                val good = KmOrderGood()
                                good.goodsName = t.data!!.packageName
                                good.goodsNum = "1"
                                good.goodsLogo =
                                    if (TextUtils.isEmpty(t.data!!.logo)) "" else t.data!!.logo
                                good.summary =
                                    if (TextUtils.isEmpty(t.data!!.summary)) "" else t.data!!.summary
                                beans.add(good)
                            }
                            memberDetailAdapter.setNewData(beans)
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@ScanDetailActivity)
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

    /**
     * 扫码详情
     */
    private fun getOrderInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["consumptionCode"] = intent.getStringExtra("scanResult")
        RetrofitUtils.get()
            .getJson(
                UrlConstants.ScanOrderInfoUrl, params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        LogUtils.d("tag", s)
                        val t = GsonUtils.fromJson(s, ScanDetailResponse::class.java)
                        orderId = t.data!!.id!!
                        tv_orderSn.text = t.data!!.orderSn
                        tv_serviceFee.text = "￥${t.data!!.orderMoney}"
                        if (t.data!!.userCars != null) {
                            tv_cardNo.text = t.data!!.userCars!!.cardNo
                            tv_vnCode.text = t.data!!.userCars!!.vnCode
                            tv_carName.text = t.data!!.userCars!!.carName
                        }
                        if (t.data!!.kmAppointmentRecord != null) {
                            tv_driverName.text = t.data!!.kmAppointmentRecord!!.driverName
                            tv_driverPhone.text = t.data!!.kmAppointmentRecord!!.driverPhone
                        }
                        if (TextUtils.isEmpty(t.data!!.packageId)) {
                            beans.addAll(t.data!!.kmOrderGoods)
                        } else {
                            val good = KmOrderGood()
                            good.goodsName = t.data!!.packageName
                            good.goodsNum = "1"
                            good.goodsLogo =
                                if (TextUtils.isEmpty(t.data!!.logo)) "" else t.data!!.logo
                            good.summary =
                                if (TextUtils.isEmpty(t.data!!.summary)) "" else t.data!!.summary
                            beans.add(good)
                        }
                        memberDetailAdapter.setNewData(beans)
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }
}