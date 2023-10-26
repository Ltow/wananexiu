package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.PickBySupplierInfoAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.PickBySupplierInfoDetail
import com.bossed.waej.javebean.PickBySupplierInfoResponse
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_pick_his_by_supplier_info.*

class PickBySupplierInfoActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: PickBySupplierInfoAdapter
    private val list = ArrayList<PickBySupplierInfoDetail>()

    override fun getLayoutId(): Int {
        return R.layout.activity_pick_his_by_supplier_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_pick_supplier_info)
        rv_pricing.layoutManager = LinearLayoutManager(this)
        adapter = PickBySupplierInfoAdapter(list)
        adapter.bindToRecyclerView(rv_pricing)
        when (intent.getStringExtra("type")) {
            "1" -> getInfo()
            "2" -> getInfoFrSn()
        }
    }

    override fun initListener() {
        tb_pick_supplier_info.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.tv_finished -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }
        }
    }

    private fun getInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService().getPurchaseInfo(intent.getStringExtra("id").toInt())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, PickBySupplierInfoResponse::class.java)
                    when (t.code) {
                        200 -> {
                            tv_order_id.text = t.data.orderSn
                            et_kh_name.text = t.data.supplierName
                            tv_car_no.text = t.data.searchKeywords.split(";")[0]
                            tv_total.text = "${t.data.amount}元"
                            list.addAll(t.data.detailList)
                            adapter.setNewData(list)
                        }
                        401 -> {
                            PopupWindowUtils.get()
                                .showLoginOutTimePop(this@PickBySupplierInfoActivity)
                        }
                        else -> ToastUtils.showShort(t.msg)
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    private fun getInfoFrSn() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService().getPurchaseInfoFrSn(intent.getStringExtra("id"))
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, PickBySupplierInfoResponse::class.java)
                    tv_order_id.text = t.data.orderSn
                    et_kh_name.text = t.data.supplierName
                    tv_car_no.text = t.data.searchKeywords.split(";")[0]
                    tv_total.text = "${t.data.amount}元"
                    list.addAll(t.data.detailList)
                    adapter.setNewData(list)
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }
}