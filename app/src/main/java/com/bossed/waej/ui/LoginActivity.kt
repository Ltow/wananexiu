package com.bossed.waej.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.activityresultcontract.StartInstallsPermissionContract
import com.bossed.waej.adapter.ItemSelectedAdapter
import com.bossed.waej.adapter.TestAccountAdapter
import com.bossed.waej.base.BaseMVPActivity
import com.bossed.waej.contract.LoginContract
import com.bossed.waej.customview.BottomDialog
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.http.UrlConstants.PolicyUrl
import com.bossed.waej.javebean.LoginResponse
import com.bossed.waej.javebean.TestAccountResponse
import com.bossed.waej.presenter.LoginPresenter
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.system.exitProcess

class LoginActivity : BaseMVPActivity<LoginPresenter>(), OnNoRepeatClickListener,
    LoginContract.LoginView {
    private var registrationId = ""
    private var exitTime: Long = 0
    private var forcedToup = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        registrationId = JPushInterface.getRegistrationID(this)
    }

    override fun initListener() {
        cb_agree.setOnCheckedChangeListener { buttonView, isChecked ->
            SPUtils.getInstance().put("agree", isChecked)
            buttonClickState(isChecked, tv_login)
        }
        CheckVersionUtils.get()
            .checkVersion(this, object : CheckVersionUtils.OnCheckVersionListener {
                override fun onRequest(forcedToup: Int) {
                    this@LoginActivity.forcedToup = forcedToup
                    requestPermission()
                }

                override fun downLoad(forcedToup: Int) {
                    this@LoginActivity.forcedToup = forcedToup
                    CheckVersionUtils.get().download(this@LoginActivity,
                        object : CheckVersionUtils.OnDownloadListener {
                            @RequiresApi(Build.VERSION_CODES.O)
                            override fun onRequest() {
                                installsPermissionLauncher.launch(packageName)
                            }

                            override fun onSuccess(path: String) {
                                CheckVersionUtils.get().installApk(this@LoginActivity)
                            }
                        })
                }
            })
    }

    /**
     * 小于android 30 dir权限
     */
    private val dirLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[Manifest.permission.WRITE_EXTERNAL_STORAGE]!! && it[Manifest.permission.READ_EXTERNAL_STORAGE]!!) {
                CheckVersionUtils.get()
                    .download(this, object : CheckVersionUtils.OnDownloadListener {
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onRequest() {
                            installsPermissionLauncher.launch(packageName)
                        }

                        override fun onSuccess(path: String) {
                            CheckVersionUtils.get().installApk(this@LoginActivity)
                        }
                    })
            } else {
                ToastUtils.showShort("请手动开启存储权限")
            }
        }

    /**
     * 大于等于android 30 dir权限
     */
//    @RequiresApi(Build.VERSION_CODES.R)
//    private val filesDirLauncher = registerForActivityResult(StartFilesDirContract()) {
//        if (it == RESULT_CANCELED)
//            if (Environment.isExternalStorageManager())
//                CheckVersionUtils.get()
//                    .download(this, object : CheckVersionUtils.OnDownloadListener {
//                        override fun onRequest() {
//                            installsPermissionLauncher.launch(packageName)
//                        }
//
//                        override fun onSuccess(path: String) {
//                            CheckVersionUtils.get().installApk(this@LoginActivity)
//                        }
//                    })
//            else {
//                ToastUtils.showShort("请手动开启存储权限")
//                if (forcedToup == 1)
//                    exitProcess(0)
//            }
//    }

    /**
     * 未知应用安装
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private val installsPermissionLauncher =
        registerForActivityResult(StartInstallsPermissionContract()) {
            if (it == RESULT_CANCELED) {
                ToastUtils.showShort("请手动开启未知应用安装权限")
                if (forcedToup == 1)
                    exitProcess(0)
            } else {
                if (packageManager.canRequestPackageInstalls())
                    CheckVersionUtils.get().installApk(this@LoginActivity)
            }
        }

    private val requestPermission = {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            filesDirLauncher.launch(null)
//        } else {
        dirLauncher.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
//        }
    }

    override fun onRepeatClick(v: View?) {
        when (v!!.id) {
            R.id.tv_login -> {//登录
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (cb_agree.isChecked) {
                    when {
                        TextUtils.isEmpty(et_phone_login.text.toString()) -> ToastUtils.showShort("请输入账号")
                        TextUtils.isEmpty(et_pass_login.text.toString()) -> ToastUtils.showShort("请输入密码")
                        else -> {
                            val params = HashMap<String, String>()
                            params["username"] = et_phone_login.text.toString()
                            params["code"] = ""
                            params["password"] = et_pass_login.text.toString()
                            LoadingUtils.showLoading(this, "加载中...")
                            mPresenter?.login(this, params)
                        }
                    }
                } else
                    ToastUtils.showLong("请勾选并阅读注册及隐私协议")
            }
            R.id.tv_sel_test -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                LoadingUtils.showLoading(this, "加载中...")
                RetrofitUtils.get().getJson(
                    "km/index/getTestUserList", HashMap(), this,
                    object : RetrofitUtils.OnCallBackListener {
                        override fun onSuccess(s: String) {
                            LogUtils.d("tag", s)
                            val t = GsonUtils.fromJson(s, TestAccountResponse::class.java)
                            if (t.data.isNotEmpty()) {
                                BottomDialog(this@LoginActivity).create(R.layout.layout_pop_sel_test)
                                    .setCanceledOnTouchOutside(true)
                                    .setViewInterface { view, dialog ->
                                        val recyclerView =
                                            view.findViewById<RecyclerView>(R.id.rv_test)
                                        recyclerView.layoutManager =
                                            LinearLayoutManager(this@LoginActivity)
                                        val adapter = TestAccountAdapter(t.data as ArrayList)
                                        adapter.bindToRecyclerView(recyclerView)
                                        adapter.setOnItemClickListener { adapter, view, position ->
                                            et_phone_login.setText(t.data[position].userName)
                                            et_pass_login.setText(t.data[position].password)
                                            dialog.dismiss()
                                        }
                                    }.show()
                            }
                        }

                        override fun onFailed(e: String) {
                            ToastUtils.showShort(e)
                        }
                    })
            }
            R.id.tv_register -> {//注册
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this, RegisterActivity::class.java))
            }
            R.id.tv_forget_pass -> {//忘记密码
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this, ForgetPasswordActivity::class.java))
            }
            R.id.tv_agreement -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showHtmlPop(UrlConstants.AgreementUrl, this, rl_content)
            }
            R.id.tv_policy -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showHtmlPop(PolicyUrl, this, rl_content)
            }
        }
    }

    override fun onSuccess(response: LoginResponse) {
        SPUtils.getInstance().put("username", et_phone_login.text.toString())
        SPUtils.getInstance().put("password", et_pass_login.text.toString())
        SPUtils.getInstance().put("remember", true)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun showError(e: String) {
        ToastUtils.showShort(e)
    }

    override fun getPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event!!.action == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showShort("再按一次退出程序")
                exitTime = System.currentTimeMillis()
            } else
                finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        buttonClickState(SPUtils.getInstance().getBoolean("agree", false), tv_login)
        et_phone_login.setText(SPUtils.getInstance().getString("username"))
        et_pass_login.setText(SPUtils.getInstance().getString("password"))
        cb_agree.isChecked = SPUtils.getInstance().getBoolean("agree", false)
    }
}
