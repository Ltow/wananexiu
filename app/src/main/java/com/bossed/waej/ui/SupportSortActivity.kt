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
import com.bossed.waej.adapter.SupportSortAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants.GoodsSortListUrl
import com.bossed.waej.http.UrlConstants.GoodsSortUrl
import com.bossed.waej.javebean.GoodsSortData
import com.bossed.waej.javebean.GoodsSortResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_support_sort.*

class SupportSortActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: SupportSortAdapter
    private val list = ArrayList<GoodsSortData>()

    override fun getLayoutId(): Int {
        return R.layout.activity_support_sort
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_support_sort)
        rv_support_sort.layoutManager = LinearLayoutManager(this)
        adapter = SupportSortAdapter(list)
        adapter.bindToRecyclerView(rv_support_sort)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
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
                                    "商品类别名称"
                                val editText = contentView.findViewById<EditText>(R.id.et_content)
                                editText.hint = "请输入商品类别名称"
                                editText.setText(list[position].name)
                                contentView.findViewById<TextView>(R.id.tv_confirm)!!
                                    .setOnClickListener {
                                        if (TextUtils.isEmpty(editText.text.toString()))
                                            return@setOnClickListener
                                        LoadingUtils.showLoading(this, "加载中...")
                                        val params = HashMap<String, Any>()
                                        params["id"] = list[position].id
                                        params["name"] = editText.text.toString()
                                        RetrofitUtils.get().putJson(
                                            GoodsSortUrl, params, this,
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
                            .deleteGoodsSort(list[position].id)
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
                                                .showLoginOutTimePop(this@SupportSortActivity)
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
                        this, ll_content, "添加商品分类", "商品分类名称", "请输入类别名称"
                    ) {
                        if (TextUtils.isEmpty(it))
                            return@showEditPop
                        LoadingUtils.showLoading(this, "加载中...")
                        val params = HashMap<String, Any>()
                        params["name"] = it
                        params["parentId"] = 0
                        params["status"] = 1
                        params["shopId"] = intent.getStringExtra("shopId")
                        RetrofitUtils.get().postJson(
                            GoodsSortUrl, params, this,
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
        RetrofitUtils.get()
            .getJson(GoodsSortListUrl, HashMap(), this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, GoodsSortResponse::class.java)
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