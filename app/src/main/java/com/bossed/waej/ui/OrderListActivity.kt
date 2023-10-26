package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.OrderListAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.OrderListBean
import com.bossed.waej.javebean.OrderRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.OnNoRepeatClickListener
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_service_order.et_search
import kotlinx.android.synthetic.main.activity_service_order.rv_service_order
import kotlinx.android.synthetic.main.activity_service_order.srl_order_service
import kotlinx.android.synthetic.main.activity_service_order.tb_onder_draft

class OrderListActivity : BaseActivity(), OnNoRepeatClickListener, OnRefreshLoadMoreListener {
    private lateinit var orderAdapter: OrderListAdapter
    private val orderBean = ArrayList<OrderRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_service_order
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_onder_draft)
        rv_service_order.layoutManager = LinearLayoutManager(this)
        orderAdapter = OrderListAdapter(0, orderBean)
        orderAdapter.bindToRecyclerView(rv_service_order)
        orderAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        srl_order_service.setOnRefreshLoadMoreListener(this)
        tb_onder_draft.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        orderAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_edit -> {
                    val intent = Intent(this, NewOrderActivity::class.java)
                    intent.putExtra("orderType", "open")
                    intent.putExtra("id", orderBean[position].id)
                    startActivity(intent)
                }
//                R.id.tv_delete -> {
//                    PopupWindowUtils.get().showConfirmPop(this, "确认删除此单据？", object :
//                        PopupWindowUtils.OnConfirmListener {
//                        override fun onConfirm() {
//                            deleteOrder(orderBean[position].id)
//                        }
//                    })
//                }
            }
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }
            R.id.rl_new_order -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, NewOrderActivity::class.java)
                intent.putExtra("orderType", "new")
                startActivity(intent)
            }
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                LoadingUtils.showLoading(this, "加载中...")
                onRefresh(srl_order_service)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        LoadingUtils.showLoading(this, "加载中..")
        onRefresh(srl_order_service)
    }

    private fun deleteOrder(id: Int) {
        LoadingUtils.showLoading(this, "加载中")
        Api.getInstance().getApiService()
            .deleteOrder(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<BaseResponse>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: BaseResponse) {
                    when (t.code) {
                        200 -> {
                            onRefresh(srl_order_service)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@OrderListActivity)
                        }
                        703 -> {
                            PopupWindowUtils.get()
                                .showSetConfirmAlertDialog(mContext, t.msg!!, "去购买", "") {
                                    mContext.startActivity(
                                        Intent(
                                            mContext,
                                            BuyProductActivity::class.java
                                        )
                                    )
                                }
                        }
                        else -> {
                            ToastUtils.showShort(t.msg)
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    private fun getOrderListList() {
        val params = HashMap<String, String>()
        params["searchValue"] = et_search.text.toString()
        params["pageNum"] = pageNum.toString()
        params["pageSize"] = "10"
        params["orderStatus"] = "0"
        params["orderType"] = "2"
        RetrofitUtils.get().getJson(
            UrlConstants.OrderListUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, OrderListBean::class.java)
                    if (t.rows.isNotEmpty())
                        orderBean.addAll(t.rows)
                    orderAdapter.setNewData(orderBean)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageNum = 1
        orderBean.clear()
        getOrderListList()
//        orderAdapter.onRefresh()
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getOrderListList()
        refreshLayout.finishLoadMore()
    }
}