package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.PartListAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.PartListUrl
import com.bossed.waej.javebean.PartListResponse
import com.bossed.waej.javebean.PartListRow
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_purchase_list.*

class PartListActivity : BaseActivity(), OnRefreshLoadMoreListener, View.OnClickListener {
    private lateinit var adapter: PartListAdapter
    private val list = ArrayList<PartListRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_purchase_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_purchase_list)
        tb_purchase_list.title = "配件基本信息维护"
        tb_purchase_list.rightTitle = "新增配件"
        et_search.hint = "输入配件名称、自编码、OE号"
        rv_purchase_list.layoutManager = LinearLayoutManager(this)
        adapter = PartListAdapter(list)
        adapter.bindToRecyclerView(rv_purchase_list)
    }

    override fun initListener() {
        srl_list.setOnRefreshLoadMoreListener(this)
        tb_purchase_list.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                val intent = Intent(this@PartListActivity, PartActivity::class.java)
                intent.putExtra("type", 0)//0:新增 1:打开
                startActivity(intent)
            }
        })
        adapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(this, PartActivity::class.java)
            intent.putExtra("type", 1)
            intent.putExtra("id", list[position].id)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search -> {
                onRefresh(srl_list)
                KeyboardUtils.hideSoftInput(window)
            }
        }
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

    override fun onResume() {
        super.onResume()
        onRefresh(srl_list)
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["pageSize"] = "20"
        params["pageNum"] = pageNum.toString()
        params["searchKeywords"] = et_search.text.toString()
        RetrofitUtils.get()
            .getJson(PartListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
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

}