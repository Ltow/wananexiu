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
import com.bossed.waej.http.UrlConstants.UpDateRecordUrl
import com.bossed.waej.javebean.KmOrderGood
import com.bossed.waej.javebean.RecordInfoResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_reservation_info.*

class ReservationInfoActivity : BaseActivity(), View.OnClickListener {
    private val beans = ArrayList<KmOrderGood>()
    private lateinit var memberDetailAdapter: MemberDetailAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_reservation_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_reservation_info)
        rv_record_info.layoutManager = LinearLayoutManager(this)
        memberDetailAdapter = MemberDetailAdapter(beans)
        memberDetailAdapter.bindToRecyclerView(rv_record_info)
        getInfo()
    }

    override fun initListener() {
        tb_reservation_info.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.btn_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get()
                    .showConfirmPop(
                        this,
                        "如需取消预约，请与车主联系协商\n预约车辆：${tv_car_no.text}\n预约时间：${tv_time.text}"
                    ) {
                        LoadingUtils.showLoading(this, "加载中...")
                        val params = HashMap<String, Any>()
                        params["id"] = intent.getStringExtra("id").toInt()
                        params["status"] = "3"
                        RetrofitUtils.get().putJson(
                            UpDateRecordUrl, params, this,
                            object : RetrofitUtils.OnCallBackListener {
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
            }

            R.id.btn_commit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 1) {
                    LoadingUtils.showLoading(this, "加载中...")
                    val params = HashMap<String, Any>()
                    params["id"] = intent.getStringExtra("id").toInt()
                    params["beginTime"] = DateFormatUtils.get().formatDateAndTime(it)
                    RetrofitUtils.get().putJson(
                        UpDateRecordUrl, params, this,
                        object : RetrofitUtils.OnCallBackListener {
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
            }
        }
    }

    private fun getInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getRecordInfo(intent.getStringExtra("id").toInt())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onSubscribe(d: Disposable) {
                    LoadingUtils.dismissLoading()
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, RecordInfoResponse::class.java)
                    when (t.code) {
                        200 -> {
                            when (t.data!!.status) {
                                "4" -> {
                                    ll_bottom.visibility = View.GONE
                                    tb_reservation_info.title = "预约详情"
                                }
                            }
                            tv_time.text = t.data!!.beginTime
                            tv_car_no.text = t!!.data!!.cardNo
                            if (t.data!!.order!!.userCars != null) {
                                tv_brand.text = t.data!!.order!!.userCars!!.brandName
                                tv_type.text = t.data!!.order!!.userCars!!.carTypeName
                                tv_type1.text = when (t.data!!.order!!.userCars!!.carType) {//0乘用1商用
                                    0 -> "乘用"
                                    1 -> "商用"
                                    else -> ""
                                }
                            }
                            if (TextUtils.isEmpty(t.data!!.order!!.packageId)) {
                                beans.addAll(t.data!!.order!!.kmOrderGoods)
                            } else {
                                val good = KmOrderGood()
                                good.goodsName = t.data!!.order!!.packageName
                                good.goodsNum = "1"
                                good.goodsLogo =
                                    if (TextUtils.isEmpty(t.data!!.order!!.logo)) "" else t.data!!.order!!.logo!!
                                good.summary =
                                    if (TextUtils.isEmpty(t.data!!.order!!.summary)) "" else t.data!!.order!!.summary!!
                                beans.add(good)
                            }
                            memberDetailAdapter.setNewData(beans)
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@ReservationInfoActivity)
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
}