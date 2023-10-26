package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.ItemMsgBean
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_item_msg.*

class ItemMsgActivity : BaseActivity(), OnNoRepeatClickListener {
    private var shopId = -1
    private var cateId = -1
    private var marketPrice = 0.0f
    private var costPrice = 0.0f
    private var madeFee = 0.0f
    private var madeMoney = 0.0f

    override fun getLayoutId(): Int {
        return R.layout.activity_item_msg
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        val layoutParams: RelativeLayout.LayoutParams =
            iv_back.layoutParams as RelativeLayout.LayoutParams
        layoutParams.topMargin = BarUtils.getStatusBarHeight()
        iv_back.layoutParams = layoutParams
        getItemMsg()
    }

    override fun initListener() {

    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }
            R.id.tv_shelves -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                shelves()
            }
        }
    }

    private fun getItemMsg() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getItemMsg(intent.getIntExtra("id", -1))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<ItemMsgBean>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: ItemMsgBean) {
                    when (t.code) {
                        200 -> {
                            shopId = t.data.shopId
                            shopId = t.data.cateId
                            marketPrice = t.data.marketPrice
                            costPrice = t.data.costPrice
                            madeFee = t.data.madeFee
                            madeMoney = t.data.madeMoney
                            tv_name.text = t.data.name
                            tv_briefIntroduction.text = t.data.briefIntroduction
                            tv_marketPrice.text = t.data.marketPrice.toString()
                            tv_virtualPrice.text = t.data.virtualPrice.toString()
                            tv_details.text = t.data.details
                            GlideUtils.get()
                                .loadItemPic(this@ItemMsgActivity, t.data.mainPicture, iv_item)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@ItemMsgActivity)
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

    private fun shelves() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = ArrayList<HashMap<String, Any>>()
        val hashMap = HashMap<String, Any>()
        hashMap["cateId"] = cateId
        hashMap["costPrice"] = costPrice
        hashMap["id"] = intent.getIntExtra("id", -1)
        hashMap["madeFee"] = madeFee
        hashMap["madeMoney"] = madeMoney
        hashMap["marketPrice"] = marketPrice
        hashMap["name"] = tv_name.text
        hashMap["shopId"] = shopId
        hashMap["status"] = 2
        params.add(hashMap)
        RetrofitUtils.get().putJson(
            UrlConstants.UpDateItemStatusUrl,
            params,this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    ToastUtils.showShort("上架成功")
                    finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}