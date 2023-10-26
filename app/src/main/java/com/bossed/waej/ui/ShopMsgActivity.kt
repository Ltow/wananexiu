package com.bossed.waej.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants.UpdateShopMsgUrl
import com.bossed.waej.javebean.ShopMsgBean
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_shop_msg.*
import java.lang.StringBuilder
import java.util.ArrayList
import android.widget.TimePicker
import com.blankj.utilcode.constant.PermissionConstants
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.KmShopImageVoListBean
import com.bossed.waej.javebean.UpDateBVDCResponse
import com.bossed.waej.ui.amap.AMapActivity
import com.bossed.waej.util.*

class ShopMsgActivity : BaseActivity(), OnNoRepeatClickListener {
    private val checkBoxes = ArrayList<CheckBox>()
    private var businessBegin = ""
    private var businessEnd = ""
    private var doorPhoto = ""
    private var latitude = "0.0"//纬度
    private var longitude = "0.0"//经度
    private var province = ""
    private var city = ""
    private var county = ""
    private var licenseUrl = ""
    private val kmShopImageVoList = ArrayList<KmShopImageVoListBean>()

    override fun getLayoutId(): Int {
        return R.layout.activity_shop_msg
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_shop_msg)
        val array = arrayOf("供货及时", "有问必答", "轮胎专营", "奥迪", "汽配城", "维护工具", "其他")
        for (str: String in array) {
            val checkbox = LayoutInflater.from(Utils.getApp()).inflate(
                R.layout.layout_checkbox,
                fl_shop_msg, false
            ) as CheckBox
            checkbox.text = str
            checkBoxes.add(checkbox)
            fl_shop_msg.addView(checkbox)
        }
        getShopMsg()
    }

    override fun initListener() {
        tb_shop_msg.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        for (checkBox: CheckBox in checkBoxes) {
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                buttonView.setTextColor(
                    if (isChecked) Color.parseColor("#FC8A25") else Color.parseColor(
                        "#666666"
                    )
                )
            }
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_address -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PermissionUtils.permission(PermissionConstants.LOCATION)
                    .callback(object : PermissionUtils.FullCallback {
                        override fun onGranted(granted: MutableList<String>) {
                            val intent = Intent(this@ShopMsgActivity, AMapActivity::class.java)
                            intent.putExtra("longitude", longitude.toDouble())
                            intent.putExtra("latitude", latitude.toDouble())
                            startActivityForResult(intent, 102)
                        }

                        override fun onDenied(
                            deniedForever: MutableList<String>,
                            denied: MutableList<String>
                        ) {
                            if (deniedForever.size == 3 && denied.size == 3) {
                                ToastUtils.showShort("请手动开启定位权限，否则将无法正常获取定位信息！")
                            }
                        }
                    }).request()
            }
            R.id.tv_business_time -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                selBusiness()
            }
            R.id.tv_businessLicense -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, UploadPicActivity::class.java)
                intent.putExtra("url", licenseUrl)
                intent.putExtra("type", "license")
                startActivityForResult(intent, 101)
            }
            R.id.tv_doorPhoto -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, UploadPicActivity::class.java)
                intent.putExtra("url", doorPhoto)
                intent.putExtra("type", "doorTitle")
                startActivityForResult(intent, 103)
            }
            R.id.tv_shop_pic -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val urls = ArrayList<String>()
                for (bean: KmShopImageVoListBean in kmShopImageVoList) {
                    urls.add(bean.imageUrl)
                }
                val intent = Intent(this, ShopPicActivity::class.java)
                intent.putStringArrayListExtra("urls", urls)
                startActivityForResult(intent, 104)
            }
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(et_shop_name.text.toString()) -> {
                        ToastUtils.showShort("店铺名称不能为空")
                        return
                    }
//                    TextUtils.isEmpty(tv_address.text.toString()) -> {
//                        ToastUtils.showShort("店铺地址不能为空")
//                        return
//                    }
                    TextUtils.isEmpty(et_shop_phone.text.toString()) -> {
                        ToastUtils.showShort("联系电话不能为空")
                        return
                    }
//                    TextUtils.isEmpty(et_withdrawalAccount.text.toString()) -> {
//                        ToastUtils.showShort("提现账号不能为空")
//                        return
//                    }
//                    TextUtils.isEmpty(et_shopDescription.text.toString()) -> {
//                        ToastUtils.showShort("简介不能为空")
//                        return
//                    }
//                    TextUtils.isEmpty(businessBegin) -> {
//                        ToastUtils.showShort("营业时间不能为空")
//                        return
//                    }
//                    TextUtils.isEmpty(businessEnd) -> {
//                        ToastUtils.showShort("营业时间不能为空")
//                        return
//                    }
//                    checkTags() -> {
//                        ToastUtils.showShort("标签不能少于1个")
//                        return
//                    }
                    else -> {
                        save()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK)
            when (requestCode) {
                101 -> {
                    licenseUrl = data?.getStringExtra("url")!!
                    tv_businessLicense.text = "已上传"
                }
                103 -> {
                    doorPhoto = data?.getStringExtra("url")!!
                    tv_doorPhoto.text = "已上传"
                }
                104 -> {
                    val bundle: Bundle = data!!.extras!!
                    kmShopImageVoList.clear()
                    for (s: String in bundle.getStringArrayList("urls")!!) {
                        val bean = KmShopImageVoListBean()
                        bean.imageUrl = s
                        kmShopImageVoList.add(bean)
                    }
                    tv_shop_pic.text = "已上传"
                }
                102 -> {
                    val bundle = data?.extras
                    tv_address.text = bundle!!.getString("address")
                    latitude = bundle.getString("latitude")!!
                    longitude = bundle.getString("latitude")!!
                    city = bundle.getString("city")!!
                    county = bundle.getString("county")!!
                    province = bundle.getString("province")!!
                }
            }
    }

    private fun checkTags(): Boolean {
        for (checkBox: CheckBox in checkBoxes) {
            if (checkBox.isChecked)
                return false
        }
        return true
    }

    private fun checkTime(time: Int): String {
        return if (time < 10)
            "0$time"
        else
            "$time"
    }

    private fun selBusiness() {
        val popWindow = PopWindow.Builder(this)
            .setView(R.layout.layout_pop_time_picker)
            .setWidthAndHeight(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            .setOutsideTouchable(true)
            .setAnimStyle(R.style.BottomAnimation)
            .setBackGroundLevel(0.5f)
            .setChildrenView { contentView, pop ->
                val begin = contentView.findViewById<TimePicker>(R.id.timepicker)
                begin.setIs24HourView(true)
                begin.descendantFocusability = TimePicker.FOCUS_BLOCK_DESCENDANTS  //设置点击事件不弹键盘
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    begin.hour =
                        if (businessBegin.isEmpty()) 8 else businessBegin.split(Regex(":"))[0].toInt()
                    begin.minute =
                        if (businessBegin.isEmpty()) 0 else businessBegin.split(Regex(":"))[1].toInt()
                }
                val end = contentView.findViewById<TimePicker>(R.id.timepicker2)
                end.setIs24HourView(true)
                end.descendantFocusability = TimePicker.FOCUS_BLOCK_DESCENDANTS
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    end.hour =
                        if (businessEnd.isEmpty()) 18 else businessEnd.split(Regex(":"))[0].toInt()
                    end.minute =
                        if (businessEnd.isEmpty()) 0 else businessEnd.split(Regex(":"))[1].toInt()
                }
                contentView.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
                    pop.dismiss()
                }
                contentView.findViewById<TextView>(R.id.tv_sure_manual).setOnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        businessBegin =
                            "${checkTime(begin.hour)}:${checkTime(begin.minute)}"
                        businessEnd =
                            "${checkTime(end.hour)}:${checkTime(end.minute)}"
                        tv_business_time.text = "$businessBegin~$businessEnd"
                    }
                    pop.dismiss()
                }
            }
            .create()
        popWindow.isClippingEnabled = false
        popWindow.showAtLocation(rl_content, Gravity.BOTTOM, 0, 0)
    }

    private fun getShopMsg() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getShopMsg(SPUtils.getInstance().getInt("shopId"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<ShopMsgBean>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: ShopMsgBean) {
                    when (t.code) {
                        200 -> {
                            et_shop_name.setText(t.data.fullname)
                            et_shop_phone.setText(t.data.shopPhone)
                            et_shopDescription.setText(t.data.shopDescription)
                            et_operationStation.setText(t.data.operationStation.toString())
                            et_technicianNum.setText(t.data.technicianNum.toString())
                            et_withdrawalAccount.setText(t.data.withdrawalAccount)
                            tv_address.text = t.data.address
                            licenseUrl = t.data.businessLicense
                            if (!TextUtils.isEmpty(t.data.businessLicense))
                                tv_businessLicense.text = "已上传"
                            doorPhoto = t.data.doorPhoto
                            if (!TextUtils.isEmpty(t.data.doorPhoto))
                                tv_doorPhoto.text = "已上传"
                            if (t.data.kmShopImageVoList.isNotEmpty())
                                tv_shop_pic.text = "已上传"
                            businessBegin = t.data.businessBegin
                            businessEnd = t.data.businessEnd
                            if (!TextUtils.isEmpty(t.data.longitude))
                                longitude = t.data.longitude
                            if (!TextUtils.isEmpty(t.data.latitude))
                                latitude = t.data.latitude
                            city = t.data.city
                            county = t.data.county
                            province = t.data.province
                            tv_business_time.text = "$businessBegin~$businessEnd"
                            kmShopImageVoList.addAll(t.data.kmShopImageVoList)
                            var temp = arrayOf<String>()
                            if (!TextUtils.isEmpty(t.data.tags)) {
                                temp = t.data.tags.split(",").toTypedArray()
                            }
                            for (checkBox: CheckBox in checkBoxes) {
                                if (temp.isNotEmpty()) {
                                    for (str: String in temp) {
                                        if (checkBox.text.toString() == str) {
                                            checkBox.isChecked = true
                                        }
                                    }
                                }
                            }
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@ShopMsgActivity)
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
                    LogUtils.d("tag", throwable.message)
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    private fun save() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["id"] = SPUtils.getInstance().getInt("shopId")
        params["fullname"] = et_shop_name.text.toString()
        params["shopPhone"] = et_shop_phone.text.toString()
        params["shopDescription"] = et_shopDescription.text.toString()
        params["operationStation"] = et_operationStation.text.toString()
        params["technicianNum"] = et_technicianNum.text.toString()
        params["withdrawalAccount"] = et_withdrawalAccount.text.toString()
        params["address"] = tv_address.text.toString()
        params["businessLicense"] = tv_businessLicense.text.toString()
        params["businessBegin"] = businessBegin
        params["businessEnd"] = businessEnd
        params["doorPhoto"] = doorPhoto
        params["latitude"] = latitude
        params["longitude"] = longitude
        params["city"] = city
        params["county"] = county
        params["province"] = province
        params["businessLicense"] = licenseUrl
        params["kmShopImageVoList"] = kmShopImageVoList
        val stringBuilder = StringBuilder()
        for (checkBox: CheckBox in checkBoxes) {
            if (checkBox.isChecked) {
                if (stringBuilder.isNotEmpty())
                    stringBuilder.append(",")
                stringBuilder.append(checkBox.text.toString())
            }
        }
        params["tags"] = stringBuilder.toString()
        RetrofitUtils.get()
            .putJson(UpdateShopMsgUrl, params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        upDataBVDCMsg()
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }

    /**
     * http://bvdcapi.bsd128.com/bvdcPro.ashx?method=GetBVData&bvdcUserID=id&inKind=5202&inStr1=单位名称&inStr2=联系人&inStr3=地址&inStr4=手机号
     */
    private fun upDataBVDCMsg() {
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "5202"
        params["inStr1"] = et_shop_name.text.toString()
        params["inStr3"] = tv_address.text.toString()
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, UpDateBVDCResponse::class.java)
                    ToastUtils.showShort(t.message)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}