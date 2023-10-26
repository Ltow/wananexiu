package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.SelectPartAdapter2
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBSelectPart
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.PartList2Response
import com.bossed.waej.javebean.PartListRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_select_part2.*
import org.greenrobot.eventbus.EventBus

class SelectPartActivity2 : BaseActivity(), OnRefreshLoadMoreListener, View.OnClickListener {
    private lateinit var adapter: SelectPartAdapter2
    private val list = ArrayList<PartListRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_select_part2
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_sel_part)
        rv_part.layoutManager = LinearLayoutManager(this)
        adapter = SelectPartAdapter2(list)
        adapter.bindToRecyclerView(rv_part)
        adapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
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
                val intent = Intent(this@SelectPartActivity2, PartActivity::class.java)
                intent.putExtra("type", 0)//0:新增 1:打开
                startActivity(intent)
            }
        })
        adapter.setOnItemClickListener { adapter, view, position ->
            list[position].isSelected = !list[position].isSelected
            adapter.setNewData(list)
            checkSelAll()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ctv_all -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_all.isChecked = !ctv_all.isChecked
                for (row: PartListRow in list) {
                    row.isSelected = ctv_all.isChecked
                }
                adapter.setNewData(list)
            }
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onRefresh(srl_sel_part)
            }
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val sel = ArrayList<PartListRow>()
                for (row: PartListRow in list) {
                    if (row.isSelected)
                        sel.add(row)
                }
                if (sel.isEmpty())
                    return
                EventBus.getDefault().post(EBSelectPart(sel))
                finish()
            }
        }
    }

    private fun checkSelAll() {
        var isAll = true
        for (row: PartListRow in list) {
            if (!row.isSelected) {
                isAll = false
                break
            }
        }
        ctv_all.isChecked = isAll
    }

    override fun onResume() {
        super.onResume()
        onRefresh(srl_sel_part)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageNum = 1
        list.clear()
        getList()
        refreshLayout.finishRefresh()
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
                UrlConstants.PartListStoreUrl, params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        LogUtils.d("tag", s)
                        val t = GsonUtils.fromJson(s, PartList2Response::class.java)
                        list.addAll(t.rows)
                        adapter.setNewData(list)
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }
}