package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.SelfPackageAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.FreeItemListUrl
import com.bossed.waej.javebean.SelfPackageResponse
import com.bossed.waej.javebean.SelfPackageRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_self_support.*

class SelfPackageActivity : BaseActivity(), View.OnClickListener, OnRefreshLoadMoreListener {
    private lateinit var adapter: SelfPackageAdapter
    private val list = ArrayList<SelfPackageRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_self_support
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_self_support)
        tb_self_support.title = "自营项目维护"
        et_search.hint = "输入商品名称检索"
        btn_commit.text = "添加服务项目"
        rv_self_support.layoutManager = LinearLayoutManager(this)
        adapter = SelfPackageAdapter(list,0)
        adapter.bindToRecyclerView(rv_self_support)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
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
                startActivity(Intent(mContext, ImportShopItemActivity::class.java))
            }
        })
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_edit -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    val intent = Intent(this, AddShopItemActivity::class.java)
                    intent.putExtra("id", list[position].id)
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
                startActivity(Intent(this, AddShopItemActivity::class.java))
            }

            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onRefresh(srl_support)
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

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["name"] = et_search.text.toString()
        params["pageSize"] = "20"
        params["pageNum"] = pageNum.toString()
        RetrofitUtils.get()
            .getJson(FreeItemListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, SelfPackageResponse::class.java)
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


}