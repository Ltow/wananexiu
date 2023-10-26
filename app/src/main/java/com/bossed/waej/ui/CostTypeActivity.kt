package com.bossed.waej.ui

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.CostTypeAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.UrlConstants.CateTypeListUrl
import com.bossed.waej.http.UrlConstants.CateTypeUrl
import com.bossed.waej.javebean.CostTypeResponse
import com.bossed.waej.javebean.CostTypeRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_cost_type.*

class CostTypeActivity : BaseActivity(), View.OnClickListener {
    private lateinit var costTypeAdapter: CostTypeAdapter
    private val list = ArrayList<CostTypeRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_cost_type
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_cost_type)
        rv_cost_type.layoutManager = LinearLayoutManager(this)
        val footView = LayoutInflater.from(this).inflate(R.layout.layout_footer_view_item, null)
        footView.findViewById<TextView>(R.id.tv_add_item).setOnClickListener(this)
        footView.findViewById<TextView>(R.id.tv_add_item).text = "新增费用类别"
        footView.findViewById<View>(R.id.rl_content).setBackgroundColor(Color.parseColor("#f6f6f6"))
        costTypeAdapter = CostTypeAdapter(list)
        costTypeAdapter.bindToRecyclerView(rv_cost_type)
        costTypeAdapter.setFooterView(footView)
    }

    override fun initListener() {
        tb_cost_type.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        costTypeAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_edit_item -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get()
                        .showEditPop(this, rl_content, "修改费用类别", "费用类别名称", "请输入类别名称") {
                            if (TextUtils.isEmpty(it)) {
                                ToastUtils.showShort("类别名称不能为空")
                                return@showEditPop
                            }
                            update(it, list[position].id, list[position].status)
                        }
                }
                R.id.sb_person -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemChildClickListener
                    PopupWindowUtils.get().showConfirmAndCancelPop(this,
                        "是否${if (list[position].status == 1) "禁用" else "启用"}此类别？",
                        object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                update(
                                    list[position].name,
                                    list[position].id,
                                    if (list[position].status == 1) 0 else 1
                                )
                            }

                            override fun onCancel() {
                                getList()
                            }
                        })
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_add_item -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get()
                    .showEditPop(this, rl_content, "新增费用类别", "费用类别名称", "请输入类别名称") {
                        if (TextUtils.isEmpty(it)) {
                            ToastUtils.showShort("类别名称不能为空")
                            return@showEditPop
                        }
                        new(it)
                    }
            }
        }
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        RetrofitUtils.get()
            .getJson(CateTypeListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, CostTypeResponse::class.java)
                    list.clear()
                    list.addAll(t.rows as ArrayList)
                    costTypeAdapter.setNewData(list)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun new(name: String) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["name"] = name
        RetrofitUtils.get()
            .postJson(CateTypeUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    ToastUtils.showShort(t.msg)
                    if (t.code == 200)
                        getList()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun update(name: String, id: String, status: Int) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["name"] = name
        params["id"] = id
        params["status"] = status
        RetrofitUtils.get()
            .putJson(CateTypeUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    ToastUtils.showShort(t.msg)
                    if (t.code == 200)
                        getList()
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