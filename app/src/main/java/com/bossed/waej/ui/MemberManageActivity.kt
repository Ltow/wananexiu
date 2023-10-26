package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.VipManageAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.VipCardRow
import com.bossed.waej.javebean.VipManageBean
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_member_manage.*

class MemberManageActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var memberManageAdapter: VipManageAdapter
    private val memberManageBean = ArrayList<VipCardRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_member_manage
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_member_manage)
        rv_member_manage.layoutManager = LinearLayoutManager(this)
        when (intent.getStringExtra("type")) {
            "home" -> {
                memberManageAdapter = VipManageAdapter(0, memberManageBean)
                memberManageAdapter.bindToRecyclerView(rv_member_manage)
                memberManageAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
            }
            "applet" -> {
                memberManageAdapter = VipManageAdapter(1, memberManageBean)
                memberManageAdapter.bindToRecyclerView(rv_member_manage)
                memberManageAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
                tb_member_manage.title = "会员卡维护"
                rl_bottom.visibility = View.GONE
            }
        }
    }

    override fun initListener() {
        tb_member_manage.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                val intent = Intent(this@MemberManageActivity, VipOffActivity::class.java)
                intent.putExtra("type", getIntent().getStringExtra("type"))
                startActivity(intent)
            }
        })
        memberManageAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_handle -> {
                    val intent = Intent(this, MemberHandleActivity::class.java)
                    intent.putExtra("id", memberManageBean[position].id)
                    intent.putExtra("cardNo", memberManageBean[position].cardNo)
                    startActivity(intent)
                }
                R.id.tv_off -> {
                    PopupWindowUtils.get().showConfirmPop(this, "确认将此会员卡下架？") {
                        off(position)
                    }
                }
            }
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_add_card_type -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, MemberNewActivity::class.java)
                intent.putExtra("type", "new")
                startActivity(intent)
            }
        }
    }

    private fun off(position: Int) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["id"] = memberManageBean[position].id
        params["status"] = 0
        RetrofitUtils.get()
            .putJson(
                UrlConstants.UpdateVipCardStatusUrl,
                params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        memberManageBean.clear()
                        getVipCardList()
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }

    private fun getVipCardList() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getVipCardList(2)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<VipManageBean>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: VipManageBean) {
                    when (t.code) {
                        200 -> {
                            memberManageBean.addAll(t.rows)
                            memberManageAdapter.setNewData(memberManageBean)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@MemberManageActivity)
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

    override fun onResume() {
        memberManageBean.clear()
        getVipCardList()
        super.onResume()
    }
}