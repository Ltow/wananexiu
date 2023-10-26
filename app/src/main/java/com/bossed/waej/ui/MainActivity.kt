package com.bossed.waej.ui

import android.Manifest
import android.animation.AnimatorInflater
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.text.TextUtils
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.activityresultcontract.StartInstallsPermissionContract
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseFragmentPagerAdapter
import com.bossed.waej.customview.MyAlertDialog
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.eventbus.EBBackPage
import com.bossed.waej.eventbus.EBLogout
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.MineResponse
import com.bossed.waej.ui.fragment.*
import com.bossed.waej.util.*
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import kotlin.system.exitProcess

class MainActivity : BaseActivity(), OnNoRepeatClickListener {
    private val fragments: MutableList<Fragment> = ArrayList()
    private var pagerAdapter: BaseFragmentPagerAdapter? = null
    private var forcedToup = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        fragments.add(PageFragment())
        fragments.add(FinanceFragment())
        fragments.add(SearchFragment())
        fragments.add(EJiaFragment())
        fragments.add(BVDCFragment())
        fragments.add(OnlineFragment())
        fragments.add(MineFragment())
        pagerAdapter = BaseFragmentPagerAdapter(supportFragmentManager, fragments)
        ns_vp_main.adapter = pagerAdapter
        ns_vp_main.setNoScroll(true)
        onCheckedChange(0)
        if (!TextUtils.isEmpty(intent.getStringExtra("new"))) {
            Handler().postDelayed({
                val popWindow = PopWindow.Builder(this).setView(R.layout.layout_pop_welfare)
                    .setWidthAndHeight(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    ).setOutsideTouchable(true)
                    .setAnimStyle(R.style.CenterAnimation)
                    .setBackGroundLevel(0.5f)
                    .setChildrenView { contentView, pop ->
                        contentView.findViewById<View>(R.id.tv_confirm).setOnClickListener {
                            startActivity(Intent(this, MyProductActivity::class.java))
                            pop.dismiss()
                        }
                        contentView.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                            pop.dismiss()
                        }
                    }.create()
                popWindow.isClippingEnabled = false
                popWindow.isFocusable = true
                popWindow.showAtLocation(ll_content, Gravity.CENTER, 0, 0)
            }, 1000)
        }
    }

    override fun initListener() {
        CheckVersionUtils.get()
            .checkVersion(this, object : CheckVersionUtils.OnCheckVersionListener {
                override fun onRequest(forcedToup: Int) {
                    this@MainActivity.forcedToup = forcedToup
                    requestPermission()
                }

                override fun downLoad(forcedToup: Int) {
                    this@MainActivity.forcedToup = forcedToup
                    CheckVersionUtils.get().download(this@MainActivity,
                        object : CheckVersionUtils.OnDownloadListener {
                            @RequiresApi(Build.VERSION_CODES.O)
                            override fun onRequest() {
                                installsPermissionLauncher.launch(packageName)
                            }

                            override fun onSuccess(path: String) {
                                CheckVersionUtils.get().installApk(this@MainActivity)
                            }
                        })
                }
            })
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.rl_home -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onCheckedChange(0)
            }

            R.id.rl_finance -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onCheckedChange(1)
            }

            R.id.rl_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onCheckedChange(2)
            }

            R.id.rl_e_jia -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onCheckedChange(3)
            }

            R.id.rl_bvdc -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onCheckedChange(4)
            }

            R.id.rl_online -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onCheckedChange(5)
            }

            R.id.rl_mine -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onCheckedChange(6)
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
                            CheckVersionUtils.get().installApk(this@MainActivity)
                        }
                    })
            } else {
                ToastUtils.showShort("请手动开启存储权限，否则将无法正常下载应用")
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
//                            CheckVersionUtils.get().installApk(this@MainActivity)
//                        }
//                    })
//            else {
//                ToastUtils.showShort("请手动开启存储权限，否则无法正常下载更新应用")
//                if (forcedToup == 1)
//                    exitProcess(0)
//            }
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val installsPermissionLauncher =
        registerForActivityResult(StartInstallsPermissionContract()) {
            if (it == RESULT_CANCELED) {
                ToastUtils.showShort("请手动开启安装权限，否则无法正常安装最新应用")
                if (forcedToup == 1)
                    exitProcess(0)
            } else {
                if (packageManager.canRequestPackageInstalls())
                    CheckVersionUtils.get().installApk(this@MainActivity)
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

    private fun onCheckedChange(int: Int) {
        tv_home_page_main.setTextColor(Color.parseColor("#999999"))
        iv_home_page_main.setImageResource(R.mipmap.icon_1201)
        tv_finance_page_main.setTextColor(Color.parseColor("#999999"))
        iv_finance_page_main.setImageResource(R.mipmap.icon_financeun)
        tv_search_main.setTextColor(Color.parseColor("#999999"))
        iv_search_main.setImageResource(R.mipmap.icon1260)
        tv_e_jia_main.setTextColor(Color.parseColor("#999999"))
        iv_e_jia_main.setImageResource(R.mipmap.icon12621)
        tv_mine_main.setTextColor(Color.parseColor("#999999"))
        iv_mine_main.setImageResource(R.mipmap.icon1262)
        tv_bvdc.setTextColor(Color.parseColor("#999999"))
        iv_bvdc.setImageResource(R.mipmap.icon_bvdcun)
        tv_online.setTextColor(Color.parseColor("#999999"))
        iv_online.setImageResource(R.mipmap.icon_un_online)
        when (int) {
            0 -> {
                tv_home_page_main.setTextColor(Color.parseColor("#333333"))
                iv_home_page_main.setImageResource(R.mipmap.icon1202)
                ns_vp_main.currentItem = 0
//                setAnimator(rl_home)
            }

            1 -> {
                tv_finance_page_main.setTextColor(Color.parseColor("#333333"))
                iv_finance_page_main.setImageResource(R.mipmap.icon_finance)
                ns_vp_main.currentItem = 1
//                setAnimator(rl_finance)
            }

            2 -> {
                ns_vp_main.currentItem = 2
            }

            3 -> {
                ns_vp_main.currentItem = 3
            }

            4 -> {
                tv_bvdc.setTextColor(Color.parseColor("#333333"))
                iv_bvdc.setImageResource(R.mipmap.icon_bvdc)
                ns_vp_main.currentItem = 4
//                setAnimator(rl_bvdc)
            }

            5 -> {
                tv_online.setTextColor(Color.parseColor("#333333"))
                iv_online.setImageResource(R.mipmap.icon_online)
                ns_vp_main.currentItem = 5
//                setAnimator(rl_online)
            }

            6 -> {
                tv_mine_main.setTextColor(Color.parseColor("#333333"))
                iv_mine_main.setImageResource(R.mipmap.icon12622)
                ns_vp_main.currentItem = 6
//                setAnimator(rl_mine)
            }
        }
    }

    private fun getMenuList() {
        RetrofitUtils.get().getJson(
            UrlConstants.MenuPermsUrl, HashMap(), this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    SPUtils.getInstance().put("menus", s)
                    getUserInfo()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getUserInfo() {
        RetrofitUtils.get().getJson(
            UrlConstants.Mine_Msg, HashMap(), this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag",s)
                    val t = GsonUtils.fromJson(s, MineResponse::class.java)
                    SPUtils.getInstance().put("userType", t.data.sysUser.userType)
                    SPUtils.getInstance().put("nickName", t.data.sysUser.nickName)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private var exitTime: Long = 0

//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event!!.action == KeyEvent.ACTION_DOWN) {
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                ToastUtils.showShort("再按一次退出程序")
//                exitTime = System.currentTimeMillis()
//            } else
//                finish()
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }

    override fun onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 1500) {
            ToastUtils.showShort("再按一次退出程序")
            exitTime = System.currentTimeMillis()
        } else
            exitProcess(0)
    }

    @Subscribe
    fun onFinishedBack(eb: EBLogout) {
        if (eb.finished)
            finish()
    }

    @Subscribe
    fun onMessageEvent(eb: EBBackPage) {
        if (eb.isGo)
            onCheckedChange(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        getMenuList()

    }

    private fun setAnimator(view: View) {
        val animator = AnimatorInflater.loadAnimator(this, R.animator.anim_file)
        animator.setTarget(view)
        animator.interpolator = BounceInterpolator()
        animator.start()
    }
}