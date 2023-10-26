package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.CheckAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.eventbus.EBSelectPart
import com.bossed.waej.http.UrlConstants.CheckUrl
import com.bossed.waej.javebean.PartListRow
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_check.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class CheckActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: CheckAdapter
    private val list = ArrayList<PartListRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_check
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_check)
        rv_check.layoutManager = LinearLayoutManager(this)
        adapter = CheckAdapter(list)
        adapter.bindToRecyclerView(rv_check)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        tv_date.text = DateFormatUtils.get().formatDate(TimeUtils.getNowString())
    }

    override fun initListener() {
        tb_check.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_add_part -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this, SelectPartActivity2::class.java))
            }

            R.id.tv_date -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_date.text = DateFormatUtils.get().formatDate(it)
                }
            }

            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (list.isEmpty())
                    return
                check()
            }
        }
    }

    @Subscribe
    fun onMessageEvent(eb: EBSelectPart) {
        for (row: PartListRow in eb.data) {
            if (!list.contains(row))
                list.add(row)
        }
        adapter.setNewData(list)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun check() {
        val params = HashMap<String, Any>()
        params["settleTime"] = tv_date.text.toString()
        params["status"] = "1"// 0-草稿 1-结算 2-作废
        params["remark"] = et_remark.text.toString()
        val detailList = ArrayList<HashMap<String, Any>>()
        for (row: PartListRow in list) {
            val hashMap = HashMap<String, Any>()
            hashMap["partId"] = row.id!!
            hashMap["partName"] = row.name!!
            hashMap["bookCost"] = row.partStore!!.cost
            hashMap["bookQuantity"] = row.partStore!!.quantity
            hashMap["bookAmount"] = row.partStore!!.amount
            hashMap["adjustCost"] = row.partStore!!.adjustCost
            hashMap["adjustQuantity"] = row.partStore!!.adjustQuantity
            hashMap["adjustAmount"] = row.partStore!!.adjustAmount
            detailList.add(hashMap)
        }
        params["detailList"] = detailList
        LoadingUtils.showLoading(this, "加载中...")
        RetrofitUtils.get()
            .postJson(CheckUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    ToastUtils.showShort(t.msg)
                    if (t.code == 200)
                        finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}