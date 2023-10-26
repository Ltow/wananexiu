package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.SelfSupportAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.GoodsListUrl
import com.bossed.waej.javebean.SelfSupportResponse
import com.bossed.waej.javebean.SelfSupportRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_self_support.*

class SelfSupportActivity : BaseActivity(), View.OnClickListener, OnRefreshLoadMoreListener {
    private lateinit var adapter: SelfSupportAdapter
    private val list = ArrayList<SelfSupportRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_self_support
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_self_support)
        tb_self_support.rightTitle = ""
        rv_self_support.layoutManager = LinearLayoutManager(this)
        adapter = SelfSupportAdapter(list,0)
        adapter.bindToRecyclerView(rv_self_support)
        adapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        srl_support.setOnRefreshLoadMoreListener(this)
        tb_self_support.setOnTitleBarListener(object : OnTitleBarListener {
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
                R.id.iv_edit -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    val intent = Intent(this, SelfSupportInfoActivity::class.java)
                    intent.putExtra("id", list[position].id)
                    intent.putExtra("shopId", getIntent().getStringExtra("shopId"))
                    startActivity(intent)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_commit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent=Intent(this, SelfSupportInfoActivity::class.java)
                intent.putExtra("shopId", getIntent().getStringExtra("shopId"))
                startActivity(intent)
            }
        }
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["pageSize"] = "20"
        params["pageNum"] = pageNum.toString()
        params["name"] = et_search.text.toString()
        RetrofitUtils.get()
            .getJson(GoodsListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, SelfSupportResponse::class.java)
                    list.addAll(t.rows)
                    adapter.setNewData(list)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        onRefresh(srl_support)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        list.clear()
        pageNum = 1
        getList()
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getList()
        refreshLayout.finishLoadMore()
    }

}