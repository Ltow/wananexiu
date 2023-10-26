package com.bossed.waej.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.activityresultcontract.StartInstallsPermissionContract
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.eventbus.EBExchange
import com.bossed.waej.eventbus.EBLogout
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.http.UrlConstants.LogOutUrl
import com.bossed.waej.javebean.ExchangeResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_print_setting.*
import kotlinx.android.synthetic.main.activity_system_settings.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.math.BigDecimal
import kotlin.system.exitProcess

class SystemSettingsActivity : BaseActivity(), OnNoRepeatClickListener {
    private var forcedToup = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_system_settings
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_system_settings)
        tv_version.text = "Ver:${AppUtils.getAppVersionName()}"
        tv_cache.text = "${CacheDiskStaticUtils.getCacheSize() / 1024}M"
    }

    override fun initListener() {
        tb_system_settings.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.rl_exchange -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showEditPop(this, rl_content, "使用兑换码", "兑换码", "请输入兑换码") {
                    LoadingUtils.showLoading(this, "加载中...")
                    Api.getInstance().getApiService().getExchange(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : BaseResourceObserver<String>() {
                            override fun onComplete() {
                                LoadingUtils.dismissLoading()
                            }

                            override fun onSubscribe(d: Disposable) {
                            }

                            override fun onNext(s: String) {
                                LogUtils.d("tag", s)
                                val t = GsonUtils.fromJson(s, ExchangeResponse::class.java)
                                if (t.code == 200) {
                                    val intent = Intent(
                                        this@SystemSettingsActivity,
                                        ExchangeSuccessActivity::class.java
                                    )
                                    intent.putExtra("succ", s)
                                    startActivity(intent)
                                    finish()
                                }
                                ToastUtils.showShort(t.msg)
                            }

                            override fun onError(throwable: Throwable) {
                                ToastUtils.showShort(throwable.message)
                                LoadingUtils.dismissLoading()
                            }
                        })
                }
            }
            R.id.rl_print_setting -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this, PrintSettingActivity::class.java))
            }
            R.id.rl_yszc -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showHtmlPop(UrlConstants.PolicyUrl, this, rl_content)
            }
            R.id.tv_logout -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                RetrofitUtils.get()
                    .postJson(LogOutUrl, HashMap<String, Any>(), this,
                        object : RetrofitUtils.OnCallBackListener {
                            override fun onSuccess(s: String) {
                                val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                                if (t.code == 200) {
                                    val intent = Intent(
                                        this@SystemSettingsActivity,
                                        LoginActivity::class.java
                                    )
                                    startActivity(intent)
                                    EventBus.getDefault().post(EBLogout(true))
                                    finish()
                                }
                            }

                            override fun onFailed(e: String) {
                                ToastUtils.showShort(e)
                            }
                        })
            }
            R.id.rl_cache -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showConfirmPop(this, "是否清理缓存？") {
                    CacheDiskStaticUtils.clear()
                    tv_cache.text = "${
                        BigDecimal(CacheDiskStaticUtils.getCacheSize()).divide(
                            BigDecimal(1024)
                        )
                    }M"
                }
            }
            R.id.rl_version -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                CheckVersionUtils.get()
                    .checkVersion(this, object : CheckVersionUtils.OnCheckVersionListener {
                        override fun onRequest(forcedToup: Int) {
                            this@SystemSettingsActivity.forcedToup = forcedToup
                            requestPermission()
                        }

                        override fun downLoad(forcedToup: Int) {
                            this@SystemSettingsActivity.forcedToup = forcedToup
                            CheckVersionUtils.get().download(this@SystemSettingsActivity,
                                object : CheckVersionUtils.OnDownloadListener {
                                    @RequiresApi(Build.VERSION_CODES.O)
                                    override fun onRequest() {
                                        installsPermissionLauncher.launch(packageName)
                                    }

                                    override fun onSuccess(path: String) {
                                        CheckVersionUtils.get()
                                            .installApk(this@SystemSettingsActivity)
                                    }
                                })
                        }
                    })
            }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[Manifest.permission.WRITE_EXTERNAL_STORAGE]!! && it[Manifest.permission.READ_EXTERNAL_STORAGE]!!) {
                CheckVersionUtils.get()
                    .download(this, object : CheckVersionUtils.OnDownloadListener {
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onRequest() {
                            installsPermissionLauncher.launch(packageName)
                        }

                        override fun onSuccess(path: String) {
                            CheckVersionUtils.get().installApk(this@SystemSettingsActivity)
                        }
                    })
            } else {
                ToastUtils.showShort("请手动开启存储权限")
            }
        }

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
//                            CheckVersionUtils.get().installApk(this@SystemSettingsActivity)
//                        }
//                    })
//            else {
//                ToastUtils.showShort("请手动开启存储权限")
//                if (forcedToup == 1)
//                    exitProcess(0)
//            }
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val installsPermissionLauncher =
        registerForActivityResult(StartInstallsPermissionContract()) {
            if (it == RESULT_CANCELED) {
                ToastUtils.showShort("请手动开启未知应用安装权限")
                if (forcedToup == 1)
                    exitProcess(0)
            } else {
                if (packageManager.canRequestPackageInstalls())
                    CheckVersionUtils.get().installApk(this@SystemSettingsActivity)
            }
        }

    private val requestPermission = {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            filesDirLauncher.launch(null)
//        } else {
        launcher.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
//        }
    }

    @Subscribe
    fun onMessageEvent(eb: EBExchange) {
        if (eb.isBack)
            finish()
    }

    override fun onResume() {
        super.onResume()
        tv_print_norms.text =
            SPUtils.getInstance().getString("PRINTER_NORMS", States.PrinterPageSize.PRINTER_58mm)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}