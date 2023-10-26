package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.PaymentAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.CollectionFinishBean
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.PaymentOrderResponse
import com.bossed.waej.javebean.PurchaseOrderRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.TextChangedListener
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_collection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.math.BigDecimal

class PaymentActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: PaymentAdapter
    private val list = ArrayList<PurchaseOrderRow>()
    private var isCheck = true
    private var supplierId = -1
    private var searchContent = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_collection
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_collection)
        et_search.hint = "请输入供应商名称、联系人、电话"
        tb_collection.title = "付款"
        tb_collection.rightTitle = "付款单查询"
        tv_sel_obj.text = "选择供应商"
        tv_amount.text = "当前应付"
        tv_confirm.text="付款"
        rv_collection.layoutManager = LinearLayoutManager(this)
        adapter = PaymentAdapter(list)
        adapter.bindToRecyclerView(rv_collection)
        adapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        tb_collection.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                startActivity(Intent(this@PaymentActivity, PaymentSearchActivity::class.java))
            }
        })
        et_search.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_search.isFocused)
                    if (isCheck) {
                        if (searchContent != s) {
                            tv_lxr.text = ""
                            tv_phone.text = ""
                            supplierId = -1
                        }
                        PopupWindowUtils.get()
                            .checkGongYingShangPop(et_search, this@PaymentActivity) {
                                isCheck = false
                                searchContent = it.name
                                et_search.setText(it.name)
                                et_search.setSelection(it.name.length)
                                tv_lxr.text = it.contacts
                                tv_phone.text = it.contactsPhone
                                supplierId = it.id.toInt()
                                getOrderList(it.id.toInt())
                            }
                    } else
                        isCheck = true
            }
        })
        et_search.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                if (isCheck) {
                    if (searchContent != et_search.text.toString()) {
                        tv_lxr.text = ""
                        tv_phone.text = ""
                        supplierId = -1
                    }
                    PopupWindowUtils.get()
                        .checkGongYingShangPop(et_search, this@PaymentActivity) {
                            isCheck = false
                            searchContent = it.name
                            et_search.setText(it.name)
                            et_search.setSelection(it.name.length)
                            tv_lxr.text = it.contacts
                            tv_phone.text = it.contactsPhone
                            supplierId = it.id.toInt()
                            getOrderList(it.id.toInt())
                        }
                } else
                    isCheck = true
        }
        adapter.setOnItemClickListener { adapter, view, position ->
            list[position].isSelected = !list[position].isSelected
            adapter.setNewData(list)
            count()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                var total = BigDecimal(0.0)
                val selOrders = ArrayList<Int>()
                for (row: PurchaseOrderRow in list) {
                    if (row.isSelected) {
                        selOrders.add(row.id.toInt())
                        total += BigDecimal(row.moneyOwe)
                    }
                }
                val intent = Intent(this, PaymentSettleActivity::class.java)
                intent.putIntegerArrayListExtra("sel", selOrders)
                intent.putExtra("supplierId", supplierId)
                intent.putExtra("money", if (selOrders.isEmpty()) 0.00 else total.toDouble())
                intent.putExtra("moneyOwe", tv_money.text.toString())
                intent.putExtra("supplierName", et_search.text.toString())
                startActivity(intent)
            }
        }
    }

    private fun count() {
        var total = BigDecimal(0.0)
        for (row: PurchaseOrderRow in list) {
            if (row.isSelected)
                total += BigDecimal(row.moneyOwe)
        }
        tv_total_money.text = "￥${total.setScale(2, BigDecimal.ROUND_HALF_DOWN)}"
    }

    private fun getOrderList(supplierId: Int) {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService().getPaymentOrder(supplierId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, PaymentOrderResponse::class.java)
                    tv_money.text = "￥${t.data.moneyOwe}"
                    list.clear()
                    list.addAll(t.data.purchaseOrderList)
                    adapter.setNewData(list)
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(eb: CollectionFinishBean) {
        if (eb.isFinish)
            finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        PopupWindowUtils.get().dismiss()
    }

}