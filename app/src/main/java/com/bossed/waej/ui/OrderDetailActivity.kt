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
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.KmOrderGood
import com.bossed.waej.javebean.ScanDetailResponse
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_order_detail.*

class OrderDetailActivity : BaseActivity() {
    private lateinit var memberDetailAdapter: MemberDetailAdapter
    private val beans = ArrayList<KmOrderGood>()

    override fun getLayoutId(): Int {
        return R.layout.activity_order_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_detail)
        rv_order_detail.layoutManager = LinearLayoutManager(this)
        memberDetailAdapter = MemberDetailAdapter(beans)
        memberDetailAdapter.bindToRecyclerView(rv_order_detail)
        getOrderInfo()
    }

    override fun initListener() {
        tb_detail.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    private fun getOrderInfo() {
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
                            tv_orderMoney.text = "${t.data!!.orderMoney}元"
                            tv_orderSn.text = t.data!!.orderSn
                            tv_create_time.text = t.data!!.addTime
                            tv_shippingTime.text = t.data!!.shippingTime
                            tv_finishedTime.text = t.data!!.shippingTime
                            tv_cardNo.text = t.data!!.cardNo
                            tv_status.text = when (t.data!!.shippingStatus) {
                                "1" -> "服务中"
                                "2" -> "待确认"
                                "3" -> "已确认"
                                else -> ""
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
                            PopupWindowUtils.get().showLoginOutTimePop(this@OrderDetailActivity)
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