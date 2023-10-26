package com.bossed.waej.ui

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.SelfPackageAdapter
import com.bossed.waej.adapter.SelfSupportAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.http.UrlConstants.FreeItemListUrl
import com.bossed.waej.javebean.SelfPackageResponse
import com.bossed.waej.javebean.SelfPackageRow
import com.bossed.waej.javebean.SelfSupportResponse
import com.bossed.waej.javebean.SelfSupportRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_public_goods.*
import java.math.BigDecimal

/**
 * 公域商品维护
 */
class PublicGoodsActivity : BaseActivity(), OnRefreshLoadMoreListener, OnClickListener {
    private var status = 0
    private lateinit var supportAdapter: SelfSupportAdapter
    private val supportRows = ArrayList<SelfSupportRow>()
    private lateinit var packageAdapter: SelfPackageAdapter
    private val packageRows = ArrayList<SelfPackageRow>()
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_public_goods
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_public_goods)
        rv_public_goods.layoutManager = LinearLayoutManager(this)
    }

    override fun initListener() {
        srl_public_goods.setOnRefreshLoadMoreListener(this)
        tb_public_goods.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
                TODO("Not yet implemented")
            }

            override fun onRightClick(view: View?) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageNum = 1
        when (status) {
            0 -> {
                supportRows.clear()
                getGoodsList()
            }

            1 -> {
                packageRows.clear()
                getPackageList()
            }
        }
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        when (status) {
            0 -> {
                getGoodsList()
            }

            1 -> {
                getPackageList()
            }
        }
        refreshLayout.finishLoadMore()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_goods -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                status = 0
                change()
            }

            R.id.btn_items -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                status = 1
                change()
            }
        }
    }

    private fun change() {
        btn_goods.setBackgroundResource(R.drawable.shape_corners_cccccc_dp12_5)
        btn_items.setBackgroundResource(R.drawable.shape_corners_cccccc_dp12_5)
        when (status) {
            0 -> {
                btn_goods.setBackgroundResource(R.drawable.shape_corners_3477fc_dp_12_5)
                supportAdapter = SelfSupportAdapter(supportRows, 1)
                supportAdapter.bindToRecyclerView(rv_public_goods)
                supportAdapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
                supportAdapter.setOnItemChildClickListener { adapter, view, position ->
                    when (view.id) {
                        R.id.iv_edit -> {
                            if (DoubleClicksUtils.get().isFastDoubleClick)
                                return@setOnItemChildClickListener
                            showEditPop(0, position)
                        }
                    }
                }
            }

            1 -> {
                btn_items.setBackgroundResource(R.drawable.shape_corners_3477fc_dp_12_5)
                packageAdapter = SelfPackageAdapter(packageRows, 1)
                packageAdapter.bindToRecyclerView(rv_public_goods)
                packageAdapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
                packageAdapter.setOnItemChildClickListener { adapter, view, position ->
                    when (view.id) {
                        R.id.iv_edit -> {
                            if (DoubleClicksUtils.get().isFastDoubleClick)
                                return@setOnItemChildClickListener
                            showEditPop(1, position)
                        }
                    }
                }
            }
        }
        onRefresh(srl_public_goods)
    }

    private fun showEditPop(type: Int, position: Int) {
        val popWindow =
            PopWindow.Builder(this).setView(R.layout.layout_pop_edit_price)
                .setWidthAndHeight(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                .setAnimStyle(R.style.CenterAnimation)
                .setOutsideTouchable(true)
                .setBackGroundLevel(0.5f)
                .setChildrenView { contentView, pop ->
                    val et_publicVirtualPrice =
                        contentView.findViewById<EditText>(R.id.et_publicVirtualPrice)
                    val et_publicPrice =
                        contentView.findViewById<EditText>(R.id.et_publicPrice)
                    when (type) {
                        0 -> {
                            et_publicVirtualPrice.setText(
                                if (TextUtils.isEmpty(
                                        supportRows[position].publicVirtualPrice
                                    )
                                ) "" else supportRows[position].publicVirtualPrice
                            )
                            et_publicPrice.setText(if (TextUtils.isEmpty(supportRows[position].publicPrice)) "" else supportRows[position].publicPrice)
                        }

                        1 -> {
                            et_publicVirtualPrice.setText(
                                if (TextUtils.isEmpty(
                                        packageRows[position].publicVirtualPrice
                                    )
                                ) "" else packageRows[position].publicVirtualPrice
                            )
                            et_publicPrice.setText(if (TextUtils.isEmpty(packageRows[position].publicPrice)) "" else packageRows[position].publicPrice)
                        }
                    }

                    contentView.findViewById<View>(R.id.tv_confirm)
                        .setOnClickListener {
                            if (TextUtils.isEmpty(et_publicPrice.text.toString()) || TextUtils.isEmpty(
                                    et_publicVirtualPrice.text.toString()
                                )
                            )
                                return@setOnClickListener
                            when (type) {
                                0 -> updateGoodsPrice(
                                    position,
                                    et_publicPrice.text.toString(),
                                    et_publicVirtualPrice.text.toString()
                                )

                                1 -> {
                                    updatePackagePrice(
                                        position,
                                        et_publicPrice.text.toString(),
                                        et_publicVirtualPrice.text.toString()
                                    )
                                }
                            }

                            pop.dismiss()
                        }
                    contentView.findViewById<View>(R.id.tv_cancel)
                        .setOnClickListener { pop.dismiss() }
                }.create()
        popWindow.isClippingEnabled = false
        popWindow.showAtLocation(ll_content, Gravity.CENTER, 0, 0)
    }

    /**
     * 修改项目价格
     */
    private fun updatePackagePrice(position: Int, publicPrice: String, publicVirtualPrice: String) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["id"] = packageRows[position].id!!
        params["publicPrice"] = publicPrice
        params["publicVirtualPrice"] = publicVirtualPrice
        RetrofitUtils.get()
            .putJson(UrlConstants.FreeItemUrl, params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        LogUtils.d("tag", s)
                        val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                        if (t.code == 200) {
                            packageRows[position].publicPrice =
                                BigDecimal(publicPrice).setScale(2, BigDecimal.ROUND_HALF_DOWN)
                                    .toString()
                            packageRows[position].publicVirtualPrice =
                                BigDecimal(publicVirtualPrice).setScale(
                                    2,
                                    BigDecimal.ROUND_HALF_DOWN
                                ).toString()
                            packageAdapter.setNewData(packageRows)
                        }
                        ToastUtils.showShort(t.msg)
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }

    /**
     * 更新商品价格
     */
    private fun updateGoodsPrice(position: Int, publicPrice: String, publicVirtualPrice: String) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["id"] = supportRows[position].id
        params["productType"] = "1"
        params["publicPrice"] = publicPrice
        params["publicVirtualPrice"] = publicVirtualPrice
        RetrofitUtils.get()
            .putJson(UrlConstants.GoodsUrl, params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        LogUtils.d("tag", s)
                        val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                        ToastUtils.showShort(t.msg)
                        if (t.code == 200) {
                            supportRows[position].publicPrice =
                                BigDecimal(publicPrice).setScale(2, BigDecimal.ROUND_HALF_DOWN)
                                    .toString()
                            supportRows[position].publicVirtualPrice =
                                BigDecimal(publicVirtualPrice).setScale(
                                    2,
                                    BigDecimal.ROUND_HALF_DOWN
                                ).toString()
                            supportAdapter.setNewData(supportRows)
                        }
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }

    /**
     * 商品列表
     */
    private fun getGoodsList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["pageSize"] = "20"
        params["pageNum"] = pageNum.toString()
        RetrofitUtils.get()
            .getJson(UrlConstants.GoodsListUrl, params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        LogUtils.d("tag", s)
                        val t = GsonUtils.fromJson(s, SelfSupportResponse::class.java)
                        supportRows.addAll(t.rows)
                        supportAdapter.setNewData(supportRows)
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }

    /**
     * 项目列表
     */
    private fun getPackageList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["pageSize"] = "20"
        params["pageNum"] = pageNum.toString()
        RetrofitUtils.get()
            .getJson(FreeItemListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, SelfPackageResponse::class.java)
                    packageRows.addAll(t.rows)
                    packageAdapter.setNewData(packageRows)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }


    override fun onResume() {
        super.onResume()
        change()
    }
}