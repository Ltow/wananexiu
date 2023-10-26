package com.bossed.waej.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.*
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.OnSelCheckedListener
import com.bossed.waej.util.RetrofitUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class RecommendAdapter(data: MutableList<RecommendDs1>) :
    BaseQuickAdapter<RecommendDs1, BaseViewHolder>(R.layout.layout_item_recommend, data) {
    private var listener: OnSelCheckedListener? = null
    private val arrayList = ArrayList<InformationBean>()

    fun setOnSelCheckedListener(listener: OnSelCheckedListener) {
        this.listener = listener
    }

    override fun convert(helper: BaseViewHolder, item: RecommendDs1) {
        helper.setText(R.id.tv_name, item.保养项目名称)
            .setChecked(R.id.cb_sel_item, item.isSel)
        helper.getView<CheckBox>(R.id.cb_sel_item)
            .setOnCheckedChangeListener { buttonView, isChecked ->
                listener!!.onChange(helper.adapterPosition, isChecked)
            }
        val textView = helper.getView<TextView>(R.id.tv_get)
        val recyclerView = helper.getView<RecyclerView>(R.id.rv_information)
        val adapter = InformationAdapter(arrayList)
        adapter.bindToRecyclerView(recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        textView.setOnClickListener {
            LoadingUtils.showLoading(mContext as Activity, "加载中...")
            val params = HashMap<String, String>()
            params["method"] = "GetBVData"
            params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
            params["inKind"] = "1302"
            params["inStr1"] = item.vehicleId
            params["inStr2"] = item.保养项目ID
            params["setName"] = getTelNo(mContext)
            params["Czy"] = SPUtils.getInstance().getString("username")
            RetrofitUtils.get().getBVDCJson(
                UrlConstants.BvdcUrl,
                params,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        val t = GsonUtils.fromJson(s, RecommendInformation::class.java)
                        if (t.code == "1") {
                            textView.text = "工时费:￥${t.outStr}"
                            textView.setTextColor(Color.parseColor("#333333"))
                            textView.isEnabled = false
                            recyclerView.visibility = View.VISIBLE
                            arrayList.clear()
                            when {
                                t.result.ds1.isNotEmpty() -> for (ds1: RecommendInformationDs1 in t.result.ds1) {
                                    arrayList.add(
                                        InformationBean(
                                            ds1.partNum,
                                            ds1.oe,
                                            ds1.guidePrice,
                                            ""
                                        )
                                    )
                                }
                                t.result.ds2.isNotEmpty() -> for (ds2: RecommendInformationDs2 in t.result.ds2) {
                                    arrayList.add(
                                        InformationBean(
                                            ds2.amount,
                                            ds2.oilType + "\b" + ds2.viscosityGrade,
                                            ds2.unitPrice,
                                            ds2.unit
                                        )
                                    )
                                }
                                else -> ToastUtils.showShort("查无数据")
                            }
                            adapter.setNewData(arrayList)
                        } else {
                            ToastUtils.showShort(t.message)
                        }
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
        }
    }

    @SuppressLint("HardwareIds", "MissingPermission")
    private fun getTelNo(context: Context): String {
        val tm = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> "TEl" + Settings.System.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                "TEL" + tm.imei
            }
            else -> {
                "TEL" + tm.deviceId
            }
        }
    }
}