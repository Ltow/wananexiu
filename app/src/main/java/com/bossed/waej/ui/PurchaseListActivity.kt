package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.PurchaseListAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.UrlConstants.PurchaseFinishUrl
import com.bossed.waej.http.UrlConstants.PurchaseListUrl
import com.bossed.waej.javebean.PurchaseListResponse
import com.bossed.waej.javebean.PurchaseListRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_purchase_list.*

class PurchaseListActivity : BaseActivity(), View.OnClickListener, OnRefreshLoadMoreListener {
    private lateinit var adapter: PurchaseListAdapter
    private val list = ArrayList<PurchaseListRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_purchase_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_purchase_list)
        rv_purchase_list.layoutManager = LinearLayoutManager(this)
        adapter = PurchaseListAdapter(list)
        adapter.bindToRecyclerView(rv_purchase_list)
        adapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
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
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this@PurchaseListActivity, PurchaseActivity::class.java))
            }
        })
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_edit -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    val intent = Intent(this@PurchaseListActivity, PurchaseActivity::class.java)
                    intent.putExtra("id", list[position].id)
                    startActivity(intent)
                }
                R.id.iv_delete -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get().showConfirmPop(this, "确认作废此单据？") {
                        LoadingUtils.showLoading(this, "加载中...")
                        val params = HashMap<String, Any>()
                        params["id"] = list[position].id
                        params["type"] = "1"// 1-进货 2-退回
                        params["status"] = 2//0-草稿 1-结算 2-作废
                        params["supplierId"] = list[position].supplierId
                        params["supplierName"] = list[position].supplierName
                        params["contacts"] = list[position].contacts
                        params["contactsPhone"] = list[position].contactsPhone
                        params["quantity"] = list[position].quantity
                        params["amount"] = list[position].amount
                        RetrofitUtils.get()
                            .putJson(
                                PurchaseFinishUrl, params, this,
                                object : RetrofitUtils.OnCallBackListener {
                                    override fun onSuccess(s: String) {
                                        LogUtils.d("tag", s)
                                        val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                                        ToastUtils.showShort(t.msg)
                                        onRefresh(srl_list)
                                    }

                                    override fun onFailed(e: String) {
                                        ToastUtils.showShort(e)
                                    }
                                })
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onRefresh(srl_list)
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
        params["searchKeywords"] = et_search.text.toString()
        params["pageNum"] = pageNum.toString()
        params["pageSize"] = "20"
        params["type"] = "0"//1-进货 2-退回 0-所有
        params["status"] = "0"// 0-草稿 1-结算 2-作废
        RetrofitUtils.get()
            .getJson(PurchaseListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, PurchaseListResponse::class.java)
                    list.addAll(t.rows)
                    adapter.setNewData(list)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}