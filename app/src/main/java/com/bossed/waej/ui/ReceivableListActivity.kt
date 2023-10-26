package com.bossed.waej.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.CopeWithListAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.ReceivableListUrl
import com.bossed.waej.javebean.CopeWithListResponse
import com.bossed.waej.javebean.Ledger
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_cope_with_list.*
import kotlinx.android.synthetic.main.layout_view_pager_item.*

class ReceivableListActivity : BaseActivity(), View.OnClickListener {
    private lateinit var copeWithListAdapter: CopeWithListAdapter
    private val copeList = ArrayList<Ledger>()

    override fun getLayoutId(): Int {
        return R.layout.activity_cope_with_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_cope_with)
        tb_cope_with.title = "应收账款总览"
        et_search.hint = "输入客户名称，姓名，电话查询"
        tv_payment.text = "本期收款"
        rv_cope_with.layoutManager = LinearLayoutManager(this)
        copeWithListAdapter = CopeWithListAdapter(copeList, 2)
        copeWithListAdapter.bindToRecyclerView(rv_cope_with)
        copeWithListAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        tv_end_date.text = DateFormatUtils.get().formatDate(TimeUtils.getNowDate())
        tv_start_date.text =
            DateFormatUtils.get().formatDate(CalendarUtils.get().nowDateAddDays(-30))
        getList()
    }

    override fun initListener() {
        tb_cope_with.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        copeWithListAdapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(this, ReceivableInfoActivity::class.java)
            intent.putExtra("id", copeList[position].id)
            intent.putExtra("startDate", tv_start_date.text.toString())
            intent.putExtra("endDate", tv_end_date.text.toString())
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                getList()
            }
            R.id.ctv_only_qm -> {
                ctv_only_qm.isChecked = !ctv_only_qm.isChecked
                getList()
            }
            R.id.tv_start_date -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_start_date.text = DateFormatUtils.get().formatDate(it)
                    tv_start_date.setTextColor(Color.parseColor("#FF3A3A"))
                    tv_start_date.setBackgroundResource(R.drawable.shape_stroke_ffa8a8_corners_fff6f6_dp13)
                    getList()
                }
            }
            R.id.tv_end_date -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_end_date.text = DateFormatUtils.get().formatDate(it)
                    tv_end_date.setTextColor(Color.parseColor("#FF3A3A"))
                    tv_end_date.setBackgroundResource(R.drawable.shape_stroke_ffa8a8_corners_fff6f6_dp13)
                    getList()
                }
            }
        }
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["search"] = et_search.text.toString()
        params["st"] = tv_start_date.text.toString()
        params["et"] = tv_end_date.text.toString()
        params["isGtZero"] = if (ctv_only_qm.isChecked) "true" else "false"
        RetrofitUtils.get()
            .getJson(ReceivableListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, CopeWithListResponse::class.java)
                    tv_moneyOwe.text = t.data.moneyOwe
                    tv_moneyAdd.text = t.data.moneyAdd
                    tv_moneyPay.text = t.data.moneyPay
                    tv_discount.text = t.data.discount
                    tv_balance.text = t.data.balance
                    copeList.clear()
                    if (t.data.ledgerList != null)
                        copeList.addAll(t.data.ledgerList)
                    copeWithListAdapter.setNewData(copeList)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}