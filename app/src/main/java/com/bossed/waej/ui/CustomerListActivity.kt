package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.CustomerListAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.CustomerListBean
import com.bossed.waej.javebean.CustomerListRow
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_customer_list.*

class CustomerListActivity : BaseActivity(), OnNoRepeatClickListener, OnRefreshLoadMoreListener {
    private var pageNum = 1
    private lateinit var customerListAdapter: CustomerListAdapter
    private val beans = ArrayList<CustomerListRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_customer_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_customer_list)
        rv_customer_list.layoutManager = LinearLayoutManager(this)
        customerListAdapter = CustomerListAdapter(beans)
        customerListAdapter.bindToRecyclerView(rv_customer_list)
        customerListAdapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        srl_customer_list.setOnRefreshLoadMoreListener(this)
        tb_customer_list.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        customerListAdapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(this, NewCustomerActivity::class.java)
            intent.putExtra("customerId", beans[position].id)
            intent.putExtra("type", "open")
            startActivity(intent)
        }
        customerListAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
//                R.id.iv_edit_item -> {
//                    val intent = Intent(this, NewCustomerActivity::class.java)
//                    intent.putExtra("customerId", beans[position].id)
//                    intent.putExtra("type", "open")
//                    startActivity(intent)
//                }
                R.id.iv_delete_item -> {
                    PopupWindowUtils.get().showConfirmPop(this, "确认删除此客户？") {
                        delete(beans[position].id)
                    }
                }
            }
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onRefresh(srl_customer_list)
            }

            R.id.tv_new_customer -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, NewCustomerActivity::class.java)
                intent.putExtra("type", "new")
                startActivity(intent)
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageNum = 1
        beans.clear()
        getCustomerList()
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getCustomerList()
        refreshLayout.finishLoadMore()
    }

    override fun onResume() {
        super.onResume()
        LoadingUtils.showLoading(this, "加载中...")
        onRefresh(srl_customer_list)
    }

    private fun delete(id: Int) {
        LoadingUtils.showLoading(this, "加载中")
        Api.getInstance().getApiService()
            .deleteCustomer(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<BaseResponse>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: BaseResponse) {
                    ToastUtils.showShort(t.msg)
                    when (t.code) {
                        200 -> {
                            onRefresh(srl_customer_list)
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@CustomerListActivity)
                        }

                        703 -> {
                            PopupWindowUtils.get()
                                .showSetConfirmAlertDialog(
                                    this@CustomerListActivity,
                                    t.msg!!,
                                    "去购买",
                                    ""
                                ) {
                                    startActivity(
                                        Intent(
                                            this@CustomerListActivity,
                                            BuyProductActivity::class.java
                                        )
                                    )
                                }
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    private fun getCustomerList() {
        val params = HashMap<String, String>()
        params["searchValue"] = et_search_customer.text.toString()
        params["pageNum"] = pageNum.toString()
        params["pageSize"] = "10"
        RetrofitUtils.get().getJson(
            UrlConstants.CustomerListUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, CustomerListBean::class.java)
                    beans.addAll(t.rows)
                    customerListAdapter.setNewData(beans)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}