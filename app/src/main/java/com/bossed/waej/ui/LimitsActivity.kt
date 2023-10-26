package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.customview.treeView.Node
import com.bossed.waej.customview.treeView.TreeListView
import com.bossed.waej.http.UrlConstants.TreeListUrl
import com.bossed.waej.javebean.TreeListBean
import com.bossed.waej.javebean.TreeListData
import com.bossed.waej.util.OnNoRepeatClickListener
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_limits.*
import android.widget.RelativeLayout
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.RoleInfoResponse
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.DoubleClicksUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class LimitsActivity : BaseActivity(), OnNoRepeatClickListener {
    private val treeList = ArrayList<Node>()
    private var treeView: TreeListView? = null
    private val appMenuIds = arrayListOf<Int>()

    override fun getLayoutId(): Int {
        return R.layout.activity_limits
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        setMarginTop(tb_limits)
        BarUtils.setStatusBarLightMode(window, true)
        tv_depart_name.text = intent.getStringExtra("deptName")
        if (intent.getIntExtra("roleId", -1) == -1)
            getTreeList()
        else {
            getInfo(intent.getIntExtra("roleId", -1))
            tb_limits.title = "修改角色"
        }
    }

    override fun initListener() {
        tb_limits.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (intent.getIntExtra("roleId", -1) == -1) {
                    LoadingUtils.showLoading(this, "加载中")
                    val ids = arrayListOf<String>()
                    for (node: Node in treeView!!.get()) {
                        if (node.isChecked)
                            ids.add(node.curId)
                    }
                    val params = HashMap<String, Any>()
                    params["roleName"] = et_name.text.toString()
                    params["roleKey"] = UUID.randomUUID()
                    params["appMenuIds"] = ids
                    params["menuCheckStrictly"] = 0
                    params["roleSort"] = 1
                    params["deptId"] = intent.getIntExtra("deptId", -1)
                    RetrofitUtils.get().postJson(
                        UrlConstants.NewRoleUrl, params, this,
                        object : RetrofitUtils.OnCallBackListener {
                            override fun onSuccess(s: String) {
                                val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                                ToastUtils.showShort(t.msg)
                                if (t.code == 200) {
                                    finish()
                                }
                            }

                            override fun onFailed(e: String) {
                                ToastUtils.showShort(e)
                            }
                        })
                } else {
                    LoadingUtils.showLoading(this, "加载中")
                    val ids = arrayListOf<String>()
                    for (node: Node in treeView!!.get()) {
                        if (node.isChecked)
                            ids.add(node.curId)
                    }
                    val params = HashMap<String, Any>()
                    params["roleId"] = intent.getIntExtra("roleId", -1)
                    params["roleName"] = et_name.text.toString()
                    params["roleKey"] = UUID.randomUUID()
                    params["appMenuIds"] = ids
                    params["menuCheckStrictly"] = 0
                    params["roleSort"] = 1
                    params["deptId"] = intent.getIntExtra("deptId", -1)
                    RetrofitUtils.get().putJson(
                        UrlConstants.NewRoleUrl, params, this,
                        object : RetrofitUtils.OnCallBackListener {
                            override fun onSuccess(s: String) {
                                val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                                ToastUtils.showShort(t.msg)
                                if (t.code == 200) {
                                    finish()
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

    private val getInfo: (Int) -> Unit = {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService().getRoleInfo(it)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, RoleInfoResponse::class.java)
                    et_name.setText(t.data.roleName)
                    appMenuIds.addAll(t.data.appMenuIds)
                    getTreeList()
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    private val getTreeList: () -> Unit = {
        LoadingUtils.showLoading(this, "加载中")
        RetrofitUtils.get()
            .getJson(TreeListUrl, HashMap(), this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, TreeListBean::class.java)
                    for (data: TreeListData in t.data) {
                        var isTrue = true
                        for (i: Int in appMenuIds) {
                            if (data.menuId == i) {
                                isTrue = false
                                treeList.add(
                                    Node(
                                        data.parentId.toString(),
                                        data.menuId.toString(),
                                        data.menuName,
                                        true
                                    )
                                )
                                LogUtils.d("tag", data.menuId, data.menuName)
                            }
                        }
                        if (isTrue)
                            treeList.add(
                                Node(
                                    data.parentId.toString(),
                                    data.menuId.toString(),
                                    data.menuName,
                                    false
                                )
                            )
                    }
                    treeView = TreeListView(this@LimitsActivity, treeList)
                    treeView!!.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                    )
                    treeView!!.get()
                    tree_view.addView(treeView)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}