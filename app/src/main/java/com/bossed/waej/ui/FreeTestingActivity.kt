package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.FreeTestContentAdapter
import com.bossed.waej.adapter.FreeTestTypeAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.http.UrlConstants.FreeCheckListUrl
import com.bossed.waej.javebean.FreeTestingData
import com.bossed.waej.javebean.FreeTestingResponse
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_free_testing.*

class FreeTestingActivity : BaseActivity(), OnClickListener {
    private lateinit var typeAdapter: FreeTestTypeAdapter
    private val typeList = ArrayList<FreeTestingData>()
    private lateinit var contentAdapter: FreeTestContentAdapter
    private val contentList = ArrayList<FreeTestingData>()
    private val allDate = ArrayList<FreeTestingData>()

    override fun getLayoutId(): Int {
        return R.layout.activity_free_testing
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_free_testing)
        rv_test_type.layoutManager = LinearLayoutManager(this)
        typeAdapter = FreeTestTypeAdapter(typeList)
        typeAdapter.bindToRecyclerView(rv_test_type)
        typeAdapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        rv_test_content.layoutManager = LinearLayoutManager(this)
        contentAdapter = FreeTestContentAdapter(contentList)
        contentAdapter.bindToRecyclerView(rv_test_content)
        contentAdapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        getList()
    }

    override fun initListener() {
        tb_free_testing.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        typeAdapter.setOnItemClickListener { adapter, view, position ->
            typeList.forEach {
                it.isSelect = false
            }
            typeList[position].isSelect = true
            adapter.setNewData(typeList)
            contentList.clear()
            allDate.forEach {
                if (it.parentId == typeList[position].id) {
                    contentList.add(it)
                }
            }
            contentAdapter.setNewData(contentList)
        }
        contentAdapter.setOnItemClickListener { adapter, view, position ->
            contentList[position].isSelect = !contentList[position].isSelect
            adapter.setNewData(contentList)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_commit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val stringBuilder = StringBuilder()
                allDate.forEach {
                    if (it.parentId != 0 && it.isSelect) {
                        if (stringBuilder.isNotEmpty())
                            stringBuilder.append(",")
                        stringBuilder.append(it.id!!.toString())
                    }
                }
                LogUtils.d("tag", stringBuilder.toString())
                val params = HashMap<String, String>()
                params["id"] = intent.getStringExtra("id")!!
                params["freecheckitems"] = stringBuilder.toString()
                params["fullname"] = intent.getStringExtra("fullname")!!
                LoadingUtils.showLoading(this, "加载中...")
                RetrofitUtils.get()
                    .putJson(UrlConstants.ShopUrl, params, this,
                        object : RetrofitUtils.OnCallBackListener {
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
        }
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        RetrofitUtils.get().getJson(
            FreeCheckListUrl, HashMap(), this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, FreeTestingResponse::class.java)
                    allDate.addAll(t.data)
                    t.data.forEach {
                        if (it.parentId == 0)
                            typeList.add(it)
                    }
                    typeList[0].isSelect = true
                    typeAdapter.setNewData(typeList)
                    val selected = intent.getStringExtra("freecheckitems")!!.split(",")
                    allDate.forEach {
                        selected.forEach { s ->
                            if (s == it.id!!.toString())
                                it.isSelect = true
                        }
                        if (it.parentId == typeList[0].id) {
                            contentList.add(it)
                        }
                    }
                    contentAdapter.setNewData(contentList)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}