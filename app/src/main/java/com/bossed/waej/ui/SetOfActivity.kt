package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.ModelAdapter
import com.bossed.waej.adapter.SetOfAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBSelCarModel
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.ModelDs1
import com.bossed.waej.javebean.ModelResponse
import com.bossed.waej.javebean.SetOfDs1
import com.bossed.waej.javebean.SetOfResponse
import com.bossed.waej.util.GlideUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_car_displacement.*
import org.greenrobot.eventbus.EventBus

class SetOfActivity : BaseActivity() {
    private val setOfList = ArrayList<SetOfDs1>()

    override fun getLayoutId(): Int {
        return R.layout.activity_car_displacement
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_car_displacement)
        tb_car_displacement.title = "请选择车组"
        tv_title.text = "请选择车组"
        tv_scan_car.visibility = View.GONE
        tv_car_brand.text =
            intent.getStringExtra("brandName") + "-->" + intent.getStringExtra("familyName")
        GlideUtils.get().loadCarLogo(this, intent.getStringExtra("logo")!!, iv_car_logo)
        rv_car_displacement.layoutManager = LinearLayoutManager(this)
        getSetOfData()
    }

    override fun initListener() {
        tb_car_displacement.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    private fun getSetOfData() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "1207"//用brandId时使用
        params["inKind2"] = "3"
        params["inStr1"] = intent.getStringExtra("familyId")!!
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, SetOfResponse::class.java)
                    if (t.code == "1") {
                        setOfList.addAll(t.result.ds1)
                        val setOfAdapter = SetOfAdapter(setOfList)
                        setOfAdapter.bindToRecyclerView(rv_car_displacement)
                        setOfAdapter.setOnItemClickListener { adapter, view, position ->
                            tv_car_brand.text =
                                intent.getStringExtra("brandName") + "-->" + intent.getStringExtra("familyName") + "-->" + setOfList[position].groupName
                            tb_car_displacement.title = "请选择车型"
                            tv_title.text = "请选择车型"
                            getModelData(setOfList[position].groupId)
                            setOfList.clear()
                            setOfAdapter.setNewData(setOfList)
                        }
                    } else
                        ToastUtils.showShort(t.message)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }


    private fun getModelData(groupId: String) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "1207"//用brandId时使用
        params["inKind2"] = "4"
        params["inStr1"] = groupId
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d(s)
                    val t = GsonUtils.fromJson(s, ModelResponse::class.java)
                    if (t.code == "1") {
                        val modelAdapter = ModelAdapter(t.result.ds1 as MutableList<ModelDs1>)
                        modelAdapter.bindToRecyclerView(rv_car_displacement)
                        modelAdapter.setOnItemClickListener { adapter, view, position ->
                            EventBus.getDefault().post(
                                EBSelCarModel(
                                    t.result.ds1[position].车型名称, t.result.ds1[position].品牌名称,
                                    intent.getStringExtra("logo")!!, t.result.ds1[position].vehicleId
                                )
                            )
                            finish()
                        }
                    } else
                        ToastUtils.showShort(t.message)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}