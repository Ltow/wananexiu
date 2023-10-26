package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.adapter.DepartmentAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.customview.BottomDialog
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.DepartListData
import com.bossed.waej.javebean.DepartListResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_department_manage.*

class DepartmentActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var departmentAdapter: DepartmentAdapter
    private val departList = ArrayList<DepartListData>()

    override fun getLayoutId(): Int {
        return R.layout.activity_department_manage
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        BarUtils.setStatusBarLightMode(window, true)
        setMarginTop(tb_depart_man)
        rv_depart.layoutManager = LinearLayoutManager(this)
        departmentAdapter = DepartmentAdapter(departList)
        departmentAdapter.bindToRecyclerView(rv_depart)
        departmentAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        ll_bottom.visibility =
            if (intent.getStringExtra("type") == "register") View.VISIBLE else View.GONE
        tb_depart_man.title =
            if (intent.getStringExtra("type") == "register") "请完善企业部门角色" else "部门管理"
    }

    override fun initListener() {
        tb_depart_man.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                showAddDepart("", -1)
            }
        })
        departmentAdapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(this, RoleActivity::class.java)
            intent.putExtra("deptName", departList[position].deptName)
            intent.putExtra("deptId", departList[position].deptId)
            startActivity(intent)
        }
        departmentAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_edit -> {
                    showAddDepart(departList[position].deptName, position)
                }
                R.id.iv_delete -> {
                    PopupWindowUtils.get()
                        .showConfirmPop(this, "确定删除此部门？") {
                            LoadingUtils.showLoading(this@DepartmentActivity, "加载中")
                            Api.getInstance().getApiService()
                                .deleteDepart(departList[position].deptId)
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
                                                getDepartList()
                                            }
                                            401 -> {
                                                PopupWindowUtils.get().showLoginOutTimePop(this@DepartmentActivity)
                                            }
                                            703 -> {
                                                PopupWindowUtils.get()
                                                    .showSetConfirmAlertDialog(mContext, t.msg!!, "去购买", "") {
                                                        mContext.startActivity(
                                                            Intent(
                                                                mContext,
                                                                BuyProductActivity::class.java
                                                            )
                                                        )
                                                    }
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

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_skip -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this, RegisterSuccessActivity::class.java))
                finish()
            }
            R.id.tv_next -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, PersonListActivity::class.java)
                intent.putExtra("type", "register")
                startActivity(intent)
            }
        }
    }

    private val showAddDepart: (String, Int) -> Unit = { name, position ->
        BottomDialog(this).create(R.layout.bottom_dialog_add_depart)
            .setCanceledOnTouchOutside(true)
            .setViewInterface { view, dialog ->
                view.findViewById<TextView>(R.id.tv_title).text =
                    if (position == -1) "添加部门" else "编辑部门"
                val editText = view.findViewById<EditText>(R.id.et_depart_name)
                editText.setText(name)
                val ll_bottom = view.findViewById<View>(R.id.ll_bottom)
                if (BarUtils.isNavBarVisible(window)) {
                    BarUtils.setNavBarLightMode(window, true)
                    val layoutParams = ll_bottom.layoutParams as LinearLayout.LayoutParams
                    layoutParams.bottomMargin = BarUtils.getNavBarHeight()
                    ll_bottom.layoutParams = layoutParams
                }
                SoftKeyBoardUtils.setOnKeyBoardChangeListener(this,
                    object : SoftKeyBoardUtils.OnSoftKeyBoardChangeListener {
                        override fun keyBoardShow(height: Int) {
                            val layoutParams =
                                ll_bottom.layoutParams as LinearLayout.LayoutParams
                            layoutParams.bottomMargin = height + ConvertUtils.dp2px(10f)
                            ll_bottom.layoutParams = layoutParams
                        }

                        override fun keyBoardHide(height: Int) {
                            val layoutParams =
                                ll_bottom.layoutParams as LinearLayout.LayoutParams
                            layoutParams.bottomMargin = ConvertUtils.dp2px(10f)
                            ll_bottom.layoutParams = layoutParams
                        }
                    })
                view.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                    dialog.dismiss()
                }
                view.findViewById<View>(R.id.tv_confirm).setOnClickListener {
                    if (TextUtils.isEmpty(editText.text.toString())) {
                        ToastUtils.showShort("部门名称不能为空")
                        return@setOnClickListener
                    }
                    if (TextUtils.isEmpty(name))
                        newDepart(editText.text.toString())
                    else
                        update(editText.text.toString(), position)
                    dialog.dismiss()
                }
            }.show()
    }

    private val getDepartList: () -> Unit = {
        LoadingUtils.showLoading(this, "加载中")
        val params = HashMap<String, String>()
        RetrofitUtils.get().getJson(
            UrlConstants.DepartListUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, DepartListResponse::class.java)
                    departList.clear()
                    departList.addAll(t.data)
                    departmentAdapter.setNewData(departList)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private val newDepart: (String) -> Unit = {
        LoadingUtils.showLoading(this, "加载中")
        val params = HashMap<String, Any>()
        params["deptName"] = it
        params["orderNum"] = 1
        RetrofitUtils.get().postJson(
            UrlConstants.NewDepartUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    getDepartList()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
    private val update: (String, Int) -> Unit = { it, position ->
        LoadingUtils.showLoading(this, "加载中")
        val params = HashMap<String, Any>()
        params["deptName"] = it
        params["deptId"] = departList[position].deptId
        params["orderNum"] = 1
        params["parentId"] = departList[position].parentId
        RetrofitUtils.get().putJson(
            UrlConstants.NewDepartUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    getDepartList()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        getDepartList()
    }


}