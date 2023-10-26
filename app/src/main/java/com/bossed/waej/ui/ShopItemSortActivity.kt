package com.bossed.waej.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.ShopItemSortAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.http.UrlConstants.FreeItemSortListUrl
import com.bossed.waej.http.UrlConstants.FreeItemSortUrl
import com.bossed.waej.javebean.ShopItemSortData
import com.bossed.waej.javebean.ShopItemSortResponse
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_support_sort.*

class ShopItemSortActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: ShopItemSortAdapter
    private val list = ArrayList<ShopItemSortData>()

    override fun getLayoutId(): Int {
        return R.layout.activity_support_sort
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_support_sort)
        tb_support_sort.title = "项目分类"
        btn_commit.text = "添加项目分类"
        rv_support_sort.layoutManager = LinearLayoutManager(this)
        adapter = ShopItemSortAdapter(list)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        adapter.bindToRecyclerView(rv_support_sort)
    }

    override fun initListener() {
        tb_support_sort.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_edit -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    val popWindow =
                        PopWindow.Builder(this).setView(R.layout.layout_pop_base_cofirm_edit)
                            .setWidthAndHeight(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            .setAnimStyle(R.style.CenterAnimation)
                            .setOutsideTouchable(true)
                            .setBackGroundLevel(0.5f)
                            .setChildrenView { contentView, pop ->
                                contentView.findViewById<TextView>(R.id.tv_title).text =
                                    "修改类别名称"
                                contentView.findViewById<TextView>(R.id.tv_content).text =
                                    "类别名称"
                                val editText = contentView.findViewById<EditText>(R.id.et_content)
                                editText.hint = "请输入项目类别名称"
                                editText.setText(list[position].name)
                                contentView.findViewById<TextView>(R.id.tv_confirm)!!
                                    .setOnClickListener {
                                        if (TextUtils.isEmpty(editText.text.toString()))
                                            return@setOnClickListener
                                        LoadingUtils.showLoading(this, "加载中...")
                                        val params = HashMap<String, Any>()
                                        params["id"] = list[position].id!!
                                        params["name"] = editText.text.toString()
                                        params["status"] = list[position].status!!
                                        params["parentId"] = 0
                                        RetrofitUtils.get().putJson(
                                            FreeItemSortUrl, params, this,
                                            object : RetrofitUtils.OnCallBackListener {
                                                override fun onSuccess(s: String) {
                                                    LogUtils.d("tag", s)
                                                    val t = GsonUtils.fromJson(
                                                        s,
                                                        BaseResponse::class.java
                                                    )
                                                    ToastUtils.showShort(t.msg)
                                                    if (t.code == 200) {
                                                        getList()
                                                    }
                                                }

                                                override fun onFailed(e: String) {
                                                    ToastUtils.showShort(e)
                                                }
                                            })
                                        pop.dismiss()
                                    }
                                contentView.findViewById<TextView>(R.id.tv_cancel)!!
                                    .setOnClickListener {
                                        pop.dismiss()
                                    }
                            }.create()
                    popWindow.isClippingEnabled = false
                    popWindow.showAtLocation(ll_content, Gravity.CENTER, 0, 0)
                }

                R.id.iv_delete -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get().showConfirmPop(this, "确认删除此类别？") {
                        LoadingUtils.showLoading(this, "加载中...")
                        Api.getInstance().getApiService()
                            .deleteSelfPackage(list[position].id!!)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : BaseResourceObserver<BaseResponse>() {
                                override fun onComplete() {
                                    LoadingUtils.dismissLoading()
                                }

                                override fun onSubscribe(d: Disposable) {
                                }

                                override fun onNext(t: BaseResponse) {
                                    ToastUtils.showShort(t.msg)
                                    when (t.code) {
                                        200 -> {
                                            getList()
                                        }

                                        401 -> {
                                            PopupWindowUtils.get()
                                                .showLoginOutTimePop(this@ShopItemSortActivity)
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
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_commit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get()
                    .showEditPop(
                        this,
                        ll_content,
                        "添加项目分类",
                        "分类名称",
                        "请输入类别名称"
                    ) {
                        if (TextUtils.isEmpty(it))
                            return@showEditPop
                        LoadingUtils.showLoading(this, "加载中...")
                        val params = HashMap<String, Any>()
                        params["name"] = it
                        params["status"] = 1
                        params["parentId"] = 0
                        RetrofitUtils.get().postJson(
                            FreeItemSortUrl, params, this,
                            object : RetrofitUtils.OnCallBackListener {
                                override fun onSuccess(s: String) {
                                    LogUtils.d("tag", s)
                                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                                    ToastUtils.showShort(t.msg)
                                    if (t.code == 200) {
                                        getList()
                                    }
                                }

                                override fun onFailed(e: String) {
                                    ToastUtils.showShort(e)
                                }
                            })
                    }
            }
        }
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        RetrofitUtils.get().getJson(FreeItemSortListUrl, HashMap(), this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, ShopItemSortResponse::class.java)
                    list.clear()
                    list.addAll(t.data)
                    adapter.setNewData(list)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        getList()
    }
}