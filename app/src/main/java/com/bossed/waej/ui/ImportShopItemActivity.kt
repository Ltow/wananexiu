package com.bossed.waej.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.ImportShopItemAdapter
import com.bossed.waej.adapter.ImportShopItemTypeAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.UrlConstants.ImportItemListUrl
import com.bossed.waej.http.UrlConstants.ImportItemUrl
import com.bossed.waej.javebean.ImportItemResponse
import com.bossed.waej.javebean.ImportItemRow
import com.bossed.waej.javebean.ImportShopItemTypeBean
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_import_shop_item.*
import java.math.BigDecimal

class ImportShopItemActivity : BaseActivity(), View.OnClickListener {
    private lateinit var typeAdapter: ImportShopItemTypeAdapter
    private val typeList = ArrayList<ImportShopItemTypeBean>()
    private lateinit var adapter: ImportShopItemAdapter
    private val list = ArrayList<ImportItemRow>()
    private var categoryId = 1
    private var categoryName = "美容类"

    override fun getLayoutId(): Int {
        return R.layout.activity_import_shop_item
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_import_item)
        rv_item_type.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        typeList.add(ImportShopItemTypeBean(1, "美容类", true))
        typeList.add(ImportShopItemTypeBean(2, "养护类", false))
        typeList.add(ImportShopItemTypeBean(3, "轮胎服务", false))
        typeAdapter = ImportShopItemTypeAdapter(typeList)
        typeAdapter.bindToRecyclerView(rv_item_type)
        rv_item.layoutManager = LinearLayoutManager(this)
        adapter = ImportShopItemAdapter(list)
        adapter.bindToRecyclerView(rv_item)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        tb_import_item.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        typeAdapter.setOnItemClickListener { adapter, view, position ->
            typeList.forEach { it.isSelect = false }
            typeList[position].isSelect = true
            typeAdapter.setNewData(typeList)
            categoryId = typeList[position].id
            categoryName = typeList[position].name
            getList()
        }
        adapter.setOnItemClickListener { adapter, view, position ->
            list[position].isSelect = !list[position].isSelect
            adapter.setNewData(list)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_commit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val selected = ArrayList<ImportItemRow>()
                list.forEach {
                    if (it.isSelect)
//                        if (BigDecimal(if (TextUtils.isEmpty(it.priceCash)) "0" else it.priceCash) > BigDecimal(
//                                if (TextUtils.isEmpty(it.originalPrice)) "0" else it.originalPrice
//                            )
//                        )
//                            ToastUtils.showShort("${it.serviceItem}的会员价不能大于实际零售价")
//                        else
                            selected.add(it)
                }
                if (selected.isEmpty())
                    return
                LoadingUtils.showLoading(this, "加载中...")
                RetrofitUtils.get().postJson(
                    ImportItemUrl, selected, this,
                    object : RetrofitUtils.OnCallBackListener {
                        override fun onSuccess(s: String) {
                            LogUtils.d("tag", s)
                            val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                            if (t.code == 200)
                                finish()
                            ToastUtils.showShort(t.msg)
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
        val params = HashMap<String, String>()
        params["categoryId"] = categoryId.toString()
        params["categoryName"] = categoryName
        RetrofitUtils.get()
            .getJson(ImportItemListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, ImportItemResponse::class.java)
                    list.clear()
                    list.addAll(t.rows)
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