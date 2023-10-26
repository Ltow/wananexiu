package com.bossed.waej.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
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
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.kyleduo.switchbutton.SwitchButton
import kotlinx.android.synthetic.main.activity_alert.*
import java.math.BigDecimal

class BYAlertActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var byAlertAdapter: AlertAdapter
    private lateinit var unAlertAdapter: UnAlertAdapter
    private val byAlertList = ArrayList<AlertRow>()
    private var index = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_alert
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        setMarginTop(tb_alert)
        BarUtils.setStatusBarLightMode(window, true)
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
                if (DoubleClicksUtils.get().isFastDoubleClick) {
                    return
                }
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

    private val change = {
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

    private fun getAlertList(status: String) {
        LoadingUtils.showLoading(this, "加载中")
        val params = HashMap<String, String>()
        params["type"] = "2"
        params["status"] = status
        RetrofitUtils.get().getJson(
            UrlConstants.AlertUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, AlertListResponse::class.java)
                    byAlertList.clear()
                    byAlertList.addAll(t.rows)
                    when (index) {
                        0 -> {
                            byAlertAdapter = AlertAdapter(byAlertList, 0)
                            byAlertAdapter.bindToRecyclerView(rv_alert)
                            byAlertAdapter.emptyView =
                                layoutInflater.inflate(R.layout.layout_empty_view, null)
                            byAlertAdapter.setOnItemChildClickListener { adapter, view, position ->
                                when (view.id) {
                                    R.id.iv_alert -> {
                                        BottomDialog(this@BYAlertActivity).create(R.layout.layout_bottom_dialog_alert_by)
                                            .setCanceledOnTouchOutside(false)
                                            .setViewInterface { contentView, dialog ->
                                                val alertRow = byAlertList[position]
                                                contentView.findViewById<TextView>(R.id.tv_name).text =
                                                    alertRow.customerName + "     " + alertRow.customerPhone
                                                contentView.findViewById<TextView>(R.id.tv_car_no).text =
                                                    alertRow.cardNo
                                                contentView.findViewById<TextView>(R.id.tv_end_date).text =
                                                    "本次提醒日期：  ${alertRow.endDate}"
                                                val editText =
                                                    contentView.findViewById<EditText>(R.id.et_remark)
                                                val next =
                                                    contentView.findViewById<EditText>(R.id.et_next)
                                                editText.setText(alertRow.remark)
                                                val sb_continue =
                                                    contentView.findViewById<SwitchButton>(R.id.sb_continue)
                                                val tv_date_next =
                                                    contentView.findViewById<TextView>(R.id.tv_date_next)
                                                val tv_sel_date =
                                                    contentView.findViewById<TextView>(R.id.tv_sel_date)
                                                tv_sel_date.setOnClickListener {
                                                    PopupWindowUtils.get().showSelDateTimePop(
                                                        this@BYAlertActivity,
                                                        2
                                                    ) {
                                                        tv_sel_date.text =
                                                            DateFormatUtils.get().formatDate(it)
                                                    }
                                                }
                                                sb_continue.setOnCheckedChangeListener { buttonView, isChecked ->
                                                    LogUtils.d("tag", alertRow.maintenanceType)
                                                    if (alertRow.maintenanceType == 1)
                                                        contentView.findViewById<View>(R.id.ll_next).visibility =
                                                            if (isChecked) View.VISIBLE else View.GONE
                                                    if (alertRow.maintenanceType == 2)
                                                        contentView.findViewById<View>(R.id.ll_next_date).visibility =
                                                            if (isChecked) View.VISIBLE else View.GONE
                                                }
                                                next.addTextChangedListener(object :
                                                    TextChangedListener {
                                                    override fun afterTextChange(s: String) {
                                                        if (TextUtils.isEmpty(s))
                                                            return
                                                        val surplus =
                                                            BigDecimal(s).subtract(
                                                                BigDecimal(
                                                                    alertRow.maintenanceMileage
                                                                )
                                                            )
                                                        val days = surplus.divide(
                                                            BigDecimal(alertRow.maintenanceMileageDay),
                                                            0,
                                                            BigDecimal.ROUND_HALF_DOWN
                                                        )
                                                        tv_date_next.text = DateFormatUtils.get()
                                                            .formatDate(
                                                                CalendarUtils.get()
                                                                    .nowDateAddDays(days.toInt())
                                                            )
                                                    }
                                                })
                                                contentView.findViewById<TextView>(R.id.tv_name)
                                                    .setOnClickListener {
                                                        callTelPhone(byAlertList[position].customerPhone)
                                                    }
                                                contentView.findViewById<View>(R.id.tv_confirm)
                                                    .setOnClickListener {
                                                        if (sb_continue.isChecked)
                                                            when (alertRow.maintenanceType) {
                                                                1 -> {
                                                                    if (TextUtils.isEmpty(next.text.toString())) {
                                                                        ToastUtils.showShort("请输入下次保养里程")
                                                                        return@setOnClickListener
                                                                    }
                                                                    if (next.text.toString()
                                                                            .toDouble() < alertRow.maintenanceMileage
                                                                    ) {
                                                                        ToastUtils.showShort("下次保养里程不能小于上次保养里程")
                                                                        return@setOnClickListener
                                                                    }
                                                                }

                                                                2 -> {
                                                                    if (TextUtils.isEmpty(
                                                                            tv_sel_date.text.toString()
                                                                        )
                                                                    ) {
                                                                        ToastUtils.showShort("请选择下次提醒日期")
                                                                        return@setOnClickListener
                                                                    }
                                                                }
                                                            }
                                                        LoadingUtils.showLoading(
                                                            this@BYAlertActivity,
                                                            "加载中"
                                                        )
                                                        val params = HashMap<String, Any>()
                                                        params["id"] = byAlertList[position].id
                                                        params["status"] = 1
                                                        params["isRemind"] =
                                                            if (sb_continue.isChecked) 1 else 0
                                                        params["remark"] = editText.text.toString()
                                                        if (sb_continue.isChecked) {
                                                            when (alertRow.maintenanceType) {
                                                                1 -> {
                                                                    params["maintenanceMileageNext"] =
                                                                        next.text.toString()
                                                                    params["endNextDate"] =
                                                                        tv_date_next.text.toString()
                                                                }

                                                                2 -> {
                                                                    params["endNextDate"] =
                                                                        tv_sel_date.text.toString()
                                                                }
                                                            }

                                                        }
                                                        RetrofitUtils.get()
                                                            .putJson(
                                                                UrlConstants.UpdateAlertUrl,
                                                                params,
                                                                this@BYAlertActivity,
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
                                        callTelPhone(byAlertList[position].customerPhone)
                                    }
                                }
                            }
                        }

                        1 -> {
                            unAlertAdapter = UnAlertAdapter(byAlertList)
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