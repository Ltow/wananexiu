package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.CheckInfoAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.CheckInfoDataDetailsRow
import com.bossed.waej.javebean.CheckInfoResponse
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_check_info.*

class CheckInfoActivity : BaseActivity() {
    private lateinit var adapter: CheckInfoAdapter
    private val list = ArrayList<CheckInfoDataDetailsRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_check_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_check_info)
        rv_check_info.layoutManager = LinearLayoutManager(this)
        adapter = CheckInfoAdapter(list)
        adapter.bindToRecyclerView(rv_check_info)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        getInfo()
    }

    override fun initListener() {
        tb_check_info.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    private fun getInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getCheckInfo(intent.getStringExtra("id").toInt())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, CheckInfoResponse::class.java)
                    when (t.code) {
                        200 -> {
                            tv_order_id.text = t.data.orderSn
                            tv_date.text = t.data.settleTime
                            tv_remark.text = t.data.remark
                            tv_czy.text = t.data.createBy
                            list.addAll(t.data.details.rows)
                            adapter.setNewData(list)
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@CheckInfoActivity)
                        }

                        else -> {
                            if (t.msg != null)
                                ToastUtils.showShort(t.msg)
                            else
                                ToastUtils.showShort("异常（代码：${t.code}）")
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