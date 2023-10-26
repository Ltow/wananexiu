package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.PartSearchAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBOCRCallBack
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.PartSearchDs1
import com.bossed.waej.javebean.PartSearchResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_part_search.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class PartSearchActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var partSearchAdapter: PartSearchAdapter
    private val arrayList = ArrayList<PartSearchDs1>()
    private var brandName = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_part_search
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_part_search)
        rv_part.layoutManager = LinearLayoutManager(this)
        partSearchAdapter = PartSearchAdapter(arrayList)
        partSearchAdapter.bindToRecyclerView(rv_part)
        partSearchAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        et_vin.setText(intent.getStringExtra("vin"))
    }

    override fun initListener() {
        tb_part_search.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        partSearchAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_details -> {
                    val intent = Intent(this, OESearchActivity::class.java)
                    intent.putExtra("oe", arrayList[position].oe)
                    startActivity(intent)
                }
            }
        }
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
                when {
                    TextUtils.isEmpty(et_vin.text.toString()) -> ToastUtils.showShort("请输入vin码")
                    TextUtils.isEmpty(et_name.text.toString()) -> ToastUtils.showShort("请输入配件名称")
                    else -> PopupWindowUtils.get()
                        .getBalance(this, object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                getData()
                            }

                            override fun onCancel() {
                            }
                        })
                }
            }
        }
    }

    private fun getData() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "1401"
        params["inStr1"] = et_vin.text.toString()
        params["inStr3"] = et_name.text.toString()
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, PartSearchResponse::class.java)
                    if (!TextUtils.isEmpty(t.outStr))
                        brandName = t.outStr.split(" ")[1]
                    tv_model.text = t.outStr
                    arrayList.clear()
                    if (t.code == "1") {
                        if (t.result.ds1.isNotEmpty())
                            arrayList.addAll(t.result.ds1)
                    } else
                        ToastUtils.showShort(t.message)
                    partSearchAdapter.setNewData(arrayList)
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