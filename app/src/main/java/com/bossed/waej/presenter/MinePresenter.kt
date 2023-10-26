package com.bossed.waej.presenter

import android.app.Activity
import android.content.Intent
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.base.BasePresenter
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.contract.MineContract
import com.bossed.waej.eventbus.EBLogout
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.MineResponse
import com.bossed.waej.ui.LoginActivity
import com.bossed.waej.util.RetrofitUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class MinePresenter : BasePresenter<MineContract.MineView>(), MineContract.MineModel {
    override fun exit(activity: Activity) {
        RetrofitUtils.get().postJson(UrlConstants.LogOutUrl, HashMap<String, Any>(), activity,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    mView!!.onExitSuccess(t)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun getUserInfo(activity: Activity) {
        RetrofitUtils.get().getJson(UrlConstants.Mine_Msg, HashMap(), activity,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, MineResponse::class.java)
                    mView!!.onUserInfoSuccess(t)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}