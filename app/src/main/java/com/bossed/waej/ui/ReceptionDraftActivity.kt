package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.ReceptionDraftAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.eventbus.EBOpenFinish
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.JieCheDraftBean
import com.bossed.waej.javebean.JieCheRow
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_jieche_draft.*
import org.greenrobot.eventbus.EventBus

class ReceptionDraftActivity : BaseActivity(), OnNoRepeatClickListener,
    OnRefreshLoadMoreListener {
    private lateinit var receptionDraftAdapter: ReceptionDraftAdapter
    private val rows = ArrayList<JieCheRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_jieche_draft
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_jieche_draft)
        rv_jieche_draft.layoutManager = LinearLayoutManager(this)
        receptionDraftAdapter = ReceptionDraftAdapter(rows)
        receptionDraftAdapter.bindToRecyclerView(rv_jieche_draft)
    }

    override fun initListener() {
        srl_jieche_draft.setOnRefreshLoadMoreListener(this)
        tb_jieche_draft.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        receptionDraftAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_edit -> {
                    val intent = Intent(this, NewReceptionActivity::class.java)
                    intent.putExtra("id", rows[position].id)
                    intent.putExtra("orderStatus", "open")
                    startActivity(intent)
                    EventBus.getDefault().post(EBOpenFinish(true))
                }
                R.id.rl_delete -> {
                    PopupWindowUtils.get().showConfirmPop(this, "是否确认删除？") {
                        deleteOrder(rows[position].id)
                    }
                }
            }
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.rl_new_jieche -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, NewReceptionActivity::class.java)
                intent.putExtra("orderStatus", "new")
                startActivity(intent)
            }
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                LoadingUtils.showLoading(this, "加载中...")
                onRefresh(srl_jieche_draft)
            }
        }
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
                            LoadingUtils.showLoading(this@ReceptionDraftActivity, "加载中...")
                            onRefresh(srl_jieche_draft)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@ReceptionDraftActivity)
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

    private fun getOrderList() {
        val params = HashMap<String, String>()
        params["isAsc"] = ""
        params["orderByColumn"] = ""
        params["pageNum"] = pageNum.toString()
        params["pageSize"] = "10"
        params["searchKeywords"] = et_search.text.toString()
        RetrofitUtils.get()
            .getJson(
                UrlConstants.JieCheListUrl, params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        val t = GsonUtils.fromJson(s, JieCheDraftBean::class.java)
                        tv_total.text = "共${t.total}项"
                        rows.addAll(t.rows)
                        receptionDraftAdapter.setNewData(rows)
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageNum = 1
        rows.clear()
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
        onRefresh(srl_jieche_draft)
    }
}