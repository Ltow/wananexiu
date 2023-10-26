package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.StockInfoAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.DetailsRow
import com.bossed.waej.javebean.StockInfoResponse
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_stock_info.*

class StockInfoActivity : BaseActivity() {
    private lateinit var adapter: StockInfoAdapter
    private val list = ArrayList<DetailsRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_stock_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_stock_info)
        rv_stock_info.layoutManager = LinearLayoutManager(this)
        adapter = StockInfoAdapter(list)
        adapter.bindToRecyclerView(rv_stock_info)
        getPartInfo()
    }

    override fun initListener() {
        tb_stock_info.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    private fun getPartInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getStockInfo(intent.getStringExtra("id").toInt())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, StockInfoResponse::class.java)
                    when (t.details.code) {
                        200 -> {
                            tv_name.text = t.part.name
                            tv_model.text = t.part.model
                            tv_code.text = t.part.code
                            tv_brand.text = t.part.brand
                            tv_quantity.text = t.quantity
                            tv_cost.text = t.cost
                            tv_amount.text = t.amount
                            list.addAll(t.details.rows)
                            adapter.setNewData(list)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@StockInfoActivity)
                        }
                        else -> {
                            if (t.details.msg != null)
                                ToastUtils.showShort(t.details.msg)
                            else
                                ToastUtils.showShort("异常（代码：${t.details.code}）")
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }
}