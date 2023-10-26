package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.NoticeListAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.NoticeListBean
import com.bossed.waej.javebean.NoticeRow
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.OnNoRepeatItemClickListener
import com.bossed.waej.util.PopupWindowUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_notice_list.*

class NoticeListActivity : BaseActivity() {
    private lateinit var listAdapter: NoticeListAdapter
    private val listBean = ArrayList<NoticeRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_notice_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_notice_list)
        rv_notice_list.layoutManager = LinearLayoutManager(this)
        listAdapter = NoticeListAdapter(listBean)
        listAdapter.bindToRecyclerView(rv_notice_list)
        listAdapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        getNoticeList()
    }

    override fun initListener() {
        tb_notice_list.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        listAdapter.onItemClickListener = object : OnNoRepeatItemClickListener {
            override fun onNoRepeatItemItemClick(
                adapter: BaseQuickAdapter<*, *>?,
                view: View?,
                position: Int
            ) {
                val intent = Intent(this@NoticeListActivity, NoticeMsgActivity::class.java)
                intent.putExtra("id", listBean[position].noticeId)
                startActivity(intent)
            }
        }
    }

    private fun getNoticeList() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getNoticeList("", "2")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<NoticeListBean>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: NoticeListBean) {
                    when (t.code) {
                        200 -> {
                            listBean.addAll(t.rows)
                            listAdapter.setNewData(listBean)
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@NoticeListActivity)
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
                    LoadingUtils.dismissLoading()
                }
            })
    }
}