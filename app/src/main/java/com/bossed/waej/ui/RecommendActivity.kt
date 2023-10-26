package com.bossed.waej.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.RecommendAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBOCRCallBack
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.RecommendDs1
import com.bossed.waej.javebean.RecommendResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_recommend.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class RecommendActivity : BaseActivity(), OnNoRepeatClickListener {
    private val allItems = ArrayList<RecommendDs1>()
    private val showItems = ArrayList<RecommendDs1>()
    private lateinit var recommendAdapter: RecommendAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_recommend
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_recommend_search)
        rv_recommend.layoutManager = LinearLayoutManager(this)
        recommendAdapter = RecommendAdapter(showItems)
        recommendAdapter.bindToRecyclerView(rv_recommend)
        recommendAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        et_vin.setText(intent.getStringExtra("vin"))
    }

    override fun initListener() {
        tb_recommend_search.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        recommendAdapter.setOnItemClickListener { adapter, view, position ->
            showItems[position].isSel = !showItems[position].isSel
            LogUtils.d("tag", position, showItems[position].isSel)
            recommendAdapter.setNewData(showItems)
            for (ds: RecommendDs1 in allItems) {
                if (ds.保养项目ID == showItems[position].保养项目ID)
                    ds.isSel = showItems[position].isSel
            }
        }
        recommendAdapter.setOnSelCheckedListener(object : OnSelCheckedListener {
            override fun onChange(position: Int, isChecked: Boolean) {
                showItems[position].isSel = isChecked
                for (ds: RecommendDs1 in allItems) {
                    if (ds.保养项目ID == showItems[position].保养项目ID)
                        ds.isSel = showItems[position].isSel
                }
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
            R.id.ctv_recommend -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_recommend.isChecked = !ctv_recommend.isChecked
                if (ctv_recommend.isChecked)
                    for (ds: RecommendDs1 in showItems)
                        ds.isSel = true
                else
                    for (ds: RecommendDs1 in showItems)
                        ds.isSel = false
                for (all: RecommendDs1 in allItems)
                    for (ds: RecommendDs1 in showItems)
                        if (all.保养项目ID == ds.保养项目ID)
                            all.isSel = ds.isSel
                recommendAdapter.setNewData(showItems)
            }
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(et_vin.text.toString()) -> ToastUtils.showShort("请输入vin码")
                    TextUtils.isEmpty(et_mileage.text.toString()) -> ToastUtils.showShort("请输入行驶里程")
                    else -> PopupWindowUtils.get()
                        .getBalance(this, object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                getCarData()
                            }

                            override fun onCancel() {
                            }
                        })
                }
            }
            R.id.tv_renewal -> {//更换
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switchTab(0)
            }
            R.id.tv_check -> {//检查
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switchTab(1)
            }
            R.id.tv_create -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val sel = ArrayList<RecommendDs1>()
                for (all: RecommendDs1 in allItems) {
                    if (all.isSel)
                        sel.add(all)
                }
                if (sel.isEmpty()) {
                    ToastUtils.showShort("请选择项目后再点击生成工单")
                    return
                }
                val intent = Intent(this, NewOrderActivity::class.java)
                intent.putExtra("orderType", "new")
                intent.putExtra("id", "")
                intent.putParcelableArrayListExtra("items", sel)
                intent.putExtra("vin", et_vin.text.toString())
                intent.putExtra("model", tv_brand.text.toString())
                startActivity(intent)
                finish()
            }
        }
    }

    private fun switchTab(type: Int) {
        switchData(type)
        tv_renewal.setBackgroundResource(R.drawable.shape_tab_unsel_bvdc)
        tv_renewal.setTextColor(Color.parseColor("#333333"))
        tv_check.setBackgroundResource(R.drawable.shape_tab_unsel_bvdc)
        tv_check.setTextColor(Color.parseColor("#333333"))
        when (type) {
            0 -> {
                tv_renewal.setBackgroundResource(R.drawable.shape_tab_sel_bvdc)
                tv_renewal.setTextColor(Color.parseColor("#3477FC"))
            }
            1 -> {
                tv_check.setBackgroundResource(R.drawable.shape_tab_sel_bvdc)
                tv_check.setTextColor(Color.parseColor("#3477FC"))
            }
        }
    }

    private fun switchData(type: Int) {
        showItems.clear()
        when (type) {
            0 -> {
                for (ds: RecommendDs1 in allItems) {
                    if (ds.更换标识 == "1")
                        showItems.add(ds)
                }
            }
            1 -> {
                for (ds: RecommendDs1 in allItems) {
                    if (ds.更换标识 == "0")
                        showItems.add(ds)
                }
            }
        }
        recommendAdapter.setNewData(showItems)
    }

    private fun getCarData() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "1301"
        params["inStr1"] = et_vin.text.toString()
        params["inStr3"] = "0"
        params["inStr4"] = et_mileage.text.toString()
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, RecommendResponse::class.java)
                    ctv_recommend.isChecked = false
                    allItems.clear()
                    if (t.code == "1") {
                        tv_brand.text = t.outStr
                        if (t.result == null)
                            ToastUtils.showShort("查无数据")
                        else
                            allItems.addAll(t.result.ds1!!)
                        switchTab(0)
                    } else
                        ToastUtils.showShort(t.message)
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