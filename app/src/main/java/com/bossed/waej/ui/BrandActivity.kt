package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.BrandAdapter
import com.bossed.waej.adapter.SeriesAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.customview.SideBarView
import com.bossed.waej.eventbus.EBSelBrand
import com.bossed.waej.eventbus.EBSelCarModel
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.BrandDs1
import com.bossed.waej.javebean.BrandResponse
import com.bossed.waej.javebean.SeriesDs1
import com.bossed.waej.javebean.SeriesResponse
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_brand.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class BrandActivity : BaseActivity() {
    private lateinit var brandAdapter: BrandAdapter
    private val brands = ArrayList<BrandDs1>()
    private lateinit var seriesAdapter: SeriesAdapter
    private val series = ArrayList<SeriesDs1>()
    private var logo = ""
    private var brandName = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_brand
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_sel_brand)
        rv_brand.layoutManager = LinearLayoutManager(this)
        brandAdapter = BrandAdapter(brands)
        brandAdapter.bindToRecyclerView(rv_brand)
        brandAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        getBrandList("A")
        rv_series.layoutManager = LinearLayoutManager(this)
        seriesAdapter = SeriesAdapter(series)
        seriesAdapter.bindToRecyclerView(rv_series)
        seriesAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        tb_sel_brand.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        sbv_brand.setLetterTouchListener(object : SideBarView.LetterTouchListener {
            override fun setLetterVisibility(visibility: Int) {
                tv_letter_show.visibility = visibility
            }

            override fun setLetter(letter: String) {
                tv_letter.text = letter
                tv_letter_show.text = letter
                getBrandList(letter)
            }
        })
        brandAdapter.setOnItemClickListener { adapter, view, position ->
            logo = brands[position].brandIcon
            brandName = brands[position].brandName
            when (intent.getStringExtra("selType")) {
                "code" -> {
                    EventBus.getDefault()
                        .post(EBSelBrand(brands[position].brandName, brands[position].brandCode))
                    finish()
                }
                "id" -> {
                    EventBus.getDefault()
                        .post(EBSelBrand(brands[position].brandName, brands[position].brandId))
                    finish()
                }
                else -> {
                    getSeries(brands[position].brandId)
                }
            }
        }
        seriesAdapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(this, SetOfActivity::class.java)
            intent.putExtra("familyName", series[position].familyName)
            intent.putExtra("familyId", series[position].familyId)
            intent.putExtra("logo", logo)
            intent.putExtra("brandName", brandName)
            startActivity(intent)
        }
    }

    private fun getSeries(brandId: String) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "1207"//用brandId时使用
        params["inKind2"] = "2"
        params["inStr1"] = brandId
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, SeriesResponse::class.java)
                    series.clear()
                    if (t.code == "1") {
                        series.addAll(t.result.ds1)
                        dl_brand.openDrawer(Gravity.RIGHT)
                    } else
                        ToastUtils.showShort(t.message)
                    seriesAdapter.setNewData(series)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getBrandList(inStr1: String) {
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        if (intent.getStringExtra("selType") == "code")
            params["inKind"] = "1208"//用brandCode时使用
        else
            params["inKind"] = "1207"//用brandId时使用
        params["inKind2"] = "1"
        params["inStr1"] = inStr1
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BrandResponse::class.java)
                    brands.clear()
                    if (t.code == "1") {
                        if (t.result.ds1.isNotEmpty())
                            brands.addAll(t.result.ds1)
                    } else
                        ToastUtils.showShort(t.message)
                    brandAdapter.setNewData(brands)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    @Subscribe
    fun onSelCarModelCallBack(eb: EBSelCarModel) {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {
        if (dl_brand.isDrawerOpen(Gravity.RIGHT))
            dl_brand.closeDrawer(Gravity.RIGHT)
        else
            super.onBackPressed()
    }

}