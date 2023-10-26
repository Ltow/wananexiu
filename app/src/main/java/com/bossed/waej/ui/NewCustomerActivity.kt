package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.adapter.CarAdapter
import com.bossed.waej.adapter.GivePersonAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.customview.BottomDialog
import com.bossed.waej.eventbus.EBOCRCallBack
import com.bossed.waej.eventbus.EBSelCarModel
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants.NewCustomerUrl
import com.bossed.waej.javebean.*
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_new_customer.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.StringBuilder
import java.math.BigDecimal
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NewCustomerActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var carAdapter: CarAdapter
    private val cars = ArrayList<Car>()
    private lateinit var givePersonAdapter: GivePersonAdapter
    private val persons = ArrayList<WorkOrder>()
    private var carPosition = -1
    private val tags = ArrayList<TextView>()
    private var editCarNoPosition = -1
    private var checkKeHu = true
    private var isEdit = false

    override fun getLayoutId(): Int {
        return R.layout.activity_new_customer
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_new_customer)
        rv_customer_person.layoutManager = LinearLayoutManager(this)
        givePersonAdapter = GivePersonAdapter(persons)
        givePersonAdapter.bindToRecyclerView(rv_customer_person)
        rv_customer_car.layoutManager = LinearLayoutManager(this)
        carAdapter = CarAdapter(cars)
        carAdapter.bindToRecyclerView(rv_customer_car)
        val footView = LayoutInflater.from(this).inflate(R.layout.layout_footer_view_item, null)
        footView.findViewById<TextView>(R.id.tv_add_item).setOnClickListener(this)
        footView.findViewById<TextView>(R.id.tv_add_item).text = "增加车辆"
        carAdapter.addFooterView(footView)
        when (intent.getStringExtra("type")) {
            "open" -> {
                tb_new_customer.title = "客户详情"
                getCustomerMsg(intent.getIntExtra("customerId", -1))
            }
            "new" -> {
                tv_receiveBy.text = SPUtils.getInstance().getString("nickName")
            }
        }
    }

    override fun initListener() {
        tb_new_customer.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        carAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.rl_model2 -> {
                    carPosition = position
                    val intent = Intent(this, BrandActivity::class.java)
                    intent.putExtra("selType", "all")
                    startActivity(intent)
                    isEdit = true
                }
                R.id.iv_delete -> {
                    PopupWindowUtils.get()
                        .showConfirmPop(this, "确定删除此车辆？") {
                            cars.removeAt(position)
                            carAdapter.setNewData(cars)
                            isEdit = true
                        }
                }
                R.id.iv_scan_vin -> {
                    carPosition = position
                    val intent = Intent(this, OCRScanActivity::class.java)
                    intent.putExtra("ocrType", 2)
                    startActivity(intent)
                    isEdit = true
                }
                R.id.tv_search -> {
                    isEdit = true
                    PopupWindowUtils.get()
                        .getBalance(this, object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                getVinData(cars[position].vnCode, object : OnGetVinBackListener {
                                    override fun onBack(model: String, id: String) {
                                        cars[position].carName = model
                                        cars[position].jycarId = id
                                        carAdapter.setNewData(cars)
                                        isEdit = true
                                    }
                                })
                            }

                            override fun onCancel() {

                            }
                        })
                }
                R.id.tv_sy_date -> {
                    PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                        cars[position].insureDate = DateFormatUtils.get().formatDate(it)
                        adapter.setNewData(cars)
                    }
                }
                R.id.tv_jq_date -> {
                    PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                        cars[position].insure2Date = DateFormatUtils.get().formatDate(it)
                        adapter.setNewData(cars)
                    }
                }
                R.id.tv_nj_date -> {
                    PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                        cars[position].dueDate = DateFormatUtils.get().formatDate(it)
                        adapter.setNewData(cars)
                    }
                }
                R.id.iv_set_alert -> {
                    showSetAlertDia(position)
                }
            }
        }
        carAdapter.setOnCarNoEditListener(object : CarAdapter.OnCarNoEditListener {
            override fun onEdit(position: Int) {
                editCarNoPosition = position
                isEdit = true
            }
        })
        et_phone.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_phone.isFocused) if (checkKeHu)
                    PopupWindowUtils.get().checkKeHuList(et_phone, this@NewCustomerActivity) {
                        checkKeHu = false
                        getCustomerMsg(it.customerId)
                    } else checkKeHu = true
                isEdit = true
            }
        })
    }

//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        if (editCarNoPosition != -1) if (ev.action == MotionEvent.ACTION_DOWN) {
//            if (cars.isEmpty()) return super.dispatchTouchEvent(ev)
//            val e = carAdapter.getViewByPosition(editCarNoPosition, R.id.tv_car_no) as EditText
//            if (isShouldHideKeyboard(e, ev)) e.clearFocus()
//        }
//        return super.dispatchTouchEvent(ev)
//    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_add_item -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val car = Car(0, "", "", "", "", "", "", "", "", "")
                cars.add(car)
                carAdapter.setNewData(cars)
                isEdit = true
            }
            R.id.tv_add_tag -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showEditPop(this, rl_content, "添加标签", "标签名称", "请输入标签名称") {
                    if (TextUtils.isEmpty(it))
                        return@showEditPop
                    val layout = LayoutInflater.from(this@NewCustomerActivity)
                        .inflate(R.layout.layout_item_tags_textview, null)
                    val textView = layout.findViewById<TextView>(R.id.tv_tag)
                    textView.text = it
                    fl_new_customer.addView(layout)
                    tags.add(textView)
                    isEdit = true
                }
            }
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when (intent.getStringExtra("type")) {
                    "open" -> {
                        updateCustomer()
                    }
                    "new" -> {
                        saveCustomer()
                    }
                }
            }
            R.id.tv_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }
        }
    }

    private val showSetAlertDia = { position: Int ->
        BottomDialog(this).create(R.layout.layout_bottom_dialog_set_alert)
            .setCanceledOnTouchOutside(true)
            .setViewInterface { content, dialog ->
                val car = cars[position]
                val cb_bx_alert = content.findViewById<CheckBox>(R.id.cb_bx_alert)
                val cb_nj_alert = content.findViewById<CheckBox>(R.id.cb_nj_alert)
                val cb_by_alert = content.findViewById<CheckBox>(R.id.cb_by_alert)
                val cb_gls = content.findViewById<CheckBox>(R.id.cb_gls)
                val cb_date = content.findViewById<CheckBox>(R.id.cb_date)
                val tv_date_sy = content.findViewById<TextView>(R.id.tv_date_sy)
                val tv_date_jq = content.findViewById<TextView>(R.id.tv_date_jq)
                val tv_nj_date = content.findViewById<TextView>(R.id.tv_nj_date)
                val tv_next_date = content.findViewById<TextView>(R.id.tv_next_date)
                val tv_an_date = content.findViewById<TextView>(R.id.tv_an_date)
                val et_advanceInsure =
                    content.findViewById<EditText>(R.id.et_advanceInsure)
                val et_advanceDue =
                    content.findViewById<EditText>(R.id.et_advanceDue)
                val et_advanceMaintenance =
                    content.findViewById<EditText>(R.id.et_advanceMaintenance)
                val et_mileage = content.findViewById<EditText>(R.id.et_mileage)
                val et_next = content.findViewById<EditText>(R.id.et_next)
                val et_day = content.findViewById<EditText>(R.id.et_day)
                cb_bx_alert.isChecked = car.remindInsure == 1
                content.findViewById<View>(R.id.ll_bx_alert).visibility =
                    if (car.remindInsure == 1) View.VISIBLE else View.GONE
                cb_nj_alert.isChecked = car.remindDue == 1
                content.findViewById<View>(R.id.ll_nj_alert).visibility =
                    if (car.remindDue == 1) View.VISIBLE else View.GONE
                cb_by_alert.isChecked = car.remindMaintenance == 1
                content.findViewById<View>(R.id.ll_by_alert).visibility =
                    if (car.remindMaintenance == 1) View.VISIBLE else View.GONE
                tv_date_sy.text = car.insureDate
                tv_date_jq.text = car.insure2Date
                tv_nj_date.text = car.dueDate

                et_advanceInsure.setText(car.advanceInsure.toString())
                et_advanceDue.setText(car.advanceDue.toString())
                et_advanceMaintenance.setText(car.advanceMaintenance.toString())
                et_mileage.setText(car.mileage)
                et_next.setText(car.maintenanceMileageNext.toString())
                et_day.setText(car.maintenanceMileageDay.toString())
                when (car.maintenanceType) {
                    1 -> cb_gls.isChecked = true
                    2 -> cb_date.isChecked = true
                }
                if (cb_gls.isChecked)
                    tv_next_date.text = car.maintenanceDate
                if (cb_date.isChecked)
                    tv_an_date.text = car.maintenanceDate
                content.findViewById<View>(R.id.ll_gls).visibility =
                    if (cb_gls.isChecked) View.VISIBLE else View.GONE
                content.findViewById<View>(R.id.rl_date).visibility =
                    if (cb_date.isChecked) View.VISIBLE else View.GONE
                et_mileage.addTextChangedListener(object : TextChangedListener {
                    override fun afterTextChange(s: String) {
                        if (TextUtils.isEmpty(s) || TextUtils.isEmpty(et_next.text.toString())
                            || TextUtils.isEmpty(et_day.text.toString())
                        )
                            return
                        if (et_mileage.isFocused) {
                            if (et_day.text.toString().toInt() == 0)
                                return
                            val surplus =
                                BigDecimal(et_next.text.toString()).subtract(BigDecimal(s))
                            val days = surplus.divide(
                                BigDecimal(et_day.text.toString()),
                                0,
                                BigDecimal.ROUND_HALF_DOWN
                            )
                            tv_next_date.text = DateFormatUtils.get()
                                .formatDate(CalendarUtils.get().nowDateAddDays(days.toInt()))
                        }
                    }
                })
                et_next.addTextChangedListener(object : TextChangedListener {
                    override fun afterTextChange(s: String) {
                        if (TextUtils.isEmpty(s) || TextUtils.isEmpty(et_mileage.text.toString())
                            || TextUtils.isEmpty(et_day.text.toString())
                        )
                            return
                        if (et_next.isFocused) {
                            if (et_day.text.toString().toInt() == 0)
                                return
                            val surplus =
                                BigDecimal(et_next.text.toString()).subtract(BigDecimal(et_mileage.text.toString()))
                            val days = surplus.divide(
                                BigDecimal(et_day.text.toString()),
                                0,
                                BigDecimal.ROUND_HALF_DOWN
                            )
                            tv_next_date.text = DateFormatUtils.get()
                                .formatDate(CalendarUtils.get().nowDateAddDays(days.toInt()))
                        }
                    }
                })
                et_day.addTextChangedListener(object : TextChangedListener {
                    override fun afterTextChange(s: String) {
                        if (TextUtils.isEmpty(s) || TextUtils.isEmpty(et_mileage.text.toString())
                            || TextUtils.isEmpty(et_next.text.toString())
                        )
                            return
                        if (et_day.isFocused) {
                            if (et_day.text.toString().toInt() == 0)
                                return
                            val surplus =
                                BigDecimal(et_next.text.toString()).subtract(BigDecimal(et_mileage.text.toString()))
                            val days = surplus.divide(
                                BigDecimal(et_day.text.toString()),
                                0,
                                BigDecimal.ROUND_HALF_DOWN
                            )
                            tv_next_date.text = DateFormatUtils.get()
                                .formatDate(CalendarUtils.get().nowDateAddDays(days.toInt()))
                        }
                    }
                })
                tv_an_date.setOnClickListener {
                    PopupWindowUtils.get().showSelDateTimePop(this, 2) { lone ->
                        tv_an_date.text = DateFormatUtils.get().formatDate(lone)
                    }
                }
                cb_bx_alert.setOnCheckedChangeListener { buttonView, isChecked ->
                    content.findViewById<View>(R.id.ll_bx_alert).visibility =
                        if (isChecked) View.VISIBLE else View.GONE
                }
                cb_nj_alert.setOnCheckedChangeListener { buttonView, isChecked ->
                    content.findViewById<View>(R.id.ll_nj_alert).visibility =
                        if (isChecked) View.VISIBLE else View.GONE
                }
                cb_by_alert.setOnCheckedChangeListener { buttonView, isChecked ->
                    content.findViewById<View>(R.id.ll_by_alert).visibility =
                        if (isChecked) View.VISIBLE else View.GONE
                    cb_gls.isChecked = isChecked
                }
                cb_gls.setOnCheckedChangeListener { buttonView, isChecked ->
                    cb_date.isChecked = !isChecked
                    content.findViewById<View>(R.id.ll_gls).visibility =
                        if (isChecked) View.VISIBLE else View.GONE
                }
                cb_date.setOnCheckedChangeListener { buttonView, isChecked ->
                    cb_gls.isChecked = !isChecked
                    content.findViewById<View>(R.id.rl_date).visibility =
                        if (isChecked) View.VISIBLE else View.GONE
                }
                content.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                    dialog.dismiss()
                }
                content.findViewById<View>(R.id.tv_confirm).setOnClickListener {
                    isEdit = true
                    if (cb_bx_alert.isChecked) {
                        car.remindInsure = 1
                        car.advanceInsure =
                            if (TextUtils.isEmpty(et_advanceInsure.text.toString())) 0 else et_advanceInsure.text.toString()
                                .toInt()
                    } else {
                        car.remindInsure = 0
                    }
                    if (cb_nj_alert.isChecked) {
                        car.remindDue = 1
                        car.advanceDue =
                            if (TextUtils.isEmpty(et_advanceDue.text.toString())) 0 else et_advanceDue.text.toString()
                                .toInt()
                    } else
                        car.remindDue = 0
                    if (cb_by_alert.isChecked) {
                        car.remindMaintenance = 1
                        car.advanceMaintenance =
                            if (TextUtils.isEmpty(et_advanceMaintenance.text.toString())) 0 else et_advanceMaintenance.text.toString()
                                .toInt()
                        if (cb_gls.isChecked) {
                            car.maintenanceDate = tv_next_date.text.toString()
                            car.maintenanceType = 1
                            car.mileage =
                                if (TextUtils.isEmpty(et_mileage.text.toString())) "0" else et_mileage.text.toString()
                            car.maintenanceMileageNext =
                                if (TextUtils.isEmpty(et_next.text.toString())) 0 else et_next.text.toString()
                                    .toInt()
                            car.maintenanceMileageDay =
                                if (TextUtils.isEmpty(et_day.text.toString())) 0 else et_day.text.toString()
                                    .toInt()
                        }
                        if (cb_date.isChecked) {
                            car.maintenanceType = 2
                            car.maintenanceDate = tv_an_date.text.toString()
                        }
                    } else
                        car.remindMaintenance = 0
                    carAdapter.setNewData(cars)
                    dialog.dismiss()
                }
            }.show()
    }

    private fun updateCustomer() {
        for (car: Car in cars) {
            if (car.cardNo.length < 7) {
                ToastUtils.showShort("请输入正确的车牌号")
                return
            }
        }
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["carList"] = cars
        params["id"] = intent.getIntExtra("customerId", -1)
        params["createBy"] = tv_receiveBy.text.toString()
        params["customerName"] = et_name.text.toString()
        params["customerPhone"] = et_phone.text.toString()
        params["remark"] = et_remark.text.toString()
        val stringBuilder = StringBuilder()
        for (textView: TextView in tags) {
            if (stringBuilder.isNotEmpty()) stringBuilder.append(",")
            stringBuilder.append(textView.text.toString())
        }
        params["tags"] = stringBuilder.toString()
        RetrofitUtils.get()
            .putJson(NewCustomerUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    ToastUtils.showShort("保存成功")
                    finish()
                    isEdit = false
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun saveCustomer() {
        for (car: Car in cars) {
            if (car.cardNo.length < 7) {
                ToastUtils.showShort("请输入正确的车牌号")
                return
            }
        }
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["carList"] = cars
        params["createBy"] = tv_receiveBy.text.toString()
        params["customerName"] = et_name.text.toString()
        params["customerPhone"] = et_phone.text.toString()
        params["remark"] = et_remark.text.toString()
        val stringBuilder = StringBuilder()
        for (textView: TextView in tags) {
            if (stringBuilder.isNotEmpty()) stringBuilder.append(",")
            stringBuilder.append(textView.text.toString())
        }
        params["tags"] = stringBuilder.toString()
        RetrofitUtils.get()
            .postJson(NewCustomerUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    ToastUtils.showShort("保存成功")
                    finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private val getCustomerMsg: (Int) -> Unit = {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService().getCustomerMsg(it)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<CustomerMsgBean>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: CustomerMsgBean) {
                    when (t.code) {
                        200 -> {
                            if (t.data.carList != null && t.data.carList.isNotEmpty()) {
                                cars.addAll(t.data.carList)
                                for (car: Car in cars) {
                                    car.type = 1
                                }
                                carAdapter.setNewData(cars)
                            }
                            et_name.setText(t.data.customerName)
                            et_phone.setText(t.data.customerPhone)
                            et_remark.setText(t.data.remark)
                            tv_receiveBy.text = t.data.createBy
                            var temp = arrayOf<String>()
                            if (!TextUtils.isEmpty(t.data.tags)) {
                                temp = t.data.tags.split(",").toTypedArray()
                            }
                            for (str: String in temp) {
                                val layout = LayoutInflater.from(this@NewCustomerActivity)
                                    .inflate(R.layout.layout_item_tags_textview, null)
                                val textView = layout.findViewById<TextView>(R.id.tv_tag)
                                textView.text = str
                                fl_new_customer.addView(layout)
                                tags.add(textView)
                            }
                            isEdit = false
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@NewCustomerActivity)
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
                    LoadingUtils.dismissLoading()
                }
            })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelCarModelCallBack(eb: EBSelCarModel) {
        cars[carPosition].carName = eb.model
        cars[carPosition].jycarId = eb.vehicleId
        cars[carPosition].brandName = eb.brand
        cars[carPosition].brandLogo = eb.logo
        carAdapter.setNewData(cars)
        isEdit = true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onOcrScanCallBack(eb: EBOCRCallBack) {
        cars[carPosition].vnCode = eb.vin
        LogUtils.d("tag", carPosition)
        LogUtils.d("tag", cars[carPosition].cardNo)
        carAdapter.setNewData(cars)
        isEdit = true
    }

    override fun onBackPressed() {
        if (isEdit) {
            PopupWindowUtils.get().showConfirmAndCancelPop(this, "是否保存客户信息？",
                object : PopupWindowUtils.OnConfirmAndCancelListener {
                    override fun onConfirm() {
                        when (intent.getStringExtra("type")) {
                            "open" -> {
                                updateCustomer()
                            }
                            "new" -> {
                                saveCustomer()
                            }
                        }
                    }

                    override fun onCancel() {
                        finish()
                    }
                })
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        PopupWindowUtils.get().dismiss()
    }
}