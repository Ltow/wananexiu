package com.bossed.waej.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.bossed.waej.R
import com.hjq.bar.TitleBar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.*
import com.bossed.waej.adapter.InformationAdapter
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.*
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import kotlin.collections.ArrayList
import android.view.WindowManager
import android.widget.*
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.blankj.utilcode.constant.PermissionConstants

abstract class BaseActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var mActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        //适配全面屏
        val lp: WindowManager.LayoutParams = this.window.attributes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        this.window.attributes = lp
        //隐藏导航栏
        WindowInsetsControllerCompat(
            window, window.decorView
        ).hide(WindowInsetsCompat.Type.navigationBars())
        if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        super.onCreate(savedInstanceState)
        mContext = this.applicationContext
        mActivity = this
        setContentView(getLayoutId())
        initView(savedInstanceState)
        initListener()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        currentFocus?.apply {
            if (this is EditText) {
                if (ev.action == MotionEvent.ACTION_DOWN) {
                    if (isShouldHideKeyboard(this, ev)) {
                        this.clearFocus()
                        KeyboardUtils.hideSoftInput(window)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     *  加载布局
     */
    abstract fun getLayoutId(): Int

    open fun initView(savedInstanceState: Bundle?) {}
    open fun initListener() {}

    // 实例化presenter
    protected open fun initPresenter() {}

    @SuppressLint("HardwareIds", "MissingPermission")
    open fun getIMEI(context: Context): String {
        val tm = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> Settings.System.getString(
                contentResolver, Settings.Secure.ANDROID_ID
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                tm.imei
            }
            else -> {
                tm.deviceId
            }
        }
    }

    open val callTelPhone = { phone: String ->
        if (PermissionUtils.isGranted(PermissionConstants.PHONE)) {
            val intent = Intent(
                Intent.ACTION_CALL,
                Uri.parse("tel:$phone")
            )
            startActivity(intent)
        } else {
            PermissionUtils.permission(PermissionConstants.PHONE)
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        val intent =
                            Intent(
                                Intent.ACTION_CALL,
                                Uri.parse("tel:$phone")
                            )
                        startActivity(intent)
                    }

                    override fun onDenied() {
                        ToastUtils.showShort("请在系统设置中开启拨号权限")
                    }
                }).request()
        }
    }

    /**
     * 动态设置titleBar距顶部一个状态栏的高度（px）
     */
    fun setMarginTop(view: View) {
        when (view.parent) {
            is LinearLayout -> {
                val layoutParams: LinearLayout.LayoutParams =
                    view.layoutParams as LinearLayout.LayoutParams
                layoutParams.topMargin = BarUtils.getStatusBarHeight()
                view.layoutParams = layoutParams
            }
            else -> {
                val layoutParams: RelativeLayout.LayoutParams =
                    view.layoutParams as RelativeLayout.LayoutParams
                layoutParams.topMargin = BarUtils.getStatusBarHeight()
                view.layoutParams = layoutParams
            }
        }
    }

    fun setTextColorAndSize(textView: TextView, color: String, textSize: Float) {
        textView.setTextColor(Color.parseColor(color))
        textView.textSize = textSize
    }

    fun buttonClickState(isChecked: Boolean, textView: TextView) {
        if (isChecked) {
            textView.setBackgroundResource(R.drawable.shape_blue_gradient_bg)
            textView.setTextColor(Color.parseColor("#ffffff"))
            textView.isEnabled = true
        } else {
            textView.setBackgroundResource(R.drawable.shape_stroke_cccccc_dp6)
            textView.setTextColor(Color.parseColor("#999999"))
            textView.isEnabled = false
        }
    }

    open fun getCalendarForDateAndTime(str: String): Calendar {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date: Date? = null
        try {
            date = sdf.parse(str)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar
    }

    open fun getCalendarForDate(str: String): Calendar {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var date: Date? = null
        try {
            date = sdf.parse(str)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar
    }

    /**
     * 判断是否点击的是EditText以外的区域
     */
    private fun isShouldHideKeyboard(v: View, event: MotionEvent): Boolean {
        if (v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return (event.x <= left || event.x >= right
                    || event.y <= top || event.y >= bottom)
        }
        return false
    }

    /**
     * 根据vin码获取车型和id
     */
    fun getVinData(vin: String, listener: OnGetVinBackListener) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "1201"
        params["inStr1"] = vin
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, VinSearchResponse::class.java)
                    if (t.code == "1") {
                        listener.onBack(t.result.ds1[0].销售车型名称, t.result.ds1[0].车型ID)
                    } else
                        ToastUtils.showShort(t.message)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    /**
     * 根据精友项目id获取4s店价格和工时费
     */
    fun getInformation(
        vehicleId: String,
        itemId: String,
        view: View,
        listener: OnShow4SPriceListener
    ) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "1302"
        params["inStr1"] = vehicleId
        params["inStr2"] = itemId
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, RecommendInformation::class.java)
                    if (t.code == "1") {
                        when {
                            t.result.ds1.isNotEmpty() -> {
                                if (t.result.ds1.size > 1) {
                                    val arrayList = ArrayList<InformationBean>()
                                    for (ds1: RecommendInformationDs1 in t.result.ds1) {
                                        arrayList.add(
                                            InformationBean(
                                                ds1.partNum,
                                                ds1.oe,
                                                ds1.guidePrice,
                                                ""
                                            )
                                        )
                                    }
                                    showSel4SPrice(view, arrayList, t.outStr, listener)
                                } else
                                    listener.onBack(
                                        t.result.ds1[0].guidePrice,
                                        t.outStr,
                                        t.result.ds1[0].oe,
                                        t.result.ds1[0].partNum
                                    )
                            }
                            t.result.ds2.isNotEmpty() -> {
                                if (t.result.ds2.size > 1) {
                                    val arrayList = ArrayList<InformationBean>()
                                    for (ds2: RecommendInformationDs2 in t.result.ds2) {
                                        arrayList.add(
                                            InformationBean(
                                                ds2.amount,
                                                ds2.oilType + "\b" + ds2.viscosityGrade,
                                                ds2.unitPrice,
                                                ds2.unit
                                            )
                                        )
                                    }
                                    showSel4SPrice(view, arrayList, t.outStr, listener)
                                } else
                                    listener.onBack(
                                        t.result.ds2[0].unitPrice,
                                        t.outStr,
                                        t.result.ds2[0].oilType + "\b" + t.result.ds2[0].viscosityGrade,
                                        t.result.ds2[0].amount + "\b" + t.result.ds2[0].unit
                                    )
                            }
                            else -> ToastUtils.showShort("查无数据")
                        }
                    } else
                        ToastUtils.showShort(t.message)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    /**
     * 多个价格时选择一个
     */
    private fun showSel4SPrice(
        view: View,
        arrayList: ArrayList<InformationBean>,
        fee: String,
        listener: OnShow4SPriceListener
    ) {
        val popWindow = PopWindow.Builder(this)
            .setView(R.layout.layout_pop_sel_4s_price)
            .setWidthAndHeight(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            .setOutsideTouchable(true)
            .setAnimStyle(R.style.BottomAnimation)
            .setBackGroundLevel(0.5f)
            .setChildrenView { contentView, pop ->
                val recyclerView = contentView.findViewById<RecyclerView>(R.id.rv_4s_item)
                recyclerView.layoutManager = LinearLayoutManager(this@BaseActivity)
                val adapter = InformationAdapter(arrayList)
                adapter.bindToRecyclerView(recyclerView)
                adapter.setOnItemClickListener { _, _, position ->
                    listener.onBack(
                        arrayList[position].sPrice,
                        fee,
                        arrayList[position].oe,
                        arrayList[position].num + "\b" + arrayList[position].unit
                    )
                    pop.dismiss()
                }
            }
            .create()
        popWindow.isClippingEnabled = false
        popWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)
    }

    /**
     * 根据配件查oe号
     */
    fun getOEData(
        vin: String,
        name: String,
        listener: (oe: String, price: String) -> Unit
    ) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "1401"
        params["inStr1"] = vin
        params["inStr3"] = name
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, PartSearchResponse::class.java)
                    if (t.code == "1") {
                        PopupWindowUtils.get()
                            .showSelOEPop(
                                this@BaseActivity, t.result.ds1 as ArrayList, listener
                            )
                    } else {
                        ToastUtils.showShort(t.message)
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }


    interface OnGetVinBackListener {
        fun onBack(model: String, id: String)
    }

    interface OnShow4SPriceListener {
        fun onBack(price: String, fee: String, oe: String, num: String)
    }
}