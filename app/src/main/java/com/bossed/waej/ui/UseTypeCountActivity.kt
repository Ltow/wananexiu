package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.UseTypeAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.UseTypeDs1
import com.bossed.waej.javebean.UseTypeResponse
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_use_type.*

class UseTypeCountActivity : BaseActivity() {
    private val arrayList = ArrayList<UseTypeDs1>()
    private lateinit var useTypeAdapter: UseTypeAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_use_type
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_use_type)
        rv_use_type.layoutManager = LinearLayoutManager(this)
        useTypeAdapter = UseTypeAdapter(arrayList)
        useTypeAdapter.bindToRecyclerView(rv_use_type)
        getData()
    }

    override fun initListener() {
        tb_use_type.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    private fun getData() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "2201"
        params["inKind2"] = "21"
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, UseTypeResponse::class.java)
                    arrayList.clear()
                    if (t.code == "1") {
                        arrayList.addAll(t.result.ds1)
                    } else
                        ToastUtils.showShort(t.message)
                    useTypeAdapter.setNewData(arrayList)
                    countTotal()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun countTotal() {
        var total = 0.0
        for (ds1: UseTypeDs1 in arrayList) {
            total += ds1.金额.toFloat()
        }
        tv_total.text = String.format("%.2f", total)
    }
}