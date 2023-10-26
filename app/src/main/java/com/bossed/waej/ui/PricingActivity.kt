package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.PricingItemAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants.PriceUrl
import com.bossed.waej.javebean.*
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_pricing.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.math.BigDecimal

/**
 * 进货划价单
 */
class PricingActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: PricingItemAdapter
    private val list = ArrayList<ItemBean>()
    private var position = -1
    private var isEdit = false//判断是否编辑内容

    override fun getLayoutId(): Int {
        return R.layout.activity_pricing
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_pricing)
        rv_pricing.layoutManager = LinearLayoutManager(this)
        adapter = PricingItemAdapter(list, 1)
        adapter.bindToRecyclerView(rv_pricing)
        getInfo()
    }

    override fun initListener() {
        tb_pricing.setOnTitleBarListener(object : OnTitleBarListener {
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
                    val intent = Intent(this, PricingMsgActivity::class.java)
                    intent.putExtra("item", GsonUtils.toJson(list[position]))
                    startActivity(intent)
                    this.position = position
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_finished -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                var isPricingFinish = false
                for (item: ItemBean in list) {
                    if (item.supplyPriceList.isEmpty())
                        isPricingFinish = true
                }
                if (isPricingFinish)
                    PopupWindowUtils.get()
                        .showSetConfirmAlertDialog(
                            this,
                            "还存在未划价的项目，强行确认划价完毕，未划价项目的进货成本自动记为0",
                            "强行确认", "#3477FC"
                        ) {
                            pricing(2)
                        }
                else
                    PopupWindowUtils.get().showConfirmPop(this, "确定后无法修改，是否确定？") {
                        pricing(2)
                    }
            }
            R.id.tv_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                pricing(1)
            }
            R.id.ll_profit -> {
                val popWindow = PopWindow.Builder(this).setView(R.layout.layout_pop_profit)
                    .setWidthAndHeight(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    ).setOutsideTouchable(true)
                    .setAnimStyle(R.style.CenterAnimation)
                    .setChildrenView { contentView, pop ->
                        contentView.setOnClickListener {
                            pop.dismiss()
                        }
                    }.create()
                popWindow.isClippingEnabled = false
                popWindow.isFocusable = true
                popWindow.showOnTargetTop(tv_cancel, PopWindow.LEFT_TOP, 0, ConvertUtils.dp2px(-1f))
            }
        }
    }

    private fun pricing(orderStatus: Int) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["orderStatus"] = orderStatus
        params["id"] = intent.getStringExtra("id").toInt()
        params["itemList"] = list
        RetrofitUtils.get()
            .postJson(PriceUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    ToastUtils.showShort(t.msg)
                    if (t.code == 200 && orderStatus == 2)
                        finish()
                    if (isEdit)
                        finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
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
                            PopupWindowUtils.get().showLoginOutTimePop(this@PricingActivity)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(eb: EBAddPricing) {
        list[position].supplyPriceList.clear()
        list[position].supplyPriceList.addAll(eb.bean)
        adapter.setNewData(list)
        countTotal()
        isEdit = true
    }

    private fun countTotal() {
        var total = BigDecimal(0.0)
        for (item: ItemBean in list) {
            for (bean: SupplyPriceBean in item.supplyPriceList) {
                total += BigDecimal(bean.unitPrice!!).multiply(BigDecimal(bean.num!!))
            }
        }
        tv_total.text = "￥${total.setScale(2, BigDecimal.ROUND_HALF_DOWN)}"
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {
        if (isEdit) {
            PopupWindowUtils.get().showConfirmAndCancelPop(this, "是否保存领料单？",
                object : PopupWindowUtils.OnConfirmAndCancelListener {
                    override fun onConfirm() {
                        pricing(1)
                    }

                    override fun onCancel() {
                        finish()
                    }
                })
        } else
            super.onBackPressed()
    }
}