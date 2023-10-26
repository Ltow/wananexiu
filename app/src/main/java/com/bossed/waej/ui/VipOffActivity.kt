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
import com.bossed.waej.http.UrlConstants.UpdateVipCardStatusUrl
import com.bossed.waej.javebean.VipCardRow
import com.bossed.waej.javebean.VipManageBean
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_vip_off.*

class VipOffActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var vipManageAdapter: VipManageAdapter
    private val vipManageBean = ArrayList<VipCardRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_vip_off
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_applet_vip_off)
        rv_applet_vip_off.layoutManager = LinearLayoutManager(this)
        vipManageAdapter = VipManageAdapter(2, vipManageBean)
        vipManageAdapter.bindToRecyclerView(rv_applet_vip_off)
        when (intent.getStringExtra("type")) {
            "home" -> {
            }
            "applet" -> {
                rl_bottom.visibility = View.GONE
            }
        }
        getVipCardList()
    }

    override fun initListener() {
        tb_applet_vip_off.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        vipManageAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view?.id) {
                R.id.tv_handle -> {
                    PopupWindowUtils.get().showConfirmPop(
                        this@VipOffActivity,
                        "确认将此会员卡上架？"){
                        shelves(position)
                    }
                }
                R.id.tv_off -> {
                    val intent = Intent(this, MemberNewActivity::class.java)
                    intent.putExtra("type", "open")
                    intent.putExtra("id", vipManageBean[position].id)
                    startActivity(intent)
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

    private fun shelves(position: Int) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["id"] = vipManageBean[position].id
        params["status"] = 2
        RetrofitUtils.get()
            .putJson(UpdateVipCardStatusUrl, params, this,object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    vipManageBean.clear()
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
            .getVipCardList(0)
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
                            vipManageBean.addAll(t.rows)
                            vipManageAdapter.setNewData(vipManageBean)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@VipOffActivity)
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