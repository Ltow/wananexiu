package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.PurchaseAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.eventbus.EBSelectPart
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants.PurchaseFinishUrl
import com.bossed.waej.http.UrlConstants.PurchaseOrderUrl
import com.bossed.waej.javebean.*
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_purchase.*
import kotlinx.android.synthetic.main.activity_purchase_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.math.BigDecimal

class PurchaseActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: PurchaseAdapter
    private val list = ArrayList<PartListRow>()
    private var isCheck = true
    private var supplierId = ""
    private var searchContent = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_purchase
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_purchase)
        rv_purchase.layoutManager = LinearLayoutManager(this)
        adapter = PurchaseAdapter(list)
        adapter.bindToRecyclerView(rv_purchase)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        if (!TextUtils.isEmpty(intent.getStringExtra("id"))) {
            tv_delete.visibility = View.VISIBLE
            tv_confirm.text = "保存"
            getInfo()
        }
    }

    override fun initListener() {
        tb_purchase.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        et_name.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_name.isFocused)
                    if (isCheck) {
                        if (searchContent != s) {
                            et_contacts.text = ""
                            et_phone.text = ""
                            supplierId = ""
                        }
                        PopupWindowUtils.get()
                            .checkGongYingShangPop(et_name, this@PurchaseActivity) {
                                isCheck = false
                                searchContent = it.name
                                et_name.setText(it.name)
                                et_name.setSelection(it.name.length)
                                et_contacts.text = it.contacts
                                et_phone.text = it.contactsPhone
                                supplierId = it.id
                            }
                    } else
                        isCheck = true
            }
        })
        et_name.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                if (isCheck) {
                    if (searchContent != et_name.text.toString()) {
                        et_contacts.text = ""
                        et_phone.text = ""
                        supplierId = ""
                    }
                    PopupWindowUtils.get()
                        .checkGongYingShangPop(et_name, this@PurchaseActivity) {
                            isCheck = false
                            searchContent = it.name
                            et_name.setText(it.name)
                            et_name.setSelection(it.name.length)
                            et_contacts.text = it.contacts
                            et_phone.text = it.contactsPhone
                            supplierId = it.id
                        }
                } else
                    isCheck = true
        }
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_delete -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get().showConfirmPop(this, "确定删除次配件？") {
                        list.removeAt(position)
                        adapter.setNewData(list)
                        count()
                    }
                }
            }
        }
        adapter.setOnChangeListener {
            count()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(et_name.text.toString()) -> ToastUtils.showShort("请添加供应商")
                    list.isEmpty() -> ToastUtils.showShort("请添加配件")
                    else ->
                        if (TextUtils.isEmpty(intent.getStringExtra("id")))
                            save()
                        else
                            update()
                }
            }

            R.id.tv_settle -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(et_name.text.toString()) -> ToastUtils.showShort("请添加供应商")
                    list.isEmpty() -> ToastUtils.showShort("请添加配件")
                    else -> finished(1)
                }
            }

            R.id.tv_add_part -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (TextUtils.isEmpty(et_name.text.toString())) {
                    ToastUtils.showShort("请先添加供应商")
                    return
                }
                startActivity(Intent(mContext, SelectPartActivity::class.java))
            }

            R.id.tv_delete -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (TextUtils.isEmpty(intent.getStringExtra("id")))
                    finish()
                else
                    PopupWindowUtils.get().showConfirmPop(this, "确认作废此单据？") {
                        finished(2)
                    }
            }
        }
    }

    private fun count() {
        var total = BigDecimal(0.0)
        for (row: PartListRow in list) {
            total = total.add(BigDecimal(row.num!!).multiply(BigDecimal(row.purchasePrice)))
                .setScale(2, BigDecimal.ROUND_HALF_DOWN)
        }
        tv_total.text = "${total}元"
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onMessageEvent(eb: EBSelectPart) {
        for (row: PartListRow in eb.data) {
            if (!list.contains(row))
                list.add(row)
        }
        adapter.setNewData(list)
        count()
    }

    private fun save() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["type"] = "1"// 1-进货 2-退回
        params["status"] = 0//0-草稿 1-结算 2-作废
        params["supplierId"] = supplierId
        params["supplierName"] = et_name.text.toString()
        params["contacts"] = et_contacts.text.toString()
        params["contactsPhone"] = et_phone.text.toString()
        val detailList = ArrayList<HashMap<String, Any>>()
        var quantity = BigDecimal(0.0)
        var amount = BigDecimal(0.0)
        for (row: PartListRow in list) {
            quantity = quantity.add(BigDecimal(row.num!!))
            amount = amount.add(BigDecimal(row.num!!).multiply(BigDecimal(row.purchasePrice)))
            val hashMap = HashMap<String, Any>()
            hashMap["partId"] = row.id!!
            hashMap["quantity"] = row.num!!
            hashMap["cost"] = row.purchasePrice!!
            hashMap["amount"] =
                BigDecimal(row.num!!).multiply(BigDecimal(row.purchasePrice)).toString()
            detailList.add(hashMap)
        }
        params["detailList"] = detailList
        params["quantity"] = quantity.toString()
        params["amount"] = amount.toString()
        RetrofitUtils.get()
            .postJson(
                PurchaseOrderUrl, params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        LogUtils.d("tag", s)
                        val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                        ToastUtils.showShort(t.msg)
                        if (t.code == 200)
                            finish()
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }

    private fun update() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["id"] = intent.getStringExtra("id")
        params["type"] = "1"// 1-进货 2-退回
        params["status"] = 1//0-草稿 1-结算 2-作废
        params["supplierId"] = supplierId
        params["supplierName"] = et_name.text.toString()
        params["contacts"] = et_contacts.text.toString()
        params["contactsPhone"] = et_phone.text.toString()
        val detailList = ArrayList<HashMap<String, Any>>()
        var quantity = BigDecimal(0.0)
        var amount = BigDecimal(0.0)
        for (row: PartListRow in list) {
            quantity = quantity.add(BigDecimal(row.num!!))
            amount = amount.add(BigDecimal(row.num!!).multiply(BigDecimal(row.purchasePrice)))
            val hashMap = HashMap<String, Any>()
            hashMap["partId"] = row.id!!
            hashMap["quantity"] = row.num!!
            hashMap["cost"] = row.purchasePrice!!
            hashMap["amount"] =
                BigDecimal(row.num!!).multiply(BigDecimal(row.purchasePrice)).toString()
            detailList.add(hashMap)
        }
        params["detailList"] = detailList
        params["quantity"] = quantity.toString()
        params["amount"] = amount.toString()
        RetrofitUtils.get()
            .putJson(
                PurchaseOrderUrl, params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        LogUtils.d("tag", s)
                        val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                        ToastUtils.showShort(t.msg)
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }

    private fun finished(status: Int) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        if (!TextUtils.isEmpty(intent.getStringExtra("id")))
            params["id"] = intent.getStringExtra("id")
        params["type"] = "1"// 1-进货 2-退回
        params["status"] = status//0-草稿 1-结算 2-作废
        params["supplierId"] = supplierId
        params["supplierName"] = et_name.text.toString()
        params["contacts"] = et_contacts.text.toString()
        params["contactsPhone"] = et_phone.text.toString()
        val detailList = ArrayList<HashMap<String, Any>>()
        var quantity = BigDecimal(0.0)
        var amount = BigDecimal(0.0)
        for (row: PartListRow in list) {
            quantity = quantity.add(BigDecimal(row.num!!))
            amount = amount.add(BigDecimal(row.num!!).multiply(BigDecimal(row.purchasePrice)))
            val hashMap = HashMap<String, Any>()
            hashMap["partId"] = row.id!!
            hashMap["quantity"] = row.num!!
            hashMap["cost"] = row.purchasePrice!!
            hashMap["amount"] =
                BigDecimal(row.num!!).multiply(BigDecimal(row.purchasePrice)).toString()
            detailList.add(hashMap)
        }
        params["detailList"] = detailList
        params["quantity"] = quantity.toString()
        params["amount"] = amount.toString()
        RetrofitUtils.get()
            .putJson(
                PurchaseFinishUrl, params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        LogUtils.d("tag", s)
                        val t = GsonUtils.fromJson(s, PurchaseFinishedResponse::class.java)
                        ToastUtils.showShort(t.msg)
                        when (status) {
                            1 -> {
                                if (t.data.moneyOwe.toDouble() != 0.0) {
                                    val intent =
                                        Intent(mContext, PurchaseSettleActivity::class.java)
                                    intent.putExtra("money", amount.toString())
                                    intent.putExtra("supplierId", supplierId)
                                    intent.putExtra("orderId", t.data.id)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    PopupWindowUtils.get().showOnlyConfirmPop(
                                        this@PurchaseActivity,
                                        "提示",
                                        "当前供应商剩余余额自动冲减往来账。"
                                    ) {
                                        finish()
                                    }
                                }
                            }

                            2 -> finish()
                        }
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }

    private fun getInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getPurchaseInfo(intent.getStringExtra("id").toInt())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, PurchaseResponse::class.java)
                    when (t.code) {
                        200 -> {
                            et_name.setText(t.data.supplierName)
                            et_contacts.text = t.data.contacts
                            et_phone.text = t.data.contactsPhone
                            tv_total.text = t.data.amount
                            supplierId = t.data.supplierId
                            for (detail: PurchaseDetail in t.data.detailList) {
                                val row = PartListRow()
                                row.num = detail.quantity.toDouble()
                                row.purchasePrice = detail.cost
                                row.unit = detail.part.unit
                                row.name = detail.part.name
                                row.model = detail.part.model
                                row.brand = detail.part.brand
                                row.code = detail.part.code
                                row.id = detail.partId
                                row.oe = detail.part.oe
                                row.remark = detail.part.remark
                                row.status = detail.part.status
                                list.add(row)
                            }
                            adapter.setNewData(list)
                            count()
                        }

                        401 -> {
                            PopupWindowUtils.get()
                                .showLoginOutTimePop(this@PurchaseActivity)
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
}