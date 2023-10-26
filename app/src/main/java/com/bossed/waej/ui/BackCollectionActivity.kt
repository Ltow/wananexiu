package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.BackCollectionAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.CollectionFinishBean
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.BackCollectionResponse
import com.bossed.waej.javebean.PurchaseOrder
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
import java.math.BigDecimal

class BackCollectionActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: BackCollectionAdapter
    private val list = ArrayList<PurchaseOrder>()
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
        tb_collection.title = "退货收款"
        tb_collection.rightTitle = ""
        tv_sel_obj.text = "选择供应商"
        tv_amount.text = "当前应收"
        tv_confirm.text = "收款"
        rv_collection.layoutManager = LinearLayoutManager(this)
        adapter = BackCollectionAdapter(list)
        adapter.bindToRecyclerView(rv_collection)
    }

    override fun initListener() {
        tb_collection.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
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
                            .checkGongYingShangPop(et_search, this@BackCollectionActivity) {
                                isCheck = false
                                searchContent = it.name
                                et_search.setText(it.name)
                                et_search.setSelection(it.name.length)
                                tv_lxr.text = it.contacts
                                tv_phone.text = it.contactsPhone
                                supplierId = it.id.toInt()
                                getOrderList(it.id.toInt())
                            }
                    } else {
                        isCheck = true
                    }
            }
        })
        et_search.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (isCheck) {
                    if (searchContent != et_search.text.toString()) {
                        tv_lxr.text = ""
                        tv_phone.text = ""
                        supplierId = -1
                    }
                    PopupWindowUtils.get()
                        .checkGongYingShangPop(et_search, this@BackCollectionActivity) {
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
        }
        adapter.setOnItemClickListener { adapter, view, position ->
            list[position].isSelected = !list[position].isSelected
            adapter.setNewData(list)
            count()
        }
    }

    private fun count() {
        var total = BigDecimal(0.0)
        list.forEach {
            if (it.isSelected)
                total += BigDecimal(it.moneyOwe)
        }
        tv_total_money.text = "￥${total.setScale(2, BigDecimal.ROUND_HALF_DOWN)}"
    }

    private fun getOrderList(id: Int) {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getBackCollectionList(id)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BackCollectionResponse::class.java)
                    when (t.code) {
                        200 -> {
                            tv_money.text = "￥${t.data!!.moneyOwe}"
                            list.clear()
                            list.addAll(t.data!!.purchaseOrderList)
                            adapter.setNewData(list)
                        }

                        401 -> PopupWindowUtils.get()
                            .showLoginOutTimePop(this@BackCollectionActivity)

                        else ->
                            if (t.msg != null)
                                ToastUtils.showShort(t.msg)
                            else
                                ToastUtils.showShort("异常（代码：${t.code}）")
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                var total = BigDecimal(0.0)
                val selOrders = ArrayList<Int>()
                list.forEach {
                    if (it.isSelected) {
                        selOrders.add(it.id.toInt())
                        total += BigDecimal(it.moneyOwe)
                    }
                }
                val intent = Intent(this, BackCollectionSettleActivity::class.java)
                intent.putIntegerArrayListExtra("sel", selOrders)
                intent.putExtra("supplierId", supplierId)
                intent.putExtra("money", if (selOrders.isEmpty()) 0.00 else total.toDouble())
                intent.putExtra("moneyOwe", tv_money.text.toString())
                intent.putExtra("supplierName", et_search.text.toString())
                startActivity(intent)
            }
        }
    }

    @Subscribe
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