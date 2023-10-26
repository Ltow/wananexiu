package com.bossed.waej.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.DepartListData
import com.bossed.waej.javebean.PersonResponse
import com.bossed.waej.ui.BuyProductActivity
import com.bossed.waej.ui.PersonDataActivity
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DepartListAdapter(mutableList: MutableList<DepartListData>) :
    BaseQuickAdapter<DepartListData, BaseViewHolder>(
        R.layout.layout_item_depart_person,
        mutableList
    ) {

    override fun convert(helper: BaseViewHolder, item: DepartListData) {
        helper.setText(R.id.tv_name, item.deptName)
            .addOnClickListener(R.id.tv_name)
        val recyclerView = helper.getView<RecyclerView>(R.id.rv_person)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        getPerson(item.deptId, recyclerView)
        var isShow = false
        helper.getView<TextView>(R.id.tv_name).setOnClickListener {
            helper.getView<ImageView>(R.id.iv_open)
                .setImageResource(if (isShow) R.mipmap.icon_open_up else R.mipmap.icon_close_down)
            if (isShow) {
                isShow = false
                recyclerView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.GONE
                isShow = true
            }
        }
    }

    private fun getPerson(deptId: Int, recyclerView: RecyclerView) {
        val params = HashMap<String, String>()
        params["deptId"] = deptId.toString()
        params["pageNum"] = "1"
        params["pageSize"] = "10000"
        RetrofitUtils.get().getJson(
            UrlConstants.PersonListUrl, params, mContext as Activity,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, PersonResponse::class.java)
                    val personAdapter = PersonAdapter(t.rows as ArrayList)
                    personAdapter.bindToRecyclerView(recyclerView)
                    personAdapter.setOnItemChildClickListener { adapter, view, position ->
                        when (view.id) {
                            R.id.rl_delete -> {
                                PopupWindowUtils.get()
                                    .showConfirmPop(mContext as Activity, "确定删除此员工？") {
                                        LoadingUtils.showLoading(mContext as Activity, "加载中")
                                        Api.getInstance().getApiService()
                                            .deletePerson(t.rows[position].id)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(object :
                                                BaseResourceObserver<BaseResponse>() {
                                                override fun onComplete() {
                                                    LoadingUtils.dismissLoading()
                                                }

                                                override fun onSubscribe(d: Disposable) {

                                                }

                                                override fun onNext(t1: BaseResponse) {
                                                    ToastUtils.showShort(t1.msg)
                                                    when (t1.code) {
                                                        200 -> {
                                                            t.rows.removeAt(position)
                                                            adapter.setNewData(t.rows)
                                                        }
                                                        401 -> {
                                                            PopupWindowUtils.get()
                                                                .showLoginOutTimePop(
                                                                    mContext as Activity
                                                                )
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
                            R.id.tv_phone -> {
                                if (PermissionUtils.isGranted(PermissionConstants.PHONE)) {
                                    val intent = Intent(
                                        Intent.ACTION_CALL,
                                        Uri.parse("tel:${t.rows[position].phone}")
                                    )
                                    mContext.startActivity(intent)
                                } else {
                                    PermissionUtils.permission(PermissionConstants.PHONE)
                                        .callback(object : PermissionUtils.SimpleCallback {
                                            override fun onGranted() {
                                                val intent =
                                                    Intent(
                                                        Intent.ACTION_CALL,
                                                        Uri.parse("tel:${t.rows[position].phone}")
                                                    )
                                                mContext.startActivity(intent)
                                            }

                                            override fun onDenied() {
                                                ToastUtils.showShort("请在系统设置中开启拨号权限")
                                            }
                                        }).request()
                                }
                            }
                            R.id.ll_content -> {
                                val intent = Intent(
                                    mContext as Activity,
                                    PersonDataActivity::class.java
                                )
                                intent.putExtra("personType", "open")
                                intent.putExtra("id", t.rows[position].id)
                                mContext.startActivity(intent)
                            }
                        }
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

}