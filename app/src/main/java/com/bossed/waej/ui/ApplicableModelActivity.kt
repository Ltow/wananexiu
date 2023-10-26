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
import com.bossed.waej.adapter.ApplicableAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBSelBrand
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.ApplicableDs1
import com.bossed.waej.javebean.ApplicableResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_applicable_model.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ApplicableModelActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var applicableAdapter: ApplicableAdapter
    private val arrayList = ArrayList<ApplicableDs1>()
    private var inStr1 = ""//品牌id

    override fun getLayoutId(): Int {
        return R.layout.activity_applicable_model
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_model_search)
        rv_applicable.layoutManager = LinearLayoutManager(this)
        applicableAdapter = ApplicableAdapter(arrayList)
        applicableAdapter.bindToRecyclerView(rv_applicable)
        applicableAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        et_oem.setText(intent.getStringExtra("oe"))
        inStr1 = intent.getStringExtra("brandId")!!
        tv_brand.text = intent.getStringExtra("brandName")
    }

    override fun initListener() {
        tb_model_search.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick) {
                    return
                }
                when {
                    TextUtils.isEmpty(et_oem.text.toString()) -> ToastUtils.showShort("请输入OE号")
                    TextUtils.isEmpty(tv_brand.text.toString()) -> ToastUtils.showShort("请选择车辆品牌")
                    else -> PopupWindowUtils.get()
                        .getBalance(this, object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                getModelData()
                            }

                            override fun onCancel() {
                            }
                        })
                }
            }
            R.id.tv_brand -> {
                if (DoubleClicksUtils.get().isFastDoubleClick) {
                    return
                }
                val intent = Intent(this, BrandActivity::class.java)
                intent.putExtra("selType", "code")
                startActivity(intent)
            }
        }
    }

    private fun getModelData() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "1601"
        params["inStr1"] = inStr1
        params["inStr2"] = et_oem.text.toString()
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, ApplicableResponse::class.java)
                    arrayList.clear()
                    if (t.code == "1") {
                        if (t.result.ds1.isNotEmpty())
                            arrayList.addAll(t.result.ds1)
                        else
                            ToastUtils.showShort("查无数据")
                    } else
                        ToastUtils.showShort(t.message)
                    applicableAdapter.setNewData(arrayList)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    @Subscribe
    fun onMessageEvent(eb: EBSelBrand) {
        tv_brand.text = eb.brandName
        inStr1 = eb.brandId
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}