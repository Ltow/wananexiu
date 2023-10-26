package com.bossed.waej.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants.PartUrl
import com.bossed.waej.javebean.PartInfoResponse
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_part.*
import android.view.MotionEvent
import android.view.View.OnTouchListener


class PartActivity : BaseActivity(), View.OnClickListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_part
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_part)
        tb_part.title = if (intent.getIntExtra("type", -1) == 0)//0:新增 1:打开
            "新增配件信息" else "修改配件信息"
        if (intent.getIntExtra("type", -1) == 1)
            getPartInfo()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        tb_part.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        et_name.clearFocus()
        et_name.isFocusableInTouchMode = false
        et_name.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                et_name.isFocusableInTouchMode = true
                et_name.requestFocus()
                et_name.selectAll()
            }
            false
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(et_name.text.toString()) -> ToastUtils.showShort("请输入配件名称")
                    TextUtils.isEmpty(et_model.text.toString()) -> ToastUtils.showShort("请输入规格或型号")
                    TextUtils.isEmpty(et_unit.text.toString()) -> ToastUtils.showShort("请输入单位")
                    TextUtils.isEmpty(et_code.text.toString()) -> ToastUtils.showShort("请输入自编码")
                    TextUtils.isEmpty(et_brand.text.toString()) -> ToastUtils.showShort("请输入品牌或产地")
                    TextUtils.isEmpty(et_marketPrice.text.toString()) -> ToastUtils.showShort("请输入销售价")
                    else ->
                        if (intent.getIntExtra("type", -1) == 1)
                            updatePart()
                        else
                            newPart()
                }
            }
        }
    }

    private fun newPart() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["name"] = et_name.text.toString()
        params["oe"] = et_oe.text.toString()
        params["model"] = et_model.text.toString()
        params["unit"] = et_unit.text.toString()
        params["code"] = et_code.text.toString()
        params["brand"] = et_brand.text.toString()
        params["marketPrice"] = et_marketPrice.text.toString()
        RetrofitUtils.get()
            .postJson(PartUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    ToastUtils.showShort(t.msg)
                    if (t.code == 200)
                        finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun updatePart() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["id"] = intent.getStringExtra("id")
        params["name"] = et_name.text.toString()
        params["oe"] = et_oe.text.toString()
        params["model"] = et_model.text.toString()
        params["unit"] = et_unit.text.toString()
        params["code"] = et_code.text.toString()
        params["brand"] = et_brand.text.toString()
        params["marketPrice"] = et_marketPrice.text.toString()
        RetrofitUtils.get()
            .putJson(PartUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    ToastUtils.showShort(t.msg)
                    if (t.code == 200)
                        finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getPartInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getPartInfo(intent.getStringExtra("id").toInt())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, PartInfoResponse::class.java)
                    when (t.code) {
                        200 -> {
                            et_name.setText(t.data.name)
                            et_oe.setText(t.data.oe)
                            et_model.setText(t.data.model)
                            et_unit.setText(t.data.unit)
                            et_code.setText(t.data.code)
                            et_brand.setText(t.data.brand)
                            et_marketPrice.setText(t.data.marketPrice)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@PartActivity)
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