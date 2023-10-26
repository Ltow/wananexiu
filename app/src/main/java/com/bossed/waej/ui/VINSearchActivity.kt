package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.adapter.VINSearchAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBOCRCallBack
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.HistoryBean
import com.bossed.waej.javebean.VinSearchBean
import com.bossed.waej.javebean.VinSearchResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_vin_search.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class VINSearchActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var vinSearchAdapter: VINSearchAdapter
    private val arrayList = ArrayList<VinSearchBean>()
    private var flows = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_vin_search
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_vin_search)
        rv_vin.layoutManager = LinearLayoutManager(this)
        vinSearchAdapter = VINSearchAdapter(arrayList)
        vinSearchAdapter.bindToRecyclerView(rv_vin)
        vinSearchAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        if (TextUtils.isEmpty(SPUtils.getInstance().getString("vinSearchHistory")))
            return
        flows = GsonUtils.fromJson(
            SPUtils.getInstance().getString("vinSearchHistory"),
            HistoryBean::class.java
        )
        if (flows.size > 0)
            ll_history.visibility = View.VISIBLE
        flows.forEach {
            val layout = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_search_history, null)
            val textView = layout.findViewById<TextView>(R.id.tv_his)
            textView.text = it
            textView.setOnClickListener {
                et_vin.setText(textView.text.toString())
                getVinData()
            }
            flex_layout.addView(layout)
        }
    }

    override fun initListener() {
        tb_vin_search.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.ic_scan -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, OCRScanActivity::class.java)
                intent.putExtra("ocrType", 2)
                startActivity(intent)
            }
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (TextUtils.isEmpty(et_vin.text.toString()))
                    ToastUtils.showShort("请输入vin码")
                else
                    PopupWindowUtils.get()
                        .getBalance(this, object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                getVinData()
                                flows.forEach {
                                    if (it == et_vin.text.toString())
                                        return
                                }
                                flows.add(et_vin.text.toString())
                                if (flows.size > 10) {
                                    flows.removeAt(0)
                                }
                                flex_layout.removeAllViews()
                                flows.forEach {
                                    val layout = LayoutInflater.from(mContext)
                                        .inflate(R.layout.layout_search_history, null)
                                    val textView = layout.findViewById<TextView>(R.id.tv_his)
                                    textView.text = it
                                    textView.setOnClickListener {
                                        et_vin.setText(textView.text.toString())
                                        getVinData()
                                    }
                                    flex_layout.addView(layout)
                                }
                                ll_history.visibility = View.VISIBLE
                                SPUtils.getInstance()
                                    .put("vinSearchHistory", GsonUtils.toJson(flows))
                            }

                            override fun onCancel() {
                            }
                        })
            }
            R.id.tv_clear -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ll_history.visibility = View.GONE
                flows.clear()
                SPUtils.getInstance()
                    .put("vinSearchHistory", GsonUtils.toJson(flows))
                flex_layout.removeAllViews()
            }
            R.id.tv_ -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, RecommendActivity::class.java)
                intent.putExtra("vin", et_vin.text.toString())
                startActivity(intent)
            }
            R.id.tv_book -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, BookActivity::class.java)
                intent.putExtra("vin", et_vin.text.toString())
                startActivity(intent)
            }
            R.id.tv_part -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, PartSearchActivity::class.java)
                intent.putExtra("vin", et_vin.text.toString())
                startActivity(intent)
            }
        }
    }

    private fun getVinData() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "1201"
        params["inStr1"] = et_vin.text.toString()
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, VinSearchResponse::class.java)
                    arrayList.clear()
                    if (t.code == "1") {
                        ll_search.visibility = View.VISIBLE
                        if (t.result != null && t.result.ds1.isNotEmpty()) {
                            arrayList.add(VinSearchBean(t.result.ds1[0].销售车型名称, "销售车型名称"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].国产进口, "国产进口"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].品牌名称, "品牌名称"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].车系名称, "车系名称"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].车组名称, "车组名称"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].年款, "年款"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].排量, "排量"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].车身颜色, "车身颜色"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].上市时间, "上市时间"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].发动机型号, "发动机型号"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].发动机生产企业, "发动机生产企业"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].变速器档数, "变速器档数"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].变速箱类型, "变速箱类型"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].停产日期, "停产日期"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].排放标准, "排放标准"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].燃油标号, "燃油标号"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].供油方式, "供油方式"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].燃油喷射形式, "燃油喷射形式"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].气缸, "气缸"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].驱动形式, "驱动形式"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].动力类型, "动力类型"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].外形尺寸, "外形尺寸"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].整备质量, "整备质量"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].总质量, "总质量"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].前轮胎规格, "前轮胎规格"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].后轮胎规格, "后轮胎规格"))
                            arrayList.add(VinSearchBean(t.result.ds1[0].新车购置价, "新车购置价"))
                        }
                    } else {
                        ll_search.visibility = View.GONE
                        ToastUtils.showShort(t.message)
                    }
                    vinSearchAdapter.setNewData(arrayList)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    @Subscribe
    fun onMessageEvent(eb: EBOCRCallBack) {
        et_vin.setText(eb.vin)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}