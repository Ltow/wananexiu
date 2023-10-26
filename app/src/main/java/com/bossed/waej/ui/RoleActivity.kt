package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.RoleAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.RoleResponse
import com.bossed.waej.javebean.RoleRow
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_role.*

class RoleActivity : BaseActivity() {
    private lateinit var roleAdapter: RoleAdapter
    private val roleList = ArrayList<RoleRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_role
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        setMarginTop(tb_role_man)
        BarUtils.setStatusBarLightMode(window, true)
        rv_role.layoutManager = LinearLayoutManager(this)
        roleAdapter = RoleAdapter(roleList)
        roleAdapter.bindToRecyclerView(rv_role)
        tv_depart.text = intent.getStringExtra("deptName")
    }

    override fun initListener() {
        tb_role_man.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                val intent = Intent(this@RoleActivity, LimitsActivity::class.java)
                intent.putExtra("deptName", getIntent().getStringExtra("deptName"))
                intent.putExtra("deptId", getIntent().getIntExtra("deptId", -1))
                startActivity(intent)
            }
        })
        roleAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_edit -> {
                    val intent = Intent(this@RoleActivity, LimitsActivity::class.java)
                    intent.putExtra("deptName", getIntent().getStringExtra("deptName"))
                    intent.putExtra("deptId", getIntent().getIntExtra("deptId", -1))
                    intent.putExtra("roleId", roleList[position].roleId)
                    startActivity(intent)
                }
                R.id.iv_delete -> {
                    PopupWindowUtils.get()
                        .showConfirmPop(this, "确定删除此角色？") {
                            LoadingUtils.showLoading(this@RoleActivity, "加载中")
                            Api.getInstance().getApiService()
                                .deleteRole(roleList[position].roleId)
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
                                                getRoleList()
                                            }
                                            401 -> {
                                                PopupWindowUtils.get()
                                                    .showLoginOutTimePop(this@RoleActivity)
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

    private fun getRoleList() {
        LoadingUtils.showLoading(this@RoleActivity, "加载中")
        val params = HashMap<String, String>()
        params["deptId"] = intent.getIntExtra("deptId", -1).toString()
        RetrofitUtils.get().getJson(
            UrlConstants.RoleListUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, RoleResponse::class.java)
                    roleList.clear()
                    roleList.addAll(t.rows)
                    roleAdapter.setNewData(roleList)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        getRoleList()
    }
}