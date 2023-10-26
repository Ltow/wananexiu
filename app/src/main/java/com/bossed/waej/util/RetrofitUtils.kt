package com.bossed.waej.util

import android.app.Activity
import android.content.Intent
import com.blankj.utilcode.util.GsonUtils
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.Api
import com.bossed.waej.http.ApiException
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.ui.BuyProductActivity
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.RequestBody

class RetrofitUtils private constructor() {

    companion object {
        private var instance: RetrofitUtils? = null
            get() {
                if (field == null)
                    instance = RetrofitUtils()
                return field
            }

        @Synchronized
        fun get(): RetrofitUtils {
            return instance!!
        }
    }

    fun postJson(url: String, params: Any, activity: Activity, listener: OnCallBackListener) {
        val body: RequestBody = FormBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(params)
        )
        Api.getInstance().getApiService()
            .postJson(url, body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    val gson = Gson()
                    val response: BaseResponse = gson.fromJson(s, BaseResponse::class.java)
                    when (response.code) {
                        200 -> {
                            listener.onSuccess(s)
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(activity)
                        }

                        703 -> {
                            PopupWindowUtils.get()
                                .showSetConfirmAlertDialog(activity, response.msg!!, "去购买", "") {
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            BuyProductActivity::class.java
                                        )
                                    )
                                }
                        }

                        else -> {
                            if (response.msg != null)
                                listener.onFailed(response.msg)
                            else
                                listener.onFailed("异常（代码：${response.code}）")
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    listener.onFailed(throwable.message!!)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    fun putJson(url: String, params: Any, activity: Activity, listener: OnCallBackListener) {
        val body: RequestBody = FormBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(params)
        )
        Api.getInstance().getApiService()
            .putJson(url, body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    val response = GsonUtils.fromJson(s, BaseResponse::class.java)
                    when (response.code) {
                        200 -> {
                            listener.onSuccess(s)
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(activity)
                        }

                        703 -> {
                            PopupWindowUtils.get()
                                .showSetConfirmAlertDialog(
                                    activity,
                                    response.msg!!,
                                    "去购买",
                                    ""
                                ) {
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            BuyProductActivity::class.java
                                        )
                                    )
                                }
                        }

                        else -> {
                            if (response.msg != null)
                                listener.onFailed(response.msg)
                            else
                                listener.onFailed("异常（代码：${response.code}）")
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    listener.onFailed(throwable.message!!)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    fun getJson(
        url: String,
        params: Map<String, String>,
        activity: Activity,
        listener: OnCallBackListener
    ) {
        Api.getInstance().getApiService()
            .get(url, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    val response = GsonUtils.fromJson(s, BaseResponse::class.java)
                    when (response.code) {
                        200 -> {
                            listener.onSuccess(s)
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(activity)
                        }

                        703 -> {
                            PopupWindowUtils.get()
                                .showSetConfirmAlertDialog(
                                    activity,
                                    response.msg!!,
                                    "去购买",
                                    ""
                                ) {
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            BuyProductActivity::class.java
                                        )
                                    )
                                }
                        }

                        else -> {
                            if (response.msg != null)
                                listener.onFailed(response.msg)
                            else
                                listener.onFailed("异常（代码：${response.code}）")
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    listener.onFailed("访问异常：" + throwable.message!!)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    fun post(
        url: String,
        params: Map<String, String>,
        activity: Activity,
        listener: OnCallBackListener
    ) {
        Api.getInstance().getApiService()
            .post(url, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    val response = GsonUtils.fromJson(s, BaseResponse::class.java)
                    when (response.code) {
                        200 -> {
                            listener.onSuccess(s)
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(activity)
                        }

                        703 -> {
                            PopupWindowUtils.get()
                                .showSetConfirmAlertDialog(
                                    activity,
                                    response.msg!!,
                                    "去购买",
                                    ""
                                ) {
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            BuyProductActivity::class.java
                                        )
                                    )
                                }
                        }

                        else -> {
                            if (response.msg != null)
                                listener.onFailed(response.msg)
                            else
                                listener.onFailed("异常（代码：${response.code}）")
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    listener.onFailed("访问异常：" + throwable.message!!)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    fun getBVDCJson(url: String, params: Map<String, String>, listener: OnCallBackListener) {
        Api.getInstance().getApiService()
            .get(url, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    listener.onSuccess(s)
                }

                override fun onError(throwable: Throwable) {
                    listener.onFailed("访问异常：" + throwable.message!!)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    interface OnCallBackListener {
        /**
         * 成功
         */
        fun onSuccess(s: String)

        /**
         * 失败
         */
        fun onFailed(e: String)
    }
}