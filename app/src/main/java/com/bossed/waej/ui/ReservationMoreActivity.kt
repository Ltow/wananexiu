package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.ReservationMoreAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.RecordListUrl
import com.bossed.waej.javebean.RecordListResponse
import com.bossed.waej.javebean.RecordListRow
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_reservation_more.*

class ReservationMoreActivity : BaseActivity(), View.OnClickListener {
    private lateinit var morningAdapter: ReservationMoreAdapter
    private val morningList = ArrayList<RecordListRow>()
    private lateinit var afternoonAdapter: ReservationMoreAdapter
    private val afternoonList = ArrayList<RecordListRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_reservation_more
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_reservation_more)
        rv_morning.layoutManager = LinearLayoutManager(this)
        rv_afternoon.layoutManager = LinearLayoutManager(this)
        morningAdapter = ReservationMoreAdapter(morningList)
        morningAdapter.bindToRecyclerView(rv_morning)
        morningAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        afternoonAdapter = ReservationMoreAdapter(afternoonList)
        afternoonAdapter.bindToRecyclerView(rv_afternoon)
        afternoonAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        tb_reservation_more.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        morningAdapter.setOnItemClickListener { adapter, view, position ->
            if (DoubleClicksUtils.get().isFastDoubleClick)
                return@setOnItemClickListener
            val intent = Intent(this, ReservationInfoActivity::class.java)
            intent.putExtra("id", morningList[position].id)
            startActivity(intent)
        }
        afternoonAdapter.setOnItemClickListener { adapter, view, position ->
            if (DoubleClicksUtils.get().isFastDoubleClick)
                return@setOnItemClickListener
            val intent = Intent(this, ReservationInfoActivity::class.java)
            intent.putExtra("id", afternoonList[position].id)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tev_today -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                change(0)
            }
            R.id.tev_tomorrow -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                change(1)
            }
            R.id.tev_the_day_after_tomorrow -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                change(2)
            }
            R.id.iv_morning -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                rv_morning.visibility = if (rv_morning.isVisible) View.GONE else View.VISIBLE
                iv_morning.setImageResource(if (rv_morning.isVisible) R.mipmap.icon_close_down else R.mipmap.icon_open_up)
            }
            R.id.iv_afternoon -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                rv_afternoon.visibility = if (rv_afternoon.isVisible) View.GONE else View.VISIBLE
                iv_afternoon.setImageResource(if (rv_afternoon.isVisible) R.mipmap.icon_close_down else R.mipmap.icon_open_up)
            }
        }
    }

    private fun change(i: Int) {
        tev_today.setBackgroundResource(R.drawable.shape_corners_cecece_8_8_0_0dp)
        tev_tomorrow.setBackgroundResource(R.drawable.shape_corners_cecece_8_8_0_0dp)
        tev_the_day_after_tomorrow.setBackgroundResource(R.drawable.shape_corners_cecece_8_8_0_0dp)
        when (i) {
            0 -> {
                tev_today.setBackgroundResource(R.drawable.shape_corners_3477fc_8_8_0_0dp)
                getList(DateFormatUtils.get().formatDate(CalendarUtils.get().nowDateAddDays(0)))
            }
            1 -> {
                tev_tomorrow.setBackgroundResource(R.drawable.shape_corners_3477fc_8_8_0_0dp)
                getList(DateFormatUtils.get().formatDate(CalendarUtils.get().nowDateAddDays(1)))
            }
            2 -> {
                tev_the_day_after_tomorrow.setBackgroundResource(R.drawable.shape_corners_3477fc_8_8_0_0dp)
                getList(DateFormatUtils.get().formatDate(CalendarUtils.get().nowDateAddDays(2)))
            }
        }
    }

    private fun getList(beginTime: String) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["beginTime"] = beginTime
        RetrofitUtils.get()
            .getJson(RecordListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, RecordListResponse::class.java)
                    afternoonList.clear()
                    morningList.clear()
                    for (i in t.rows.indices) {
                        val time: String = t.rows[i].beginTime
                        val timeArr = time.split(" ".toRegex()).toTypedArray()
                        val timeHourArr = timeArr[1].split(":".toRegex()).toTypedArray()
                        if (timeHourArr.isNotEmpty()) {
                            val timeHour = timeHourArr[0]
                            val hour = Integer.valueOf(timeHour)
                            if (hour >= 12)
                                afternoonList.add(t.rows[i])
                            else
                                morningList.add(t.rows[i])
                        }
                    }
                    morningAdapter.setNewData(morningList)
                    afternoonAdapter.setNewData(afternoonList)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        change(0)
    }
}