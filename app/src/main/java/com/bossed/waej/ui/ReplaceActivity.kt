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
import com.bossed.waej.adapter.ReplaceAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBSelBrand
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.ReplaceDs1
import com.bossed.waej.javebean.ReplaceResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_replace.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ReplaceActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var replaceAdapter: ReplaceAdapter
    private val arrayList = ArrayList<ReplaceDs1>()
    private var inStr1 = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_replace
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_replace_search)
        rv_replace.layoutManager = LinearLayoutManager(this)
        replaceAdapter = ReplaceAdapter(arrayList)
        replaceAdapter.bindToRecyclerView(rv_replace)
        replaceAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        et_oem.setText(intent.getStringExtra("oe"))
        inStr1 = intent.getStringExtra("brandId")!!
        tv_brand.text = intent.getStringExtra("brandName")
    }

    override fun initListener() {
        tb_replace_search.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.tv_brand -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, BrandActivity::class.java)
                intent.putExtra("selType", "id")
                startActivity(intent)
            }
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(et_oem.text.toString()) -> ToastUtils.showShort("请输入oe号")
                    TextUtils.isEmpty(tv_brand.text.toString()) -> ToastUtils.showShort("请选择品牌")
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
        params["inKind"] = "1501"
        params["inStr1"] = inStr1
        params["inStr2"] = et_oem.text.toString()
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, ReplaceResponse::class.java)
                    arrayList.clear()
                    if (t.code == "1") {
                        if (t.result.ds1.isNotEmpty())
                            arrayList.addAll(t.result.ds1)
                    } else
                        ToastUtils.showShort(t.message)
                    replaceAdapter.setNewData(arrayList)
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