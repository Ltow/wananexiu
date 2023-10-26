package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.PricingItemAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.ItemBean
import com.bossed.waej.javebean.JieCheResponse
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_pick_by_customer_info.*

class PickByCustomerInfoActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: PricingItemAdapter
    private val list = ArrayList<ItemBean>()

    override fun getLayoutId(): Int {
        return R.layout.activity_pick_by_customer_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_pick_customer_info)
        rv_pricing.layoutManager = LinearLayoutManager(this)
        adapter = PricingItemAdapter(list, 2)
        adapter.bindToRecyclerView(rv_pricing)
        adapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        getInfo()
    }

    override fun initListener() {
        tb_pick_customer_info.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_pricing -> {
                    val intent = Intent(this, PickByCustomerItemInfoActivity::class.java)
                    intent.putExtra("item", GsonUtils.toJson(list[position]))
                    startActivity(intent)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_finished -> onBackPressed()
        }
    }

    private fun getInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getOrderMsg(intent.getStringExtra("id").toInt())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onSubscribe(d: Disposable) {
                    LoadingUtils.dismissLoading()
                }

                override fun onNext(s: String) {
                    val t = GsonUtils.fromJson(s, JieCheResponse::class.java)
                    when (t.code) {
                        200 -> {
                            tv_order_id.text = t.data.orderSn
                            et_kh_name.text = t.data.customerName
                            et_kh_phone.text = t.data.customerPhone
                            tv_car_no.text = t.data.cardNo
                            et_car_type.text = t.data.carName
                            tv_dispatch.text = t.data.dispatchVoList[0].itemName
                            tv_total.text = "￥${t.data.itemCostMoney}"
                            list.addAll(t.data.itemList!!)
                            adapter.setNewData(list)
                        }
                        401 -> {
                            PopupWindowUtils.get()
                                .showLoginOutTimePop(this@PickByCustomerInfoActivity)
                        }
                        703 -> {
                            PopupWindowUtils.get()
                                .showSetConfirmAlertDialog(mContext, t.msg, "去购买", "") {
                                    mContext.startActivity(
                                        Intent(
                                            mContext,
                                            BuyProductActivity::class.java
                                        )
                                    )
                                }
                        }
                        else -> {
                            ToastUtils.showShort(t.msg)
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    LoadingUtils.dismissLoading()
                    ToastUtils.showShort(throwable.message)
                }
            })
    }
}