package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.AlertAdapter
import com.bossed.waej.adapter.UnAlertAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.customview.BottomDialog
import com.bossed.waej.javebean.AlertListResponse
import com.bossed.waej.javebean.AlertRow
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.OnNoRepeatClickListener
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.kyleduo.switchbutton.SwitchButton
import kotlinx.android.synthetic.main.activity_alert.*

class NJAlertActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var njAlertAdapter: AlertAdapter
    private lateinit var unAlertAdapter: UnAlertAdapter
    private val njAlertList = ArrayList<AlertRow>()
    private var index = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_alert
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        setMarginTop(tb_alert)
        BarUtils.setStatusBarLightMode(window, true)
        tb_alert.title = "年检提醒"
        rv_alert.layoutManager = LinearLayoutManager(this)
    }

    override fun initListener() {
        tb_alert.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.tv_tag_w -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                index = 0
                change()
            }

            R.id.tv_tag_y -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                index = 1
                change()
            }
        }
    }

    private fun change() {
        getAlertList(index.toString())
        tv_tag_w.setBackgroundResource(R.drawable.shape_corners_cccccc_dp12_5)
        tv_tag_y.setBackgroundResource(R.drawable.shape_corners_cccccc_dp12_5)
        when (index) {
            0 -> {
                tv_tag_w.setBackgroundResource(R.drawable.shape_corners_3477fc_dp_12_5)
            }

            1 -> {
                tv_tag_y.setBackgroundResource(R.drawable.shape_corners_3477fc_dp_12_5)
            }
        }
    }

    private val getAlertList = { status: String ->
        LoadingUtils.showLoading(this, "加载中")
        val params = HashMap<String, String>()
        params["type"] = "3"
        params["status"] = status
        RetrofitUtils.get().getJson(
            UrlConstants.AlertUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, AlertListResponse::class.java)
                    njAlertList.clear()
                    njAlertList.addAll(t.rows)
                    when (index) {
                        0 -> {
                            njAlertAdapter = AlertAdapter(njAlertList, 2)
                            njAlertAdapter.bindToRecyclerView(rv_alert)
                            njAlertAdapter.emptyView =
                                layoutInflater.inflate(R.layout.layout_empty_view, null)
                            njAlertAdapter.setOnItemChildClickListener { adapter, view, position ->
                                when (view.id) {
                                    R.id.iv_alert -> {
                                        BottomDialog(this@NJAlertActivity).create(R.layout.layout_bottom_dialog_alert_nj)
                                            .setCanceledOnTouchOutside(false)
                                            .setViewInterface { contentView, dialog ->
                                                val alertRow = njAlertList[position]
                                                contentView.findViewById<TextView>(R.id.tv_name).text =
                                                    alertRow.customerName + "     " + alertRow.customerPhone
                                                contentView.findViewById<TextView>(R.id.tv_car_no).text =
                                                    alertRow.cardNo
                                                contentView.findViewById<TextView>(R.id.tv_end_date).text =
                                                    "年检到期日期：${alertRow.endDate}"
                                                val editText =
                                                    contentView.findViewById<EditText>(R.id.et_remark)
                                                editText.setText(alertRow.remark)
                                                val switch =
                                                    contentView.findViewById<SwitchButton>(R.id.sb_continue)
                                                contentView.findViewById<TextView>(R.id.tv_name)
                                                    .setOnClickListener {
                                                        callTelPhone(njAlertList[position].customerPhone)
                                                    }
                                                contentView.findViewById<View>(R.id.tv_confirm)
                                                    .setOnClickListener {
                                                        LoadingUtils.showLoading(
                                                            this@NJAlertActivity,
                                                            "加载中"
                                                        )
                                                        val params = HashMap<String, Any>()
                                                        params["id"] = njAlertList[position].id
                                                        params["status"] = 1
                                                        params["isRemind"] =
                                                            if (switch.isChecked) 1 else 0
                                                        params["remark"] = editText.text.toString()
                                                        RetrofitUtils.get()
                                                            .putJson(
                                                                UrlConstants.UpdateAlertUrl,
                                                                params,
                                                                this@NJAlertActivity,
                                                                object :
                                                                    RetrofitUtils.OnCallBackListener {
                                                                    override fun onSuccess(s: String) {
                                                                        val t = GsonUtils.fromJson(
                                                                            s,
                                                                            BaseResponse::class.java
                                                                        )
                                                                        ToastUtils.showShort(t.msg)
                                                                        if (t.code == 200) {
                                                                            dialog.dismiss()
                                                                            change()
                                                                        }
                                                                    }

                                                                    override fun onFailed(e: String) {
                                                                        ToastUtils.showShort(e)
                                                                    }
                                                                })
                                                    }
                                                contentView.findViewById<View>(R.id.tv_cancel)
                                                    .setOnClickListener { dialog.dismiss() }
                                            }.show()
                                    }

                                    R.id.tv_name -> {
                                        callTelPhone(njAlertList[position].customerPhone)
                                    }
                                }
                            }
                        }

                        1 -> {
                            unAlertAdapter = UnAlertAdapter(njAlertList)
                            unAlertAdapter.bindToRecyclerView(rv_alert)
                            unAlertAdapter.emptyView =
                                layoutInflater.inflate(R.layout.layout_empty_view, null)
                        }
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        change()
    }
}