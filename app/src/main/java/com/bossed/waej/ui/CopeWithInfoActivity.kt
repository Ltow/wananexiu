package com.bossed.waej.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.CopeWithInfoAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.PaymentInfoUrl
import com.bossed.waej.javebean.ReceivableInfoResponse
import com.bossed.waej.javebean.SupplierBillRow
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_cope_with_info.*
import kotlinx.android.synthetic.main.layout_view_pager_item.*

class CopeWithInfoActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: CopeWithInfoAdapter
    private val list = ArrayList<SupplierBillRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_cope_with_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_cope_with_info)
        tv_payment.text = "本期付款"
        tv_start_date.text = intent.getStringExtra("startDate")
        tv_end_date.text = intent.getStringExtra("endDate")
        rv_cope_with_info.layoutManager = LinearLayoutManager(this)
        adapter = CopeWithInfoAdapter(list)
        adapter.bindToRecyclerView(rv_cope_with_info)
        getInfo()
    }

    override fun initListener() {
        tb_cope_with_info.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_order_id -> {
                    //跳付款单详情界面
                    if (!TextUtils.isEmpty(list[position].businessOrderSn)) {
                        val intent = Intent(this, PickBySupplierInfoActivity::class.java)
                        intent.putExtra("type", "2")
                        intent.putExtra("id", list[position].businessOrderSn)
                        startActivity(intent)
                        return@setOnItemChildClickListener
                    }
                    val intent = Intent(this, PaymentInfoActivity::class.java)
                    intent.putExtra("id", list[position].id.toString())
                    startActivity(intent)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_start_date -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_start_date.text = DateFormatUtils.get().formatDate(it)
                    tv_start_date.setTextColor(Color.parseColor("#FF3A3A"))
                    tv_start_date.setBackgroundResource(R.drawable.shape_stroke_ffa8a8_corners_fff6f6_dp13)
                    getInfo()
                }
            }
            R.id.tv_end_date -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_end_date.text = DateFormatUtils.get().formatDate(it)
                    tv_end_date.setTextColor(Color.parseColor("#FF3A3A"))
                    tv_end_date.setBackgroundResource(R.drawable.shape_stroke_ffa8a8_corners_fff6f6_dp13)
                    getInfo()
                }
            }
        }
    }

    private fun getInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["userId"] = intent.getStringExtra("id")
        params["st"] = tv_start_date.text.toString()
        params["et"] = tv_end_date.text.toString()
        RetrofitUtils.get()
            .getJson(PaymentInfoUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, ReceivableInfoResponse::class.java)
                    tv_supplierName.text = t.data.supplierName
                    tv_lxr_name.text = t.data.name
                    tv_phone.text = t.data.phone
                    tv_moneyOwe.text = t.data.moneyOwe
                    tv_moneyAdd.text = t.data.moneyAdd
                    tv_moneyPay.text = t.data.moneyPay
                    tv_discount.text = t.data.discount
                    tv_balance.text = t.data.balance
                    list.clear()
                    list.addAll(t.data.supplierBillList.rows)
                    adapter.setNewData(list)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}