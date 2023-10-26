package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.OESearchAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.HistoryBean
import com.bossed.waej.javebean.OESearchDs1
import com.bossed.waej.javebean.OESearchResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_oe_search.*
import java.util.HashMap

class OESearchActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var oeSearchAdapter: OESearchAdapter
    private val arrayList = ArrayList<OESearchDs1>()
    private var flows = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_oe_search
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_oe_search)
        rv_oe.layoutManager = LinearLayoutManager(this)
        oeSearchAdapter = OESearchAdapter(arrayList)
        oeSearchAdapter.bindToRecyclerView(rv_oe)
        oeSearchAdapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        et_oem.setText(intent.getStringExtra("oe"))
        if (TextUtils.isEmpty(SPUtils.getInstance().getString("oeSearchHistory")))
            return
        flows = GsonUtils.fromJson(
            SPUtils.getInstance().getString("oeSearchHistory"),
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
                et_oem.setText(textView.text.toString())
                getOEData()
            }
            flex_layout.addView(layout)
        }
    }

    override fun initListener() {
        tb_oe_search.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        oeSearchAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_brand -> {
                    val intent = Intent(this, ApplicableModelActivity::class.java)
                    intent.putExtra("oe", arrayList[position].oe)
                    intent.putExtra("brandId", arrayList[position].brandCode)
                    intent.putExtra("brandName", arrayList[position].车型品牌名称)
                    startActivity(intent)
                }

                R.id.tv_replace -> {
                    val intent = Intent(this, ReplaceActivity::class.java)
                    intent.putExtra("oe", arrayList[position].oe)
                    intent.putExtra("brandId", arrayList[position].brandID)
                    intent.putExtra("brandName", arrayList[position].车型品牌名称)
                    startActivity(intent)
                }
            }
        }

    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (TextUtils.isEmpty(et_oem.text.toString()))
                    ToastUtils.showShort("请输入OE号")
                else
                    PopupWindowUtils.get()
                        .getBalance(this, object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                getOEData()
                                flows.forEach {
                                    if (it == et_oem.text.toString())
                                        return
                                }
                                flows.add(et_oem.text.toString())
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
                                        et_oem.setText(textView.text.toString())
                                        getOEData()
                                    }
                                    flex_layout.addView(layout)
                                }
                                ll_history.visibility = View.VISIBLE
                                SPUtils.getInstance()
                                    .put("oeSearchHistory", GsonUtils.toJson(flows))
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
                    .put("oeSearchHistory", GsonUtils.toJson(flows))
                flex_layout.removeAllViews()
            }
        }
    }

    private fun getOEData() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "1101"
        params["inStr1"] = et_oem.text.toString()
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, OESearchResponse::class.java)
                    arrayList.clear()
                    if (t.code == "1")
                        if (t.result == null)
                            ToastUtils.showShort("查无数据")
                        else
                            arrayList.addAll(t.result.ds1)
                    else
                        ToastUtils.showShort(t.message)
                    oeSearchAdapter.setNewData(arrayList)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}