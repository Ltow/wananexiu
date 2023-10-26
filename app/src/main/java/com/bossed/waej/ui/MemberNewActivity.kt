package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.MemberShopAdapter
import com.bossed.waej.adapter.MemberItemAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBSelItem
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.http.UrlConstants.NewVipCardUrl
import com.bossed.waej.javebean.*
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_member_type.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MemberNewActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var shopAdapter: MemberShopAdapter
    private val shopBean = ArrayList<ShopRow>()
    private lateinit var itemAdapter: MemberItemAdapter
    private val itemRows = ArrayList<ItemRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_member_type
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_member_type)
        rv_member_type.layoutManager = LinearLayoutManager(this)
        itemAdapter = MemberItemAdapter(itemRows)
        itemAdapter.bindToRecyclerView(rv_member_type)
        itemAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        rv_member_shop.layoutManager = LinearLayoutManager(this)
        shopAdapter = MemberShopAdapter(shopBean)
        shopAdapter.bindToRecyclerView(rv_member_shop)
        getShopList()
        when (intent.getStringExtra("type")) {
            "new" -> {
            }
            "open" -> {
                getVipMsg()
            }
        }
    }

    override fun initListener() {
        tb_member_type.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        itemAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.iv_delete_item) {
                itemRows.removeAt(position)
                itemAdapter.setNewData(itemRows)
            }
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_add_give -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this, SelectMemberItemActivity::class.java))
            }
            R.id.tv_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when (intent.getStringExtra("type")) {
                    "new" -> {
                        newVipCard()
                    }
                    "open" -> {
                        updateVipCard()
                    }
                }
            }
            R.id.tv_confirm -> {

            }
        }
    }

    private fun getVipMsg() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getVipCardMsg(intent.getIntExtra("id", 0))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<VipCardMsgBean>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: VipCardMsgBean) {
                    when (t.code) {
                        200 -> {
                            et_cardName.setText(t.data.cardName)
                            et_marketPrice.setText(t.data.marketPrice.toString())
                            et_giveAmount.setText(t.data.giveAmount.toString())
                            et_remark.setText(t.data.remark)
                            for (item: KmShopClubCardItems in t.data.kmShopClubCardItemsList) {
                                itemRows.add(
                                    ItemRow(
                                        0,
                                        0f,
                                        "",
                                        "",
                                        item.id,
                                        0f,
                                        0f,
                                        0f,
                                        item.itemName,
                                        "",
                                        "",
                                        0,
                                        item.shopId,
                                        0,
                                        item.tenantId,
                                        0,
                                        "",
                                        "",
                                        item.availableQuantity,
                                        false
                                    )
                                )
                            }
                            itemAdapter.setNewData(itemRows)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@MemberNewActivity)
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

    private fun newVipCard() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["cardName"] = et_cardName.text.toString()
        params["marketPrice"] = et_marketPrice.text.toString()
        params["giveAmount"] = et_giveAmount.text.toString()
        params["remark"] = et_remark.text.toString()
        params["status"] = 0
        val items = ArrayList<HashMap<String, Any>>()
        for (item: ItemRow in itemRows) {
            val hashMap = HashMap<String, Any>()
            hashMap["itemName"] = item.name
            hashMap["id"] = item.id
            hashMap["shopId"] = item.shopId
            hashMap["tenantId"] = item.tenantId
            hashMap["availableQuantity"] = item.availableQuantity
            items.add(hashMap)
        }
        params["kmShopClubCardItemsList"] = items
        RetrofitUtils.get()
            .postJson(NewVipCardUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun updateVipCard() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["cardName"] = et_cardName.text.toString()
        params["marketPrice"] = et_marketPrice.text.toString()
        params["giveAmount"] = et_giveAmount.text.toString()
        params["remark"] = et_remark.text.toString()
        params["id"] = intent.getIntExtra("id", 0)
        params["status"] = 0
        val items = ArrayList<HashMap<String, Any>>()
        for (item: ItemRow in itemRows) {
            val hashMap = HashMap<String, Any>()
            hashMap["itemName"] = item.name
            hashMap["id"] = item.id
            hashMap["shopId"] = item.shopId
            hashMap["tenantId"] = item.tenantId
            hashMap["availableQuantity"] = item.availableQuantity
            items.add(hashMap)
        }
        params["kmShopClubCardItemsList"] = items
        RetrofitUtils.get()
            .putJson(
                UrlConstants.UpdateVipCardUrl,
                params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        ToastUtils.showShort("更新成功")
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }

    private fun getShopList() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getShopList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<MemberShopBean>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: MemberShopBean) {
                    when (t.code) {
                        200 -> {
                            shopBean.addAll(t.rows)
                            shopAdapter.setNewData(shopBean)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@MemberNewActivity)
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

    @Subscribe
    fun onSelItemCallBack(eb: EBSelItem) {
        itemRows.addAll(eb.sel)
        itemAdapter.setNewData(itemRows)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}