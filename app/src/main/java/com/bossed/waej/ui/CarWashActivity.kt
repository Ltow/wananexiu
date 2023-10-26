package com.bossed.waej.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.ItemAdapter
import com.bossed.waej.adapter.CommonItemAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.ItemBean
import com.bossed.waej.javebean.ItemRow
import com.bossed.waej.javebean.SelectItemBean
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_car_wash.*

class CarWashActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var commonItemAdapter: CommonItemAdapter
    private val commons = ArrayList<ItemBean>()
    private var popupView: View? = null
    private lateinit var itemAdapter: ItemAdapter
    private val items = ArrayList<ItemBean>()

    override fun getLayoutId(): Int {
        return R.layout.activity_car_wash
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_new_wash)
        rv_common_item.layoutManager = LinearLayoutManager(this)
        commonItemAdapter = CommonItemAdapter(commons)
        commonItemAdapter.bindToRecyclerView(rv_common_item)
        rv_car_wash.layoutManager = LinearLayoutManager(this)
        itemAdapter = ItemAdapter(States.SERVICE, items)
        itemAdapter.bindToRecyclerView(rv_car_wash)
        getItemList()
    }

    override fun initListener() {
        tb_new_wash.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        et_discount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s))
                    return
                countTotal()
            }
        })
        commonItemAdapter.setOnItemClickListener { adapter, view, position ->
            commons[position].isSelect = !commons[position].isSelect
            adapter.setNewData(commons)
            countTotal()
        }
        itemAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view?.id) {
                R.id.iv_delete_item -> {
                    PopupWindowUtils.get()
                        .showConfirmPop(this, "是否确认删除此项目?") {
                            items.removeAt(position)
                            adapter.setNewData(items)
                        }
                }
                R.id.iv_edit_item -> {

                }
            }
            countTotal()
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                finish()
            }
            R.id.tv_finished -> {//收款出厂
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get()
                    .showConfirmPop(this, "是否确认收款出厂?") {
                        finished()
                    }
//                object : OnConfirmListener {
//                        override fun onConfirm() {
//
//                        }
//                    })
            }
            R.id.tv_ls_btn -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                tv_car_no.text = "临时客户999"
            }
            R.id.tv_total_item -> {//查看已添加项目
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (commons.isNotEmpty() || items.isNotEmpty())
                    showSelectedPop()
            }
            R.id.tv_add_part -> {//添加配件
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get()
                    .showAddPartPop(
                        this,
                        tv_add_part,
                        "",
                        false,
                        ItemBean()
                    ) { name: String,
                        type: String,
                        specs: String,
                        remark: String,
                        price: String,
                        num: String,
                        money: String,
                        cateId: Int,
                        cost: String ->
                        val itemBean = ItemBean()
                        itemBean.itemName = name
                        itemBean.cateName = type
                        if (cateId != -1)
                            itemBean.cateId = cateId
                        itemBean.attrName = specs
                        itemBean.remark = remark
                        itemBean.type = 2
                        itemBean.costPrice = cost.toDouble()
                        items.add(itemBean)
                        itemAdapter.setNewData(items)
                        countTotal()
                    }
            }
//            R.id.tv_add_item -> {//添加项目
//                PopupWindowUtils.get()
//                    .showAddItemPop(this, object : PopupWindowUtils.OnAddItemConfirmListener {
//                        override fun onConfirm(
//                            name: String,
//                            type: String,
//                            price: String,
//                            remark: String,
//                            cateId: Int
//                        ) {
//                            val itemBean = ItemBean()
//                            itemBean.itemName = name
//                            itemBean.cateName = type
//                            if (cateId != -1)
//                                itemBean.cateId = cateId
//                            itemBean.unitPrice = price.toFloat()
//                            itemBean.remark = remark
//                            itemBean.itemMoney = price.toFloat()
//                            itemBean.num = 1f
//                            itemBean.type = 1
//                            items.add(itemBean)
//                            itemAdapter.setNewData(items)
//                            countTotal()
//                        }
//                    })
//            }
        }
    }

    private fun countTotal() {
        var totalPrice = 0.0
        for (row: ItemBean in commons) {
            totalPrice += row.unitPrice
        }
        for (row: ItemBean in items) {
            totalPrice += row.itemMoney
        }
        tv_total.text = String.format("%.2f", totalPrice)
        tv_receivable.text =
            String.format(
                "%.2f",
                totalPrice - (if (TextUtils.isEmpty(et_discount.text.toString()))
                    0.0
                else
                    et_discount.text.toString().toDouble())
            )
        tv_price_total.text =
            String.format(
                "%.2f",
                totalPrice - (if (TextUtils.isEmpty(et_discount.text.toString()))
                    0.0
                else
                    et_discount.text.toString().toDouble())
            )
        tv_total_item.text = "共${commons.size + items.size}项"
    }

    private fun showSelectedPop() {
        val selecteds = ArrayList<ItemBean>()
        for (item: ItemBean in commons) {
            if (item.isSelect)
                selecteds.add(item)
        }
        selecteds.addAll(items)
//        PopupWindowUtils.get().showSelectedItemPop(
//            selecteds,
//            this,
//            tv_total_item.text.toString(),
//            tv_total.text.toString(),
//            et_discount.text.toString(),
//            tv_price_total.text.toString()
//        )
    }

    private fun finished() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["cardNo"] = tv_car_no.text.toString()
        params["customerPhone"] = et_kh_phone.text.toString()
        params["remark"] = et_remark.text.toString()
        params["orderType"] = "3"
        params["orderStatus"] = "1"
        params["orderMoney"] = tv_total.text.toString()
        params["discountAmount"] = et_discount.text.toString()
        params["realMoney"] = tv_receivable.text.toString()
        val itemList = ArrayList<ItemBean>()
        for (item: ItemBean in commons) {
            if (item.isSelect)
                itemList.add(item)
        }
        itemList.addAll(items)
        params["itemList"] = itemList
        RetrofitUtils.get()
            .postJson(
                UrlConstants.NewOrderUrl,
                params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        val response = GsonUtils.fromJson(s, BaseResponse::class.java)
                        ToastUtils.showShort(response.msg)
                        finish()
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }

    /**
     * 常用项目列表
     */
    private fun getItemList() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getCommonItemList(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<SelectItemBean>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: SelectItemBean) {
                    when (t.code) {
                        200 -> {
                            for (row: ItemRow in t.rows) {
                                val itemBean = ItemBean()
                                itemBean.itemName = row.name
                                itemBean.isSelect = false
                                commons.add(itemBean)
                            }
                            commonItemAdapter.setNewData(commons)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@CarWashActivity)
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