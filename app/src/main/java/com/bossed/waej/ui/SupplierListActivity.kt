package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.SupplierListAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.SupplierListResponse
import com.bossed.waej.javebean.SupplierRow
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.DoubleClicksUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_select_supplier.*

class SupplierListActivity : BaseActivity(), View.OnClickListener, OnRefreshLoadMoreListener {
    private lateinit var supplierListAdapter: SupplierListAdapter
    private val supplierBean = ArrayList<SupplierRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_select_supplier
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_supplier)
        rv_select_supplier.layoutManager = LinearLayoutManager(this)
        supplierListAdapter = SupplierListAdapter(supplierBean)
        supplierListAdapter.bindToRecyclerView(rv_select_supplier)
        supplierListAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        if (intent.getStringExtra("type") == "1")
            ll_bottom.visibility = View.VISIBLE
    }

    override fun initListener() {
        tb_supplier.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        supplierListAdapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(this, SupplierMsgActivity::class.java)
            intent.putExtra("openType", "msg")
            intent.putExtra("id", supplierBean[position].id)
            startActivity(intent)
        }
        srl_supplier.setOnRefreshLoadMoreListener(this)
    }

    private fun getSupplierList() {
        Api.getInstance().getApiService()
            .getSupplierList(et_search.text.toString(), pageNum, "20")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, SupplierListResponse::class.java)
                    when (t.code) {
                        200 -> {
                            supplierBean.addAll(t.rows)
                            supplierListAdapter.setNewData(supplierBean)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@SupplierListActivity)
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
        supplierBean.clear()
        getSupplierList()
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getSupplierList()
        refreshLayout.finishLoadMore()
    }

    override fun onResume() {
        super.onResume()
        LoadingUtils.showLoading(this, "加载中...")
        onRefresh(srl_supplier)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_new_supplier -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, SupplierMsgActivity::class.java)
                intent.putExtra("openType", "new")
                startActivity(intent)
            }
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                LoadingUtils.showLoading(this, "加载中...")
                onRefresh(srl_supplier)
            }
            R.id.tv_skip -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this, RegisterSuccessActivity::class.java))
                finish()
            }
            R.id.tv_next -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, BankAccountUpholdActivity::class.java)
                intent.putExtra("type", "1")
                startActivity(intent)
            }
        }
    }
}