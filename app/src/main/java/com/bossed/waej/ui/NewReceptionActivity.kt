package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.blankj.utilcode.util.KeyboardUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.ItemAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.http.UrlConstants.NewJieCheUrl
import com.bossed.waej.http.UrlConstants.UpdateJieCheUrl
import com.bossed.waej.javebean.*
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_new_jieche.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import wang.relish.widget.vehicleedittext.VehicleKeyboardHelper
import com.bossed.waej.eventbus.*
import com.bossed.waej.http.UrlConstants.NewOrderUrl
import java.math.BigDecimal

class NewReceptionActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var itemAdapter: ItemAdapter
    private val items = ArrayList<ItemBean>()
    private var orderId = -1
    private var carId = ""
    private var brandName = ""//品牌
    private var brandLogo = ""//车型logo
    private var isCarNo = true//检索车牌
    private var isCustomer = true//检索客户
    private var isPhone = true//检索手机号
    private var isVehicle = true//检索送车人
    private val itemDelIds = ArrayList<Int>()
    private var carCustomerId = -1//送车人id
    private var customerId = -1//客户id
    private var mileage = ""//行驶里程
    private var vinCode = ""//vin码
    private val recommendList = ArrayList<RecommendDs1>()//精友数据保养项目

    override fun getLayoutId(): Int {
        return R.layout.activity_new_jieche
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_new_jieche)
        rv_jieche_order.layoutManager = LinearLayoutManager(this)
        itemAdapter = ItemAdapter(States.RECEIVE, items)
        itemAdapter.bindToRecyclerView(rv_jieche_order)
        itemAdapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        when (intent.getStringExtra("orderStatus")) {
            "open" -> {
                tb_new_jieche.rightTitle = ""
                getOrderMsg()
            }

            "new" -> {
                tv_admit_date.text = TimeUtils.date2String(TimeUtils.getNowDate())
                et_admit_name.text = SPUtils.getInstance().getString("nickName")
            }
        }
        VehicleKeyboardHelper.bind(tv_car_no)//车牌输入键盘
    }

//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        if (ev.action == MotionEvent.ACTION_DOWN) {
//            if (isShouldHideKeyboard(tv_car_no, ev)) {
//                tv_car_no.clearFocus()
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }

    override fun initListener() {
        tb_new_jieche.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                if (intent.getStringExtra("orderStatus") == "open") return
                startActivity(Intent(this@NewReceptionActivity, ReceptionDraftActivity::class.java))
            }
        })
        et_car_vin.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (TextUtils.isEmpty(s)) tv_search.visibility = View.GONE
                else tv_search.visibility = View.VISIBLE
            }
        })
        tv_car_no.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                tv_search_his.visibility = if (TextUtils.isEmpty(s)) View.GONE else View.VISIBLE
                if (tv_car_no.isFocused) if (isCarNo) PopupWindowUtils.get().checkKeHuList(
                    tv_car_no, this@NewReceptionActivity
                ) { data ->
                    isCarNo = false
                    et_kh_name.setText(data.customerName)
                    tv_car_no.setText(data.cardNo)
                    et_kh_phone.setText(data.customerPhone)
                    et_car_type.setText(data.carName)
                    et_car_vin.setText(data.vnCode)
                    et_vmt.setText(data.mileage)
                    carId = data.jycarId
                    brandLogo = data.brandLogo
                    brandName = data.brandName
                    customerId = data.customerId
                    tv_kh_detail.visibility = View.VISIBLE
                    tv_car_no.clearFocus()
                }
                else isCarNo = true
            }

        })
        ev_yh.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (TextUtils.isEmpty(s) || TextUtils.isEmpty(tv_hj.text.toString())) return
                tv_ys.text = String.format(
                    "%.2f", tv_hj.text.toString().toDouble() - ev_yh.text.toString().toDouble()
                )
                tv_price.text = "￥${tv_ys.text}"
            }
        })
        itemAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_show_4s_price -> {
                    clearFocus()
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get()
                        .getBalance(this, object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                getInformation(carId,
                                    items[position].jyitemId,
                                    rl_content,
                                    object : OnShow4SPriceListener {
                                        override fun onBack(
                                            price: String, fee: String, oe: String, num: String
                                        ) {
                                            items[position].isShow4s = true
                                            items[position].shop4sPrice = price
                                            items[position].shop4sServiceFee = fee
                                            items[position].oem = oe
                                            items[position].shop4sNum = num
                                            itemAdapter.setNewData(items)
                                        }
                                    })
                            }

                            override fun onCancel() {
                            }
                        })
                }

                R.id.iv_show_oe -> {
                    clearFocus()
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get()
                        .getBalance(this, object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                getOEData(
                                    et_car_vin.text.toString(), items[position].itemName
                                ) { oe, price ->
                                    items[position].isShowOE = true
                                    items[position].oem = oe
                                    items[position].shop4sPrice = price
                                    items[position].shop4sServiceFee = ""
                                    itemAdapter.setNewData(items)
                                }
                            }

                            override fun onCancel() {
                            }
                        })
                }

                R.id.iv_delete_item -> {
                    clearFocus()
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get()
                        .showConfirmPop(this, "是否确认删除此项目?") {
                            if (items[position].id != null) itemDelIds.add(items[position].id)
                            items.removeAt(position)
                            adapter.setNewData(items)
                            countTotal()
                        }
                }

                R.id.iv_edit_item -> {
                    clearFocus()
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get().showAddPartPop(
                        this, rl_content, tv_car_no.text.toString(), true, items[position]
                    ) { name: String, type: String, specs: String, remark: String, price: String, num: String, money: String, cateId: Int, cost: String ->
                        items[position].itemName = name
                        items[position].cateName = type
                        if (cateId != -1) items[position].cateId = cateId
                        items[position].attrName = specs
                        items[position].remark = remark
                        items[position].unitPrice = price.toDouble()
                        items[position].num = num.toDouble()
                        items[position].itemMoney =
                            BigDecimal(money).add(BigDecimal(cost)).toDouble()
                        items[position].serviceFee = cost.toDouble()
                        itemAdapter.setNewData(items)
                        countTotal()
                    }
                }

                R.id.iv_item_tag -> {
                    clearFocus()
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get().showDoubtPop(
                        this,
                        adapter.getViewByPosition(position, R.id.iv_item_tag)!!,
                        when (items[position].type) {
                            3 -> "常规保养"
                            4 -> "深度保养"
                            else -> ""
                        }
                    )
                }
            }
        }
        et_kh_name.addTextChangedListener(object : TextChangedListener {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_kh_detail.visibility = View.GONE
            }

            override fun afterTextChange(s: String) {
                if (et_kh_name.isFocused) if (isCustomer) PopupWindowUtils.get().checkKeHuList(
                    et_kh_name, this@NewReceptionActivity
                ) { data ->
                    isCustomer = false
                    et_kh_name.setText(data.customerName)
                    et_kh_name.setSelection(data.customerName.length)
                    tv_car_no.setText(data.cardNo)
                    et_kh_phone.setText(data.customerPhone)
                    et_car_type.setText(data.carName)
                    et_car_vin.setText(data.vnCode)
                    carId = data.jycarId
                    brandLogo = data.brandLogo
                    brandName = data.brandName
                    customerId = data.customerId
                    tv_kh_detail.visibility = View.VISIBLE
                }
                else isCustomer = true
            }
        })
        et_sc_name.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (isVehicle) PopupWindowUtils.get().checkKeHuList(
                    et_sc_name, this@NewReceptionActivity
                ) { data ->
                    isVehicle = false
                    et_sc_name.setText(data.customerName)
                    et_sc_phone.setText(data.customerPhone)
                    carCustomerId = data.customerId
                }
                else isVehicle = true
            }
        })
        et_kh_phone.addTextChangedListener(object : TextChangedListener {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_kh_detail.visibility = View.GONE
            }

            override fun afterTextChange(s: String) {
                if (et_kh_phone.isFocused) if (isPhone) PopupWindowUtils.get().checkKeHuList(
                    et_kh_phone, this@NewReceptionActivity
                ) { data ->
                    isPhone = false
                    et_kh_name.setText(data.customerName)
                    tv_car_no.setText(data.cardNo)
                    et_kh_phone.setText(data.customerPhone)
                    et_kh_phone.setSelection(data.customerPhone.length)
                    et_car_type.setText(data.carName)
                    et_car_vin.setText(data.vnCode)
                    carId = data.jycarId
                    brandLogo = data.brandLogo
                    brandName = data.brandName
                    customerId = data.customerId
                    tv_kh_detail.visibility = View.VISIBLE
                }
                else isPhone = true
            }
        })
    }

    override fun onRepeatClick(v: View?) {
        when (v!!.id) {
            R.id.tv_add_item -> {//添加项目
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                KeyboardUtils.hideSoftInput(this)
                PopupWindowUtils.get().showAddPartPop(
                    this, rl_content, tv_car_no.text.toString(), false, ItemBean()
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
                    itemBean.jyitemId = ""
                    itemBean.cateName = type
                    if (cateId != -1) itemBean.cateId = cateId
                    itemBean.attrName = specs
                    itemBean.remark = remark
                    itemBean.type = 0
                    if (TextUtils.isEmpty(price) && TextUtils.isEmpty(cost) && TextUtils.isEmpty(num))
                        getItemHisPrice(name, itemBean)
                    else {
                        itemBean.unitPrice = price.toDouble()
                        itemBean.serviceFee = cost.toDouble()
                        itemBean.num = num.toDouble()
                        itemBean.itemMoney = BigDecimal(money).add(BigDecimal(cost)).toDouble()
                        items.add(itemBean)
                        itemAdapter.setNewData(items)
                        countTotal()
                    }
                }
            }

            R.id.tv_total_item -> {//已选项目
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (items.isEmpty()) return
                PopupWindowUtils.get().showSelectedItemPop(
                    items, this, tv_total_item.text.toString(), tv_price.text.toString()
                )
            }

            R.id.tv_search -> {
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(et_car_vin.text.toString()) -> ToastUtils.showShort("请输入vin码")
                    else -> PopupWindowUtils.get()
                        .getBalance(this, object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                getVinData(et_car_vin.text.toString(),
                                    object : OnGetVinBackListener {
                                        override fun onBack(model: String, id: String) {
                                            et_car_type.setText(model)
                                            carId = id
                                        }
                                    })
                            }

                            override fun onCancel() {

                            }
                        })
                }
            }

            R.id.tv_maintain -> {
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(carId) -> ToastUtils.showShort("请先通过vin码解析车型")
                    TextUtils.isEmpty(et_vmt.text.toString()) -> ToastUtils.showShort("请输入行驶里程")
                    mileage == et_vmt.text.toString() && vinCode == et_car_vin.text.toString() -> {
                        val intent = Intent(
                            this@NewReceptionActivity, MaintainProposalActivity::class.java
                        )
                        intent.putExtra("mileage", et_vmt.text.toString())
                        intent.putExtra("mileage", et_vmt.text.toString())
                        intent.putExtra("vinCode", et_car_vin.text.toString())
                        intent.putParcelableArrayListExtra("list", recommendList)
                        startActivity(intent)
                    }

                    else -> {
                        PopupWindowUtils.get()
                            .getBalance(this, object : PopupWindowUtils.OnConfirmAndCancelListener {
                                override fun onConfirm() {
                                    getJYItemList()
                                }

                                override fun onCancel() {
                                }
                            })
                    }
                }
            }

            R.id.iv_scan_che_no -> {
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, OCRScanActivity::class.java)
                intent.putExtra("ocrType", 0)
                startActivity(intent)
            }

            R.id.iv_scan_vin -> {
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, OCRScanActivity::class.java)
                intent.putExtra("ocrType", 2)
                startActivity(intent)
            }

            R.id.tv_cancel -> {//作废
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when (intent.getStringExtra("orderStatus")) {
                    "open" -> {
                        PopupWindowUtils.get()
                            .showConfirmPop(this, "是否确认作废此单据?") {
                                deleteOrder(orderId)
                            }
                    }

                    "new" -> {
                        finish()
                    }
                }
            }

            R.id.tv_enter -> {//进厂
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (TextUtils.isEmpty(tv_car_no.text.toString())) ToastUtils.showShort("车牌号不能为空")
                else PopupWindowUtils.get()
                    .showConfirmPop(this@NewReceptionActivity, "是否确定进厂施工?") {
                        PopupWindowUtils.get()
                            .showSelectPersonPop(this@NewReceptionActivity, "选择总负责人") {
                                val dis = ArrayList<ItemDispatchBean>()
                                for (row: PersonRow in it) {
                                    dis.add(
                                        ItemDispatchBean(
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            0.0,
                                            0.0,
                                            0.0,
                                            0.0,
                                            0,
                                            "",
                                            "",
                                            "",
                                            row.id,
                                            row.name,
                                            row.phone,
                                            "",
                                            "",
                                            "",
                                            "1",
                                            ""
                                        )
                                    )
                                }
                                newJieOrder(2, dis)
                            }
                    }
            }

            R.id.tv_finished -> {//出厂
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get()
                    .showConfirmPop(this, "是否确定付款出厂?") {
                        finishedOrder()
                    }
            }

            R.id.tv_search_his -> {//查历史
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (tv_car_no.text.toString().length < 7) {
                    ToastUtils.showShort("请输入正确车牌号")
                    return
                }
                val intent = Intent(this, SearchHistoryActivity::class.java)
                intent.putExtra("carNo", tv_car_no.text.toString())
                startActivity(intent)
            }

            R.id.iv_select_model -> {//选车型
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, BrandActivity::class.java)
                intent.putExtra("selType", "all")
                startActivity(intent)
            }

            R.id.tv_kh_detail -> {
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, NewCustomerActivity::class.java)
                intent.putExtra("customerId", customerId)
                intent.putExtra("type", "open")
                startActivity(intent)
            }
        }
    }

    private fun clearFocus() {
        et_kh_name.clearFocus()
        et_car_vin.clearFocus()
        tv_car_no.clearFocus()
        et_kh_phone.clearFocus()
        et_vmt.clearFocus()
        et_car_type.clearFocus()
    }

    @Subscribe
    fun onSelMaintainCallBack(eb: EBSelMaintain) {
        LoadingUtils.showLoading(this, "加载中...")
        LogUtils.d("tag", eb.list.size)
        val array = ArrayList<String>()//所有选中的名称
        for (item: RecommendDs1 in eb.list) {
            array.add(item.保养项目名称)
        }
        Api.getInstance().getApiService()
            .getHisItemList(tv_car_no.text.toString(), array)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    val t = GsonUtils.fromJson(s, ItemsHisPriceResponse::class.java)
                    when (t.code) {
                        200 -> {
                            val selAllItems = ArrayList<ItemBean>()//每一次推荐保养添加过来所有的项目
                            if (t.data != null)
                                for (item: ItemBean in t.data) {
                                    if (array.contains(item.itemName))
                                        array.remove(item.itemName)
                                }
                            for (ds1: RecommendDs1 in eb.list) {
                                if (array.contains(ds1.保养项目名称)) {
                                    val itemBean = ItemBean()
                                    itemBean.itemDispatchList = ArrayList()
                                    itemBean.type = if (ds1.获取 == "获取") 3 else 4
                                    itemBean.itemName = ds1.保养项目名称
                                    itemBean.jyitemId = ds1.保养项目ID
                                    itemBean.cateName = ds1.保养项目类型名称
                                    itemBean.attrName = ""
                                    itemBean.oem = ""
                                    itemBean.remark = ""
                                    itemBean.itemMoney = 0.0
                                    itemBean.unitPrice = 0.0
                                    itemBean.serviceFee = 0.0
                                    itemBean.num = 1.0
                                    selAllItems.add(itemBean)
                                }
                            }
                            if (t.data != null)
                                selAllItems.addAll(t.data)
                            //重新排序
                            val jyItems = ArrayList<ItemBean>()
                            val otherItems = ArrayList<ItemBean>()
                            for (item: ItemBean in selAllItems) {
                                if (item.type == 3)
                                    jyItems.add(item)
                                if (item.type == 4)
                                    otherItems.add(item)
                            }
                            items.addAll(jyItems)
                            items.addAll(otherItems)
                            itemAdapter.setNewData(items)
                            countTotal()
                            LoadingUtils.dismissLoading()
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@NewReceptionActivity)
                            LoadingUtils.dismissLoading()
                        }

                        else -> {
                            ToastUtils.showShort(t.msg)
                            LoadingUtils.dismissLoading()
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    @Subscribe
    fun onOcrScanCallBack(eb: EBOCRCallBack) {
        when (eb.ocrType) {
            0 -> {
                tv_car_no.setText(eb.chepai)
            }

            2 -> {
                et_car_vin.setText(eb.vin)
            }
        }
    }

    @Subscribe
    fun onSelCarModelCallBack(eb: EBSelCarModel) {
        et_car_type.setText(eb.model)
        carId = eb.vehicleId
        brandName = eb.brand
        brandLogo = eb.logo
    }

    @Subscribe
    fun onFinished(eb: EBOpenFinish) {
        if (eb.isFinish) finish()
    }

    @Subscribe
    fun onMessageEvent(eb: EBNewCustomer) {
        if (eb.isNew) PopupWindowUtils.get().showSelectPersonPop(
            this@NewReceptionActivity,
            "选择总负责人"
        ) {
            val dis = ArrayList<ItemDispatchBean>()
            for (row: PersonRow in it) {
                dis.add(
                    ItemDispatchBean(
                        "",
                        "",
                        "",
                        "",
                        "",
                        0.0,
                        0.0,
                        0.0,
                        0.0,
                        0,
                        "",
                        "",
                        "",
                        row.id,
                        row.name,
                        row.phone,
                        "",
                        "",
                        "",
                        "1",
                        ""
                    )
                )
            }
            newJieOrder(2, dis)
        }
    }

    private fun finishedOrder() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["cardNo"] = tv_car_no.text.toString()
        params["customerName"] = et_kh_name.text.toString()
        params["customerPhone"] = et_kh_phone.text.toString()
        params["carName"] = et_car_type.text.toString()
        params["vnCode"] = et_car_vin.text.toString()
        params["mileage"] = et_vmt.text.toString()
        params["orderType"] = "2"
        params["orderMoney"] = tv_price.text.toString()
        params["discountAmount"] = ev_yh.text.toString()
        params["realMoney"] = tv_ys.text.toString()
        params["receiveBy"] = et_admit_name.text.toString()
        params["receiveTime"] = tv_admit_date.text.toString()
        params["remark"] = et_remark.text.toString()
        params["carCustomerName"] = et_sc_name.text.toString()
        params["carCustomerPhone"] = et_sc_phone.text.toString()
        if (!TextUtils.isEmpty(brandLogo)) params["brandLogo"] = brandLogo
        if (!TextUtils.isEmpty(brandName)) params["brandName"] = brandName
        if (!TextUtils.isEmpty(carId)) params["jycarId"] = carId
        if (orderId != -1) params["id"] = orderId
        params["orderStatus"] = 1
        params["itemList"] = items
        params["itemDelIds"] = itemDelIds
        RetrofitUtils.get().postJson(NewOrderUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    ToastUtils.showShort("已完成")
                    finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun deleteOrder(id: Int) {
        LoadingUtils.showLoading(this, "加载中")
        Api.getInstance().getApiService().deleteOrder(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<BaseResponse>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: BaseResponse) {
                    when (t.code) {
                        200 -> {
                            ToastUtils.showShort("作废成功")
                            finish()
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@NewReceptionActivity)
                        }

                        else -> {
                            ToastUtils.showShort(t.msg)
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    private fun updateJieOrder() {
        if (tv_car_no.text.toString().length < 7) {
            ToastUtils.showShort("请输入正确的车牌号")
            return
        }
        LoadingUtils.showLoading(this, "加载中")
        val params = HashMap<String, Any>()
        params["cardNo"] = tv_car_no.text.toString()
        params["customerName"] = et_kh_name.text.toString()
        params["customerPhone"] = et_kh_phone.text.toString()
        params["carName"] = et_car_type.text.toString()
        params["vnCode"] = et_car_vin.text.toString()
        params["mileage"] = et_vmt.text.toString()
        params["orderType"] = "1"
        params["orderMoney"] = tv_price.text.toString()
        params["discountAmount"] = ev_yh.text.toString()
        params["realMoney"] = tv_ys.text.toString()
        params["receiveBy"] = et_admit_name.text.toString()
        params["receiveTime"] = tv_admit_date.text.toString()
        params["remark"] = et_remark.text.toString()
        params["carCustomerName"] = et_sc_name.text.toString()
        params["carCustomerPhone"] = et_sc_phone.text.toString()
        if (carCustomerId != -1) params["carCustomerId"] = carCustomerId
        if (!TextUtils.isEmpty(brandLogo)) params["brandLogo"] = brandLogo
        if (!TextUtils.isEmpty(brandName)) params["brandName"] = brandName
        if (!TextUtils.isEmpty(carId)) params["jycarId"] = carId
        params["id"] = orderId
        params["itemList"] = items
        params["itemDelIds"] = itemDelIds
        RetrofitUtils.get()
            .putJson(UpdateJieCheUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    ToastUtils.showShort("保存成功")
                    finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun newJieOrder(orderType: Int, dispatchList: MutableList<ItemDispatchBean>) {
        if (tv_car_no.text.toString().length < 7) {
            ToastUtils.showShort("请输入正确的车牌号")
            return
        }
        LoadingUtils.showLoading(this, "加载中")
        val params = HashMap<String, Any>()
        params["cardNo"] = tv_car_no.text.toString()
        params["customerName"] = et_kh_name.text.toString()
        params["customerPhone"] = et_kh_phone.text.toString()
        if (customerId != -1) params["customerId"] = customerId
        params["carName"] = et_car_type.text.toString()
        params["vnCode"] = et_car_vin.text.toString()
        params["mileage"] = et_vmt.text.toString()
        params["orderType"] = orderType
        params["orderMoney"] = tv_price.text.toString()
        params["discountAmount"] = ev_yh.text.toString()
        params["realMoney"] = tv_ys.text.toString()
        params["receiveBy"] = et_admit_name.text.toString()
        params["receiveTime"] = tv_admit_date.text.toString()
        params["remark"] = et_remark.text.toString()
        params["carCustomerName"] = et_sc_name.text.toString()
        params["carCustomerPhone"] = et_sc_phone.text.toString()
        if (carCustomerId != -1) params["carCustomerId"] = carCustomerId
        if (!TextUtils.isEmpty(brandLogo)) params["brandLogo"] = brandLogo
        if (!TextUtils.isEmpty(brandName)) params["brandName"] = brandName
        if (!TextUtils.isEmpty(carId)) params["jycarId"] = carId
        if (intent.getStringExtra("orderStatus") == "open") params["id"] =
            intent.getIntExtra("id", 1)
        params["itemDispatchList"] = dispatchList
        params["itemList"] = setItemDispatch(dispatchList)//给所有项目添加默认派工
        params["itemDelIds"] = itemDelIds
        RetrofitUtils.get()
            .postJson(NewJieCheUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, NewReceptionResponse::class.java)
                    when (orderType) {
                        1 -> ToastUtils.showShort("保存成功")
                        2 -> {
                            val intent =
                                Intent(this@NewReceptionActivity, NewOrderActivity::class.java)
                            intent.putExtra("orderType", "open")
                            intent.putExtra("id", t.data.id.toInt())
                            startActivity(intent)
                        }
                    }
                    finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun setItemDispatch(dispatchList: MutableList<ItemDispatchBean>): ArrayList<ItemBean> {
        val paigonglist = ArrayList<ItemDispatchBean>()
        for (i: ItemDispatchBean in dispatchList) {
            val itemBean = ItemDispatchBean()
            itemBean.type = "3"
            itemBean.staffName = i.staffName
            itemBean.itemName = i.itemName
            itemBean.createBy = i.createBy
            itemBean.createTime = i.createTime
            itemBean.id = i.id
            itemBean.itemId = i.itemId
            itemBean.madeFee = i.madeFee
            itemBean.madeFeeRate = i.madeFeeRate
            itemBean.madeMoney = i.madeMoney
            itemBean.madeRate = i.madeRate
            itemBean.orderId = i.orderId
            itemBean.remark = i.remark
            itemBean.searchValue = i.searchValue
            itemBean.shopId = i.shopId
            itemBean.staffId = i.staffId
            itemBean.staffPhone = i.staffPhone
            itemBean.subtotal = i.subtotal
            itemBean.tenantId = i.tenantId
            itemBean.updateBy = i.updateBy
            itemBean.updateTime = i.updateTime
            paigonglist.add(itemBean)
        }
        val itemDispatch = ArrayList<ItemDispatchBean>()
        itemDispatch.addAll(paigonglist)
        for (item: ItemBean in items) {
            item.itemDispatchList = itemDispatch
        }
        return items
    }

    private fun getOrderMsg() {
        LoadingUtils.showLoading(this, "加载中")
        Api.getInstance().getApiService().getJieCheMsg(intent.getIntExtra("id", -1))
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<JieCheResponse>() {
                override fun onSubscribe(d: Disposable) {
                    LoadingUtils.dismissLoading()
                }

                override fun onNext(t: JieCheResponse) {
                    when (t.code) {
                        200 -> {
                            orderId = t.data.id
                            brandLogo = t.data.brandLogo
                            brandName = t.data.brandName
                            customerId = t.data.customerId
                            et_kh_name.setText(t.data.customerName)
                            et_kh_phone.setText(t.data.customerPhone)
                            tv_car_no.setText(t.data.cardNo)
                            carId = t.data.jycarId
                            et_car_type.setText(t.data.carName)
                            et_car_vin.setText(t.data.vnCode)
                            et_vmt.setText(t.data.mileage)
                            et_admit_name.text = t.data.receiveBy
                            tv_admit_date.text = t.data.receiveTime
                            items.addAll(t.data.itemList!!)
                            itemAdapter.setNewData(items)
//                            tv_price.text = t.data.orderMoney.toString()
                            ev_yh.setText(t.data.discountAmount.toString())
                            tv_ys.text = t.data.realMoney.toString()
                            tv_price.text = t.data.realMoney.toString()
                            et_remark.setText(t.data.remark)
                            et_sc_name.setText(t.data.carCustomerName)//送车人
                            et_sc_phone.setText(t.data.carCustomerPhone)//送车电话
                            carCustomerId = t.data.carCustomerId//送车id
                            tv_total_item.text = "共${items.size}项"
                            if (SPUtils.getInstance()
                                    .getString("vinCode") == t.data.vnCode && SPUtils.getInstance()
                                    .getString("mileage") == t.data.mileage
                            ) {
                                vinCode = t.data.vnCode
                                mileage = t.data.mileage
                                val gson = GsonUtils.fromJson(
                                    SPUtils.getInstance().getString("recommendItems"),
                                    RecommendResult::class.java
                                )
                                recommendList.addAll(gson.ds1!!)
                            }
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@NewReceptionActivity)
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

    /**
     * 获取项目最近一次历史价格
     */
    private fun getItemHisPrice(itemName: String, itemBean: ItemBean) {
        val params = HashMap<String, String>()
        params["cardNo"] = tv_car_no.text.toString()
        params["itemName"] = itemName
        RetrofitUtils.get().getJson(UrlConstants.ItemHistoryPriceUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, ItemHisPriceResponse::class.java)
                    itemBean.unitPrice = t.data.unitPrice
                    itemBean.serviceFee = t.data.serviceFee
                    itemBean.num = t.data.num
                    items.add(itemBean)
                    itemAdapter.setNewData(items)
                    countTotal()
                }

                override fun onFailed(e: String) {
                    itemBean.unitPrice = 0.0
                    itemBean.serviceFee = 0.0
                    itemBean.num = 1.0
                    items.add(itemBean)
                    itemAdapter.setNewData(items)
                    countTotal()
                }
            })
    }

    /**
     * 精友推荐保养
     */
    private fun getJYItemList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "1301"
        params["inStr2"] = carId
        params["inStr3"] = "0"
        params["inStr4"] = et_vmt.text.toString()
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, RecommendResponse::class.java)
                    recommendList.clear()
                    if (t.code == "1") {
                        for (ds: RecommendDs1 in t.result!!.ds1!!) {
                            if (ds.更换标识 == "1")
                                ds.isSel = true
                            if (ds.更换标识 == "0")
                                ds.isSel = false
                            ds.isJYItem = "3"
                        }
                        recommendList.addAll(t.result.ds1!!)
                    }
                    getMaintain()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    /**
     * 本地推荐保养
     */
    private fun getMaintain() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["mileage"] = et_vmt.text.toString()
        RetrofitUtils.get().getJson(UrlConstants.MaintainProposalUrl,
            params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, MaintainProposalBean::class.java)
                    mileage = et_vmt.text.toString()
                    vinCode = et_car_vin.text.toString()
                    val allItem = ArrayList<MaintainItem>()
                    allItem.addAll(t.data.itemList)
                    for (item: MaintainItem in allItem) {
                        if (item.cateId == 1) {
                            recommendList.add(
                                RecommendDs1(
                                    "", item.id.toString(), item.name, "", "", "1", "", true, "4"
                                )
                            )
                        } else if (item.cateId == 2) {
                            recommendList.add(
                                RecommendDs1(
                                    "", item.id.toString(), item.name, "", "", "0", "", false, "4"
                                )
                            )
                        }
                    }
                    val intent = Intent(
                        this@NewReceptionActivity, MaintainProposalActivity::class.java
                    )
                    intent.putParcelableArrayListExtra("list", recommendList)
                    intent.putExtra("vinCode", et_car_vin.text.toString())
                    startActivity(intent)
                    SPUtils.getInstance().put("mileage", mileage)
                    SPUtils.getInstance().put("vinCode", vinCode)
                    SPUtils.getInstance().put(
                        "recommendItems",
                        GsonUtils.toJson(RecommendResult(recommendList, ArrayList()))
                    )
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun countTotal() {
        var total = BigDecimal(0.0)
        for (item: ItemBean in items) {
            total = total.add(
                BigDecimal(item.num).multiply(BigDecimal(item.unitPrice)).add(
                    BigDecimal(item.serviceFee)
                )
            ).setScale(2, BigDecimal.ROUND_HALF_DOWN)
        }
        tv_hj.text = String.format(total.toString())
        tv_ys.text = String.format(
            total.subtract(BigDecimal(if (TextUtils.isEmpty(ev_yh.text.toString())) "0" else ev_yh.text.toString()))
                .toString()
        )
        tv_price.text = "￥${tv_ys.text}"
        tv_total_item.text = "共${items.size}项"
    }

    override fun onBackPressed() {
        KeyboardUtils.hideSoftInput(this)
        if (PopupWindowUtils.get().getPopWindow() != null) {
            if (PopupWindowUtils.get().isShowing()) {
                PopupWindowUtils.get().dismiss()
                return
            }
        }
        if (TextUtils.isEmpty(tv_car_no.text.toString()) && TextUtils.isEmpty(et_kh_name.text.toString()) && TextUtils.isEmpty(
                et_kh_phone.text.toString()
            ) && TextUtils.isEmpty(et_car_type.text.toString()) && TextUtils.isEmpty(et_car_vin.text.toString()) && TextUtils.isEmpty(
                et_vmt.text.toString()
            ) && items.size == 0
        ) super.onBackPressed()
        else PopupWindowUtils.get().showConfirmAndCancelPop(this,
            "是否保存草稿单?",
            object : PopupWindowUtils.OnConfirmAndCancelListener {
                override fun onConfirm() {
                    when (intent.getStringExtra("orderStatus")) {
                        "open" -> {
                            updateJieOrder()
                        }

                        "new" -> {
                            newJieOrder(1, ArrayList())
                        }
                    }
                }

                override fun onCancel() {
                    finish()
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        PopupWindowUtils.get().dismiss()
    }

}
