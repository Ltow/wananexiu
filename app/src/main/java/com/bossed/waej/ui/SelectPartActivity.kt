package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.SelectPartAdapter
import com.bossed.waej.adapter.SelectedPartAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.eventbus.EBSelectPart
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.PartListResponse
import com.bossed.waej.javebean.PartListRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_select_part.*
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal

/**
 * 配件选择器--不带库存
 */
class SelectPartActivity : BaseActivity(), View.OnClickListener, OnRefreshLoadMoreListener {
    private lateinit var adapter: SelectPartAdapter
    private val list = ArrayList<PartListRow>()
    private var pageNum = 1
    private val selList = ArrayList<PartListRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_select_part
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_sel_part)
        rv_part.layoutManager = LinearLayoutManager(this)
        adapter = SelectPartAdapter(list, 1)
        adapter.bindToRecyclerView(rv_part)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        srl_sel_part.setOnRefreshLoadMoreListener(this)
        tb_sel_part.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                val intent = Intent(this@SelectPartActivity, PartActivity::class.java)
                intent.putExtra("type", 0)//0:新增 1:打开
                startActivity(intent)
            }
        })
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_reduce -> {
                    KeyboardUtils.hideSoftInput(window)
                    list[position].num = list[position].num!! - 1
                    adapter.setNewData(list)
                    count()
                }

                R.id.iv_add -> {
                    KeyboardUtils.hideSoftInput(window)
                    list[position].num = list[position].num!! + 1
                    adapter.setNewData(list)
                    count()
                }
            }
        }
        adapter.setOnChangeListener {
            count()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onRefresh(srl_sel_part)
            }

            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                EventBus.getDefault().post(EBSelectPart(selList))
                finish()
            }

            R.id.ll_selected -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (selList.isEmpty())
                    return
                val popWindow =
                    PopWindow.Builder(this).setView(R.layout.layout_pop_selected_part)
                        .setWidthAndHeight(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                        ).setOutsideTouchable(true)
                        .setAnimStyle(R.style.BottomAnimation)
                        .setBackGroundLevel(0.5f)
                        .setChildrenView { contentView, pop ->
                            val recyclerView =
                                contentView.findViewById<RecyclerView>(R.id.rv_selected_part)
                            recyclerView.layoutManager = LinearLayoutManager(mContext)
                            val selAdapter = SelectedPartAdapter(selList)
                            selAdapter.bindToRecyclerView(recyclerView)
                            selAdapter.emptyView =
                                layoutInflater.inflate(R.layout.layout_empty_view, null)
                            contentView.findViewById<View>(R.id.iv_close)
                                .setOnClickListener { pop.dismiss() }
                        }.create()
                popWindow.setOnDismissListener {
                    ll_selected.isEnabled = true
                }
                popWindow.isClippingEnabled = false
                popWindow.isFocusable = true
                popWindow.showAtLocation(
                    rl_content,
                    Gravity.BOTTOM,
                    0,
                    0
                )
                ll_selected.isEnabled = false
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageNum = 1
        list.clear()
        getList()
        refreshLayout.finishRefresh()
        count()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getList()
        refreshLayout.finishLoadMore()
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["pageSize"] = "20"
        params["pageNum"] = pageNum.toString()
        params["searchKeywords"] = et_search.text.toString()
        RetrofitUtils.get()
            .getJson(
                UrlConstants.PartListUrl, params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        LogUtils.d("tag", s)
                        val t = GsonUtils.fromJson(s, PartListResponse::class.java)
                        list.addAll(t.rows)
                        adapter.setNewData(list)
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }

    private fun count() {
        var total = BigDecimal(0.0)
        selList.clear()
        for (row: PartListRow in list) {
            if (row.num!! > 0)
                selList.add(row)
            total = total.add(
                BigDecimal(row.purchasePrice).multiply(BigDecimal(row.num!!))
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN)
            )
        }
        tv_total.text = "${total}元"
        tv_total_num.text = "共${selList.size}种"
    }

    override fun onResume() {
        super.onResume()
        onRefresh(srl_sel_part)
    }
}