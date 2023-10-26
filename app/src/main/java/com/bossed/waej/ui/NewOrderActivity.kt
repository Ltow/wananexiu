package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.blankj.utilcode.util.KeyboardUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.ItemAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.eventbus.EBNewCustomer
import com.bossed.waej.eventbus.EBOCRCallBack
import com.bossed.waej.eventbus.EBSelCarModel
import com.bossed.waej.eventbus.EBSelMaintain
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.*
import com.bossed.waej.util.*
import com.bossed.waej.util.BluetoothUtils.Companion.bluetoothUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_new_jieche.*
import kotlinx.android.synthetic.main.activity_new_order.*
import kotlinx.android.synthetic.main.activity_new_order.et_car_type
import kotlinx.android.synthetic.main.activity_new_order.et_car_vin
import kotlinx.android.synthetic.main.activity_new_order.et_kh_name
import kotlinx.android.synthetic.main.activity_new_order.et_kh_phone
import kotlinx.android.synthetic.main.activity_new_order.rl_content
import kotlinx.android.synthetic.main.activity_new_order.tv_car_no
import kotlinx.android.synthetic.main.activity_new_order.tv_hj
import kotlinx.android.synthetic.main.activity_new_order.tv_price
import kotlinx.android.synthetic.main.activity_new_order.tv_search
import kotlinx.android.synthetic.main.activity_new_order.tv_search_his
import kotlinx.android.synthetic.main.activity_new_order.tv_total_item
import kotlinx.android.synthetic.main.activity_new_order.tv_ys
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import wang.relish.widget.vehicleedittext.VehicleKeyboardHelper
import java.math.BigDecimal

class NewOrderActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var itemAdapter: ItemAdapter
    private val items = ArrayList<ItemBean>()
    private var id = 0
    private var customerId = -1
    private var userCarId = -1
    private var carId = ""
    private var brandName = ""
    private var brandLogo = ""
    private var isCheckKH = true
    private var isCheckPhone = true
    private var isCarNo = true
    private val itemDelIds = ArrayList<Int>()
    private val dispatchList = ArrayList<ItemDispatchBean>()
    private var mileage = ""//行驶里程
    private var vinCode = ""//vin码
    private val recommendList = ArrayList<RecommendDs1>()//精友数据保养项目

    override fun getLayoutId(): Int {
        return R.layout.activity_new_order
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_new_order)
        id = intent.getIntExtra("id", 0)
        rv_new_order.layoutManager = LinearLayoutManager(this)
        itemAdapter = ItemAdapter(States.SERVICE, items)
        itemAdapter.bindToRecyclerView(rv_new_order)
        itemAdapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        when (intent.getStringExtra("orderType")) {
            "open" -> {
                LoadingUtils.showLoading(this, "加载中...")
                getOrderMsg()
            }

            "new" -> {
                tv_jd_time.text = TimeUtils.millis2String(TimeUtils.getNowMills())
                et_jdr.setText(SPUtils.getInstance().getString("nickName"))
                et_car_vin.setText(intent.getStringExtra("vin"))
                et_car_type.setText(intent.getStringExtra("model"))
                val list =
                    this.intent.getParcelableArrayListExtra<RecommendDs1>("items")
                sortSelItems(list)
            }
        }
        VehicleKeyboardHelper.bind(tv_car_no)
    }

//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        if (ev.action == MotionEvent.ACTION_DOWN)
//            if (isShouldHideKeyboard(tv_car_no, ev))
//                tv_car_no.clearFocus()
//        return super.dispatchTouchEvent(ev)
//    }

    override fun initListener() {
        tb_new_order.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {

            }

            override fun onRightClick(view: View?) {
                updateOrder(1)
            }
        })
        et_car_type.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                tv_search.visibility = if (TextUtils.isEmpty(s)) View.GONE else View.VISIBLE
            }
        })
        et_yh.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (TextUtils.isEmpty(s) || TextUtils.isEmpty(tv_hj.text.toString()))
                    return
                tv_ys.text = String.format(
                    "%.2f",
                    tv_hj.text.toString().toDouble() - s.toDouble()
                )
            }
        })
        itemAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_edit_item -> {
                    clearFocus()
                    PopupWindowUtils.get()
                        .showAddPartPop(
                            this, rl_content, tv_car_no.text.toString(), true, items[position]
                        ) { name: String,
                            type: String,
                            specs: String,
                            remark: String,
                            price: String,
                            num: String,
                            money: String,
                            cateId: Int,
                            cost: String ->
                            items[position].itemName = name
                            items[position].cateName = type
                            if (cateId != -1)
                                items[position].cateId = cateId
                            items[position].attrName = specs
                            items[position].remark = remark
                            items[position].unitPrice = price.toDouble()
                            items[position].num = num.toDouble()
                            items[position].itemMoney =
                                BigDecimal(money).add(BigDecimal(cost)).toDouble()
                            items[position].serviceFee = cost.toDouble()
                            itemAdapter.setNewData(items)
                            countHJ()
                        }
                }

                R.id.iv_delete_item -> {
                    clearFocus()
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get()
                        .showConfirmPop(this, "确认删除？") {
                            if (items[position].id != null)
                                itemDelIds.add(items[position].id)
                            items.removeAt(position)
                            adapter.setNewData(items)
                            countHJ()
                        }
                }

                R.id.iv_show_4s_price -> {
                    clearFocus()
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    if (TextUtils.isEmpty(et_car_vin.text.toString()))
                        ToastUtils.showShort("请输入vin码")
                    else
                        PopupWindowUtils.get().getBalance(this,
                            object : PopupWindowUtils.OnConfirmAndCancelListener {
                                override fun onConfirm() {
                                    getInformation(carId,
                                        items[position].jyitemId, rl_content,
                                        object : OnShow4SPriceListener {
                                            override fun onBack(
                                                price: String,
                                                fee: String,
                                                oe: String,
                                                num: String
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
                                    et_car_vin.text.toString(),
                                    items[position].itemName
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

                R.id.tv_work_person -> {//施工人员
                    clearFocus()
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get().showSelectPersonPop(this, "选择施工人员") {
                        upDateDispatch("3", position, it)
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
        tv_car_no.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                tv_search_his.visibility = if (TextUtils.isEmpty(s)) View.GONE else View.VISIBLE
                if (tv_car_no.isFocused)
                    if (isCarNo)
                        PopupWindowUtils.get().checkKeHuList(
                            tv_car_no, this@NewOrderActivity
                        ) {
                            isCarNo = false
                            et_kh_name.setText(it.customerName)
                            tv_car_no.setText(it.cardNo)
                            et_kh_phone.setText(it.customerPhone)
                            et_car_vin.setText(it.vnCode)
                            et_car_type.setText(it.carName)
                        }
                    else
                        isCarNo = true
            }
        })
        et_kh_name.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_kh_name.isFocused)
                    if (isCheckKH)
                        PopupWindowUtils.get().checkKeHuList(
                            et_kh_name,
                            this@NewOrderActivity
                        ) {
                            isCheckKH = false
                            et_kh_name.setText(it.customerName)
                            et_kh_name.setSelection(it.customerName.length)
                            tv_car_no.setText(it.cardNo)
                            et_kh_phone.setText(it.customerPhone)
                            et_car_vin.setText(it.vnCode)
                            et_car_type.setText(it.carName)
                        }
                    else
                        isCheckKH = true
            }
        })
        et_kh_phone.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_kh_phone.isFocused)
                    if (isCheckPhone)
                        PopupWindowUtils.get().checkKeHuList(
                            et_kh_phone,
                            this@NewOrderActivity
                        ) {
                            isCheckPhone = false
                            et_kh_name.setText(it.customerName)
                            tv_car_no.setText(it.cardNo)
                            et_kh_phone.setText(it.customerPhone)
                            et_kh_phone.setSelection(it.customerPhone.length)
                            et_car_vin.setText(it.vnCode)
                            et_car_type.setText(it.carName)
                        }
                    else
                        isCheckPhone = true
            }
        })
    }

    private fun clearFocus() {
        tv_car_no.clearFocus()
        et_car_vin.clearFocus()
        et_lc.clearFocus()
        et_car_type.clearFocus()
        et_kh_name.clearFocus()
        et_kh_phone.clearFocus()
    }

    private fun upDateDispatch(type: String, position: Int, arr: MutableList<PersonRow>) {
        val dis = ArrayList<ItemDispatchBean>()
        for (row: PersonRow in arr) {
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
                    type,
                    ""
                )
            )
        }
        val otherDis = ArrayList<ItemDispatchBean>()
        if (items[position].itemDispatchList != null && items[position].itemDispatchList.isNotEmpty()) {
            for (item: ItemDispatchBean in items[position].itemDispatchList) {
                if (item.type != type)
                    otherDis.add(item)
            }
            items[position].itemDispatchList.clear()
        }
        items[position].itemDispatchList.addAll(dis)
        items[position].itemDispatchList.addAll(otherDis)
        itemAdapter.setNewData(items)
    }

    override fun onRepeatClick(v: View?) {
        when (v!!.id) {
            R.id.tv_search_his -> {
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

            R.id.iv_scan_vin -> {
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, OCRScanActivity::class.java)
                intent.putExtra("ocrType", 2)
                startActivity(intent)
            }

            R.id.iv_scan_che_no -> {
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, OCRScanActivity::class.java)
                intent.putExtra("ocrType", 0)
                startActivity(intent)
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
                                getVinData(
                                    et_car_vin.text.toString(),
                                    object : OnGetVinBackListener {
                                        override fun onBack(model: String, id: String) {
                                            carId = id
                                            et_car_type.setText(model)
                                        }
                                    })
                            }

                            override fun onCancel() {
                            }
                        })
                }
            }

            R.id.tv_pics -> {
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, WorkPicActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("type", "order")
                startActivity(intent)
            }

            R.id.tv_dispatch -> {
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelectPersonPop(this, "选择总负责人") {
                    dispatchList.clear()
                    for (row: PersonRow in it) {
                        dispatchList.add(
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
                    val sb = StringBuilder()
                    for (row: ItemDispatchBean in dispatchList) {
                        if (sb.isNotEmpty())
                            sb.append(",")
                        sb.append(row.staffName)
                    }
                    tv_dispatch.text = sb.toString()
                }
            }

            R.id.tv_add_item -> {
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                KeyboardUtils.hideSoftInput(this)
                PopupWindowUtils.get()
                    .showAddPartPop(
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
                        itemBean.itemDispatchList = ArrayList()
                        itemBean.itemName = name
                        itemBean.cateName = type
                        if (cateId != -1)
                            itemBean.cateId = cateId
                        itemBean.attrName = specs
                        itemBean.remark = remark
                        itemBean.type = 0
                        if (TextUtils.isEmpty(price) && TextUtils.isEmpty(num) && TextUtils.isEmpty(
                                cost
                            )
                        )
                            getItemHisPrice(name, itemBean)
                        else {
                            itemBean.unitPrice = price.toDouble()
                            itemBean.num = num.toDouble()
                            itemBean.serviceFee = cost.toDouble()
                            itemBean.itemMoney = BigDecimal(money).add(BigDecimal(cost)).toDouble()
                            items.add(itemBean)
                            itemAdapter.setNewData(items)
                            countHJ()
                        }
                    }
            }

            R.id.tv_maintain -> {
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(carId) -> ToastUtils.showShort("请先通过vin码解析车型")
                    TextUtils.isEmpty(et_lc.text.toString()) -> ToastUtils.showShort("请输入行驶里程")
                    et_lc.text.toString() == mileage && et_car_vin.text.toString() == vinCode -> {
                        val intent = Intent(
                            this@NewOrderActivity,
                            MaintainProposalActivity::class.java
                        )
                        intent.putExtra("mileage", et_lc.text.toString())
                        intent.putExtra("vinCode", et_car_vin.text.toString())
                        intent.putParcelableArrayListExtra("list", recommendList)
                        startActivity(intent)
                    }

                    else -> PopupWindowUtils.get()
                        .getBalance(this, object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                getJYItemList()
                            }

                            override fun onCancel() {
                            }
                        })
                }
            }

            R.id.tv_finished -> {
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(tv_dispatch.text.toString()) -> ToastUtils.showShort("请选择总负责人")
                    items.isEmpty() -> ToastUtils.showShort("请添加服务项目")
                    else -> PopupWindowUtils.get()
                        .showConfirmPop(this, "是否确认完工？") {
                            finishedOrder()
                        }
                }
            }

            R.id.iv_select_model -> {
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, BrandActivity::class.java)
                intent.putExtra("selType", "all")
                startActivity(intent)
            }

            R.id.tv_total_item -> {
                clearFocus()
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (items.isEmpty())
                    return
                PopupWindowUtils.get().showSelectedItemPop(
                    items, this, tv_total_item.text.toString(), tv_price.text.toString()
                )
            }
        }
    }

    private fun newOrder(int: Int) {
        if (tv_car_no.text.toString().length < 7) {
            ToastUtils.showShort("请输入正确的车牌号")
            return
        }
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["cardNo"] = tv_car_no.text.toString()
        params["customerName"] = et_kh_name.text.toString()
        params["customerPhone"] = et_kh_phone.text.toString()
        params["carName"] = et_car_type.text.toString()
        params["vnCode"] = et_car_vin.text.toString()
        params["mileage"] = et_lc.text.toString()
        params["orderType"] = "2"
        params["orderMoney"] = tv_price.text.toString()
        params["discountAmount"] = et_yh.text.toString()
        params["realMoney"] = tv_ys.text.toString()
        params["receiveBy"] = et_jdr.text.toString()
        params["receiveTime"] = tv_jd_time.text.toString()
        if (!TextUtils.isEmpty(brandLogo))
            params["brandLogo"] = brandLogo
        if (!TextUtils.isEmpty(brandName))
            params["brandName"] = brandName
        if (!TextUtils.isEmpty(carId))
            params["jycarId"] = carId
        params["itemList"] = items
        RetrofitUtils.get()
            .postJson(UrlConstants.NewOrderUrl, params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        ToastUtils.showShort("保存成功")
                        if (int == 0)
                            finish()
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }

    private fun getOrderMsg() {
        Api.getInstance().getApiService()
            .getOrderMsg(id)
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
                            userCarId = t.data.userCarId
                            customerId = t.data.customerId
                            brandLogo = t.data.brandLogo
                            brandName = t.data.brandName
                            carId = t.data.jycarId
                            et_kh_name.setText(t.data.customerName)
                            et_kh_phone.setText(t.data.customerPhone)
                            tv_car_no.setText(t.data.cardNo)
                            et_car_type.setText(t.data.carName)
                            et_car_vin.setText(t.data.vnCode)
                            et_jdr.setText(t.data.receiveBy)
                            tv_jd_time.text = t.data.receiveTime
                            et_lc.setText(t.data.mileage)
                            et_yh.setText(t.data.discountAmount.toString())
                            tv_price.text = t.data.orderMoney.toString()
                            dispatchList.addAll(t.data.dispatchVoList)
                            val sb = StringBuilder()
                            for (row: ItemDispatchBean in t.data.dispatchVoList) {
                                if (row.type == "1") {
                                    if (sb.isNotEmpty())
                                        sb.append(",")
                                    sb.append(row.staffName)
                                }
                            }
                            tv_dispatch.text = sb.toString()
                            tv_ys.text = t.data.realMoney.toString()
                            if (t.data.itemList != null) {
                                items.addAll(t.data.itemList)
                                itemAdapter.setNewData(items)
                            }
                            if (SPUtils.getInstance().getString("mileage") == t.data.mileage &&
                                SPUtils.getInstance().getString("vinCode") == t.data.vnCode &&
                                SPUtils.getInstance().getString("recommendItems") != ""
                            ) {
                                mileage = t.data.mileage
                                vinCode = t.data.vnCode
                                val gson = GsonUtils.fromJson(
                                    SPUtils.getInstance().getString("recommendItems"),
                                    RecommendResult::class.java
                                )
                                recommendList.addAll(gson.ds1!!)
                            }
                            countHJ()
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@NewOrderActivity)
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

    /**
     * @param type 0:返回时保存  1:打印时保存
     */
    private fun updateOrder(type: Int) {
        if (tv_car_no.text.toString().length < 7) {
            ToastUtils.showShort("请输入正确的车牌号")
            return
        }
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["cardNo"] = tv_car_no.text.toString()
        params["customerName"] = et_kh_name.text.toString()
        params["customerPhone"] = et_kh_phone.text.toString()
        params["carName"] = et_car_type.text.toString()
        params["vnCode"] = et_car_vin.text.toString()
        params["mileage"] = et_lc.text.toString()
        params["orderType"] = "2"
        params["orderMoney"] = tv_price.text.toString()
        params["discountAmount"] = et_yh.text.toString()
        params["realMoney"] = tv_ys.text.toString()
        params["receiveBy"] = et_jdr.text.toString()
        params["receiveTime"] = tv_jd_time.text.toString()
        if (customerId != -1)
            params["customerId"] = customerId
        if (userCarId != -1)
            params["userCarId"] = userCarId
        if (!TextUtils.isEmpty(brandLogo))
            params["brandLogo"] = brandLogo
        if (!TextUtils.isEmpty(brandName))
            params["brandName"] = brandName
        if (!TextUtils.isEmpty(carId))
            params["jycarId"] = carId
        params["id"] = id
        params["itemList"] = items
        params["itemDelIds"] = itemDelIds
        params["itemDispatchList"] = dispatchList
        RetrofitUtils.get().putJson(
            UrlConstants.UpdateOrderUrl,
            params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    ToastUtils.showShort(t.msg)
                    when (type) {
                        0 -> finish()
                        1 -> bluetoothUtils(
                            this@NewOrderActivity,
                            States.OrderPrintType.frID,
                            id.toString(), 1
                        ).print()
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun finishedOrder() {
        if (tv_car_no.text.toString().length < 7) {
            ToastUtils.showShort("请输入正确的车牌号")
            return
        }
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["cardNo"] = tv_car_no.text.toString()
        params["customerName"] = et_kh_name.text.toString()
        params["customerPhone"] = et_kh_phone.text.toString()
        params["carName"] = et_car_type.text.toString()
        params["vnCode"] = et_car_vin.text.toString()
        params["mileage"] = et_lc.text.toString()
        params["orderType"] = "2"
        params["orderMoney"] = tv_price.text.toString()
        params["discountAmount"] = et_yh.text.toString()
        params["realMoney"] = tv_ys.text.toString()
        params["receiveBy"] = et_jdr.text.toString()
        params["receiveTime"] = tv_jd_time.text.toString()
        if (customerId != -1)
            params["customerId"] = customerId
        if (userCarId != -1)
            params["userCarId"] = userCarId
        if (!TextUtils.isEmpty(brandLogo))
            params["brandLogo"] = brandLogo
        if (!TextUtils.isEmpty(brandName))
            params["brandName"] = brandName
        if (!TextUtils.isEmpty(carId))
            params["jycarId"] = carId
        if (intent.getStringExtra("orderType") == "open")
            params["id"] = id
        params["orderStatus"] = 1
        params["itemList"] = items
        params["itemDelIds"] = itemDelIds
        params["itemDispatchList"] = dispatchList
        RetrofitUtils.get().putJson(
            UrlConstants.FinishedOrderUrl,
            params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, OrderFinishedResponse::class.java)
                    if (t.data.orderMoney.toDouble() > t.data.balancePay.toDouble()) {
                        val intent = Intent(this@NewOrderActivity, OrderSettleActivity::class.java)
                        intent.putExtra("total", t.data.orderMoney)
                        intent.putExtra("balancePay", t.data.balancePay)
                        intent.putExtra("moneyOwe", t.data.moneyOwe)
                        intent.putExtra("orderId", id)
                        startActivity(intent)
                        finish()
                    } else
                        PopupWindowUtils.get().showOnlyConfirmPop(
                            this@NewOrderActivity, "收款提醒",
                            "本单已自动收款，本单金额${tv_price.text}元，余额抵扣${t.data.balancePay}元，本单剩余应收0元。"
                        ) {
                            finish()
                        }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    @Subscribe
    fun onNewCus(eb: EBNewCustomer) {
        if (eb.isNew)
            PopupWindowUtils.get().showSelectPersonPop(this, "选择总负责人") {
                dispatchList.clear()
                for (row: PersonRow in it) {
                    dispatchList.add(
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
                val sb = StringBuilder()
                for (row: ItemDispatchBean in dispatchList) {
                    if (sb.isNotEmpty())
                        sb.append(",")
                    sb.append(row.staffName)
                }
                tv_dispatch.text = sb.toString()
            }
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
    fun onSelMaintainCallBack(eb: EBSelMaintain) {
        sortSelItems(eb.list)
    }

    private fun sortSelItems(selItems: ArrayList<RecommendDs1>) {
        LoadingUtils.showLoading(this, "加载中...")
        val array = ArrayList<String>()//所有选中的名称
        for (item: RecommendDs1 in selItems) {
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
                            for (ds1: RecommendDs1 in selItems) {
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
                            countHJ()
                            LoadingUtils.dismissLoading()
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@NewOrderActivity)
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

    /**
     * 获取最近一次历史价
     */
    private fun getItemHisPrice(itemName: String, itemBean: ItemBean) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["cardNo"] = tv_car_no.text.toString()
        params["itemName"] = itemName
        RetrofitUtils.get().getJson(
            UrlConstants.ItemHistoryPriceUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, ItemHisPriceResponse::class.java)
                    itemBean.unitPrice = t.data.unitPrice
                    itemBean.serviceFee = t.data.serviceFee
                    itemBean.num = t.data.num
                    items.add(itemBean)
                    itemAdapter.setNewData(items)
                    countHJ()
                }

                override fun onFailed(e: String) {
                    itemBean.unitPrice = 0.0
                    itemBean.serviceFee = 0.0
                    itemBean.num = 1.0
                    items.add(itemBean)
                    itemAdapter.setNewData(items)
                    countHJ()
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
        params["inStr4"] = et_lc.text.toString()
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(
                UrlConstants.BvdcUrl,
                params,
                object : RetrofitUtils.OnCallBackListener {
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
        params["mileage"] = et_lc.text.toString()
        RetrofitUtils.get().getJson(
            UrlConstants.MaintainProposalUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, MaintainProposalBean::class.java)
                    mileage = et_lc.text.toString()
                    vinCode = et_car_vin.text.toString()
                    val allItem = ArrayList<MaintainItem>()
                    allItem.addAll(t.data.itemList)
                    for (item: MaintainItem in allItem) {
                        if (item.cateId == 1) {
                            recommendList.add(
                                RecommendDs1(
                                    "", item.id.toString(), item.name, "", "",
                                    "1", "", true, "4"
                                )
                            )
                        } else if (item.cateId == 2) {
                            recommendList.add(
                                RecommendDs1(
                                    "", item.id.toString(), item.name, "", "",
                                    "0", "", false, "4"
                                )
                            )
                        }
                    }
                    val intent = Intent(
                        this@NewOrderActivity,
                        MaintainProposalActivity::class.java
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

    private fun countHJ() {
        var total = BigDecimal(0)
        for (item: ItemBean in items) {
            total = total.add(
                BigDecimal(item.unitPrice).multiply(BigDecimal(item.num))
                    .add(BigDecimal(item.serviceFee)).setScale(2, BigDecimal.ROUND_HALF_DOWN)
            )
        }
        tv_hj.text = total.toString()
        tv_ys.text = BigDecimal(tv_hj.text.toString()).subtract(
            BigDecimal(
                if (TextUtils.isEmpty(et_yh.text.toString()))
                    "0.0"
                else
                    et_yh.text.toString()
            )
        ).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString()
        tv_total_item.text = "共${items.size}项"
        tv_price.text = "￥${total}"
    }

//    private fun showQECodePop() {
//        val popWindow = PopWindow.Builder(this)
//            .setView(R.layout.layout_pop_qr_code)
//            .setWidthAndHeight(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//            .setOutsideTouchable(true)
//            .setAnimStyle(R.style.BottomAnimation)
//            .setBackGroundLevel(0.5f)
//            .setChildrenView(object : PopWindow.ViewInterface {
//                override fun getChildView(view: View, layoutResId: Int, pop: PopupWindow) {
//                    view.findViewById<View>(R.id.iv_close).setOnClickListener {
////                        dismiss()
//                    }
//                }
//            })
//            .create()
//        popWindow.isClippingEnabled = false
//        popWindow.showAtLocation(rl_content, Gravity.BOTTOM, 0, 0)
//    }

    override fun onBackPressed() {
        KeyboardUtils.hideSoftInput(this)
        if (PopupWindowUtils.get().getPopWindow() != null) {
            if (PopupWindowUtils.get().isShowing()) {
                PopupWindowUtils.get().dismiss()
                return
            }
        }
        PopupWindowUtils.get().showConfirmAndCancelPop(this, "是否保存工单?",
            object : PopupWindowUtils.OnConfirmAndCancelListener {
                override fun onConfirm() {
                    when (intent.getStringExtra("orderType")) {
                        "open" -> {
                            updateOrder(0)
                        }

                        "new" -> {
                            newOrder(0)
                        }
                    }
                }

                override fun onCancel() {
                    finish()
                }
            })
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        PopupWindowUtils.get().dismiss()
        super.onDestroy()
    }
}
