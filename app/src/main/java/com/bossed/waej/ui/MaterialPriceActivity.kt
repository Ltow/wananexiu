package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.MaterialPriceAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBSelSupplier
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.JieCheItem
import com.bossed.waej.javebean.JieCheResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_material_price.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MaterialPriceActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var xmAdapter: MaterialPriceAdapter
    private val xmBean = ArrayList<JieCheItem>()
    private var position = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_material_price
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_material_price)
        rv_material_xm.layoutManager = LinearLayoutManager(this)
        xmAdapter = MaterialPriceAdapter(xmBean)
        xmAdapter.bindToRecyclerView(rv_material_xm)
        xmAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        getOrderMsg()
    }

    override fun initListener() {
        tb_material_price.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                val intent = Intent(this@MaterialPriceActivity, OrderHistoryActivity::class.java)
                intent.putExtra("orderStatus", 1)
                intent.putExtra("orderType", 2)
                startActivity(intent)
            }
        })
        xmAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_supplier -> {
                    this.position = position
                    val intent = Intent(this, SupplierListActivity::class.java)
                    intent.putExtra("selSupplier", "select")
                    startActivity(intent)
                }
                R.id.tv_new_supplier -> {
                    startActivity(Intent(this, SupplierMsgActivity::class.java))
                }
            }
        }
        xmAdapter.setOnNumChangeListener(object : MaterialPriceAdapter.OnNumChangeListener {
            override fun onChange(position: Int, num: String, price: String) {
//                xmBean[position].supplyPriceList[0].num = num.toDouble()
//                xmBean[position].supplyPriceList[0].unitPrice = price.toDouble()
//                xmBean[position].supplyPriceList[0].supplyPrice = price.toDouble() * num.toDouble()
            }
        })
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_save -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                updateOrder(1)
            }
            R.id.tv_sure -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                updateOrder(2)
            }
        }
    }

    private fun updateOrder(status: Int) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["id"] = intent.getIntExtra("id", 0)
        params["orderStatus"] = status
        params["itemList"] = xmBean
        RetrofitUtils.get().postJson(
            UrlConstants.PriceSettingUrl,
            params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    when (status) {
                        1 -> {
                            ToastUtils.showShort("保存成功")
                        }
                        2 -> {
                            finish()
                        }
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
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
                    val t = GsonUtils.fromJson(s, JieCheResponse::class.java)
                    when (t.code) {
                        200 -> {
                            tv_car_no.text = t.data.cardNo
                            et_car_type.text = t.data.carName
                            tv_pj_hj.text = "配件收入${t.data.itemMoney}元"
                            tv_bd_sr.text = "本单收入${t.data.orderMoney}元"
                            tv_gsf.text = "工时费${t.data.itemServiceFee}元"
                            tv_hj.text = "￥${t.data.orderMoney}"
                            tv_moneyTotalItem.text = "￥${t.data.itemMoney}"
                            tv_grossProfitMoney.text = "￥${t.data.grossProfitMoney}"
                            if (t.data.itemList != null) {
//                                xmBean.addAll(t.data.itemList)
//                                xmAdapter.setNewData(xmBean)
                            }
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@MaterialPriceActivity)
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
                    ToastUtils.showShort(throwable.message)
                }
            })
    }

    @Subscribe
    fun onSelSupplierCallBack(eb: EBSelSupplier) {
//        if (eb.list.isNotEmpty()) {
//            val list = ArrayList<SupplyPriceBean>()
//            val bean = eb.list[0]
//            list.add(
//                SupplyPriceBean(
//                    bean.contacts,
//                    bean.contacts2,
//                    bean.contacts2Phone,
//                    bean.contactsPhone,
//                    bean.id,
//                    xmBean[position].id,
//                    xmBean[position].itemName,
//                    bean.name,
//                    1.00,
//                    intent.getIntExtra("id", 0),
//                    "",
//                    bean.shopId,
//                    0.00,
//                    bean.supplierId,
//                    bean.tenantId,
//                    0.00,
//                )
//            )
//            xmBean[position].supplyPriceList = list
//            xmAdapter.setNewData(xmBean)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}