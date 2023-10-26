package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.MaterialPriceListAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.OrderListBean
import com.bossed.waej.javebean.OrderRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.OnNoRepeatClickListener
import com.bossed.waej.util.PopupWindowUtils
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_commission.rv_commission
import kotlinx.android.synthetic.main.activity_commission.srl_commission
import kotlinx.android.synthetic.main.layout_special_title.et_search_special
import kotlinx.android.synthetic.main.layout_special_title.rl_special_title

class DispatchCommissionActivity : BaseActivity(), OnRefreshLoadMoreListener,
    OnNoRepeatClickListener {
    private lateinit var listAdapter: MaterialPriceListAdapter
    private val beans = ArrayList<OrderRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_commission
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(rl_special_title)
        et_search_special.hint = "输入车牌号"
        rv_commission.layoutManager = LinearLayoutManager(this)
        listAdapter = MaterialPriceListAdapter(beans, 1)
        listAdapter.bindToRecyclerView(rv_commission)
        listAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        srl_commission.setOnRefreshLoadMoreListener(this)
        listAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.tv_go_price) {
                val intent = Intent(this, DispatchCommissionMsgActivity::class.java)
                intent.putExtra("id", beans[position].id)
                startActivity(intent)
            }
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.iv_back ->{
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()}
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onRefresh(srl_commission)
            }
        }
    }

    private fun getOrderList() {
        Api.getInstance().getApiService()
            .getOrderList(
                et_search_special.text.toString(),
                pageNum,
                "10",
                intent.getIntExtra("orderStatus", 2),
                intent.getIntExtra("orderType", 2)
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<OrderListBean>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: OrderListBean) {
                    when (t.code) {
                        200 -> {
                            beans.addAll(t.rows)
                            listAdapter.setNewData(beans)
                        }
                        401->{
                            PopupWindowUtils.get().showLoginOutTimePop(this@DispatchCommissionActivity)
                        }
                        703 -> {
                            PopupWindowUtils.get()
                                .showSetConfirmAlertDialog(mContext, t.msg, "去购买", "") {
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
                }
            })
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageNum = 1
        beans.clear()
        getOrderList()
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getOrderList()
        refreshLayout.finishLoadMore()
    }

    override fun onResume() {
        super.onResume()
        onRefresh(srl_commission)
    }
}