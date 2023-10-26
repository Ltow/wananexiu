package com.bossed.waej.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.PersonMsgBean
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_person_data.*

import com.blankj.utilcode.util.*
import com.bossed.waej.adapter.WheelViewAdapter
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.customview.PopWindow.Companion.RIGHT_BOTTOM
import com.bossed.waej.eventbus.EBNewCustomer
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.RoleResponse
import com.bossed.waej.javebean.RoleRow
import com.bossed.waej.util.*
import com.luck.picture.lib.config.PictureConfig
import org.greenrobot.eventbus.EventBus

class PersonDataActivity : BaseActivity(), OnNoRepeatClickListener {
    private var avatar = ""
    private var certificate = ""
    private var startIntentType = '0'
    private var roleID = -1
    private var mCountDownTimeUtil: CountDownTimeUtil? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_person_data
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_person_data)
        when (intent.getStringExtra("personType")) {
            "new" -> {
                tv_disable.visibility = View.GONE
                tv_create_time.text = TimeUtils.getNowString()
            }
            "open" -> {
                getPersonMsg()
            }
        }
    }

    override fun initListener() {
        tb_person_data.setOnTitleBarListener(object : OnTitleBarListener {
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
            R.id.tv_create_time -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                KeyboardUtils.hideSoftInput(window)
                PopupWindowUtils.get()
                    .showSelDateTimePop(this, 1) {
                        tv_create_time.text = DateFormatUtils.get().formatDateAndTime(it)
                    }
            }
            R.id.tv_disable -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }
            R.id.iv_head -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, SelectPicActivity::class.java)
                intent.putExtra("selNum", 1)
                intent.putExtra("selMode", PictureConfig.SINGLE)
                startIntentType = '1'
                launcher.launch(intent)
            }
            R.id.iv_upload -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, UploadPicActivity::class.java)
                intent.putExtra("url", certificate)
                intent.putExtra("type", "certificate")
                startIntentType = '2'
                launcher.launch(intent)
            }
            R.id.tv_save -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(et_name.text.toString()) -> ToastUtils.showShort("请输入姓名")
                    TextUtils.isEmpty(tv_create_time.text.toString()) -> ToastUtils.showShort("请选择入职时间")
                    TextUtils.isEmpty(tv_role.text.toString()) -> ToastUtils.showShort("请选择角色")
                    !ctv_own.isChecked && !ctv_all.isChecked -> ToastUtils.showShort("请选择数据权限")
                    else -> {
                        when (intent.getStringExtra("personType")) {
                            "new" -> {
                                newPerson()
                            }
                            "open" -> {
                                updatePerson()
                            }
                        }
                    }
                }
            }
            R.id.tv_role -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                KeyboardUtils.hideSoftInput(window)
                getRoleList()
            }
            R.id.tv_alert -> {//权限提示
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                showAlert()
            }
            R.id.ctv_own -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_own.isChecked = !ctv_own.isChecked
                ctv_all.isChecked = !ctv_own.isChecked
            }
            R.id.ctv_all -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_all.isChecked = !ctv_all.isChecked
                ctv_own.isChecked = !ctv_all.isChecked
            }
            R.id.tv_get_verification -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (TextUtils.isEmpty(et_userName.text.toString()) || et_userName.text.toString().length < 11) {
                    ToastUtils.showShort("请输入手机号")
                    return
                }
                val params = HashMap<String, String>()
                params["phone"] = et_userName.text.toString()
                params["codeType"] = "1"
                RetrofitUtils.get().getJson(
                    UrlConstants.VerificationUrl, params, this,
                    object : RetrofitUtils.OnCallBackListener {
                        override fun onSuccess(s: String) {
                            mCountDownTimeUtil = CountDownTimeUtil(tv_get_verification)
                            mCountDownTimeUtil!!.runTimer()
                        }

                        override fun onFailed(e: String) {
                            ToastUtils.showShort(e)
                        }
                    })
            }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK)
                when (startIntentType) {
                    '1' -> {
                        val bundle = it.data!!.extras
                        val imgName = bundle!!.getString("name")!!
                        val imgLocalPath = bundle.getString("path")!!
                        GlideUtils.get().loadHead(this@PersonDataActivity, imgLocalPath, iv_head)
                        if (TextUtils.isEmpty(imgName) || TextUtils.isEmpty(imgLocalPath))
                            return@registerForActivityResult
                        val dialog = Dialog(this, R.style.Dialog)
                        val contentView = View.inflate(this, R.layout.layout_progress_dialog, null)
                        dialog.setContentView(contentView)
                        dialog.setCancelable(false)
                        dialog.setCanceledOnTouchOutside(false)
                        val progressBar = contentView.findViewById<ProgressBar>(R.id.pb_progress)
                        dialog.show()
                        val objectKey = "waej3/" + SPUtils.getInstance()
                            .getInt("shopId") + "/" + System.currentTimeMillis() + imgName
                        OssUtils.get().asyncPutImage(
                            objectKey,
                            imgLocalPath,
                            this,
                            object : OssUtils.OnOssCallBackListener {
                                override fun onSuccess(float: Float) {
                                    dialog.dismiss()
                                    ToastUtils.showShort("上传成功，用时：${float}秒")
                                    avatar = UrlConstants.BaseOssUrl + objectKey
                                }

                                override fun onFailed(e: String) {
                                    ToastUtils.showShort(e)
                                    dialog.dismiss()
                                }

                                override fun onProgress(progress: Int) {
                                    progressBar.progress = progress
                                }
                            })
                    }
                    '2' -> {
                        certificate = it.data!!.getStringExtra("url")!!
                        tv_certificate.text = "已上传"
                        GlideUtils.get()
                            .loadDoorTitlePic(this, certificate, iv_upload)
                    }
                }
        }

    private val showAlert: () -> Unit = {
        val popWindow = PopWindow.Builder(this).setView(R.layout.layout_pop_alert_person)
            .setWidthAndHeight(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ).setOutsideTouchable(true)
            .setChildrenView { contentView, pop ->
                contentView.setOnClickListener { pop.dismiss() }
            }.create()
        popWindow.isClippingEnabled = false
        popWindow.isFocusable = true
        popWindow.showOnTargetBottom(tv_alert, RIGHT_BOTTOM, ConvertUtils.dp2px(-20f), 0)
    }

    private fun showRolePop(items: ArrayList<RoleRow>) {
        val popWindow = PopWindow.Builder(this)
            .setView(R.layout.layout_pop_made_type)
            .setWidthAndHeight(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            .setOutsideTouchable(true)
            .setAnimStyle(R.style.BottomAnimation)
            .setBackGroundLevel(0.5f)
            .setChildrenView { contentView, pop ->
                val rv_sel_role = contentView.findViewById<RecyclerView>(R.id.rv_sel_role)
                if (BarUtils.isNavBarVisible(window)) {
                    BarUtils.setNavBarLightMode(window, true)
                    val layoutParams = rv_sel_role.layoutParams as LinearLayout.LayoutParams
                    layoutParams.bottomMargin = BarUtils.getNavBarHeight()
                    rv_sel_role.layoutParams = layoutParams
                }
                rv_sel_role.layoutManager = LinearLayoutManager(this)
                val adapter = WheelViewAdapter(items)
                adapter.bindToRecyclerView(rv_sel_role)
                adapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
                adapter.setOnItemClickListener { _, _, position ->
                    tv_role.text = items[position].roleName
                    roleID = items[position].roleId
                    pop.dismiss()
                }
                contentView.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                    pop.dismiss()
                }
            }
            .create()
        popWindow.isClippingEnabled = false
        popWindow.showAtLocation(rl_content, Gravity.BOTTOM, 0, 0)
    }


    private fun updatePerson() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["address"] = et_address.text.toString()
        params["id"] = intent.getIntExtra("id", 0)
        params["avatar"] = avatar
        params["certificate"] = certificate
        params["createBy"] = ""
        if (!TextUtils.isEmpty(tv_create_time.text.toString()))
            params["entryTime"] = tv_create_time.text.toString()
        params["emergencyName"] = et_emergencyName.text.toString()
        params["emergencyPhone"] = et_emergencyPhone.text.toString()
        params["idCard"] = et_idCard.text.toString()
        params["name"] = et_name.text.toString()
        params["phone"] = et_userName.text.toString()
        params["userName"] = et_userName.text.toString()
        params["password"] = "123456"
        params["roleName"] = tv_role.text.toString()
        params["userRole"] = roleID
        params["code"] = et_code.text.toString()
        params["skillDescription"] = et_skillDescription.text.toString()
        params["dataScope"] = when {
            ctv_all.isChecked -> "1"
            ctv_own.isChecked -> "5"
            else -> "5"
        }
        params["remark"] = et_remarks.text.toString()
        params["madeRate"] = et_commission_coefficient.text.toString()
        params["madeAdd"] = et_madeAdd.text.toString()
        params["madeType"] = when (tv_madeType.text.toString()) {
            "每月" -> 1
            "每季" -> 2
            "每年" -> 3
            else -> 0
        }
        RetrofitUtils.get().putJson(
            UrlConstants.UpdatePersonUrl,
            params,
            this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    ToastUtils.showShort(t.msg)
                    finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getPersonMsg() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getPersonMsg(intent.getIntExtra("id", 0))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<PersonMsgBean>() {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: PersonMsgBean) {
                    when (t.code) {
                        200 -> {
                            certificate = t.data.certificate
                            GlideUtils.get()
                                .loadDoorTitlePic(
                                    this@PersonDataActivity,
                                    t.data.certificate,
                                    iv_upload
                                )
                            avatar = t.data.avatar
                            GlideUtils.get()
                                .loadHead(this@PersonDataActivity, t.data.avatar, iv_head)
                            et_name.setText(t.data.name)
                            et_idCard.setText(t.data.idCard)
                            et_address.setText(t.data.address)
                            tv_create_time.text = t.data.entryTime
                            et_emergencyName.setText(t.data.emergencyName)
                            et_emergencyPhone.setText(t.data.emergencyPhone)
                            et_remarks.setText(t.data.remark)
                            et_commission_coefficient.setText(t.data.madeRate.toString())
                            et_madeAdd.setText(t.data.madeAdd.toString())
                            et_userName.setText(t.data.userName)
                            et_skillDescription.setText(t.data.skillDescription)
                            when (t.data.dataScope) {
                                "5" -> ctv_own.isChecked = true
                                "1" -> ctv_all.isChecked = true
                            }
                            tv_role.text = t.data.roleName
                            roleID = t.data.userRole
                            tv_madeType.text = when (t.data.madeType) {
                                1 -> "每月"
                                2 -> "每季"
                                3 -> "每年"
                                else -> ""
                            }
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@PersonDataActivity)
                        }
                        703 -> {
                            PopupWindowUtils.get()
                                .showSetConfirmAlertDialog(mContext, t.msg, "去购买", "") {
                                    mContext.startActivity(
                                        Intent(
                                            mContext,
                                            BuyProductActivity::class.java
                                        )
                                    )
                                }
                        }
                        else -> {
                            ToastUtils.showShort(t.msg)
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                }
            })
    }

    private fun newPerson() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["address"] = et_address.text.toString()
        params["avatar"] = avatar
        params["certificate"] = certificate
        params["createBy"] = ""
        params["entryTime"] = tv_create_time.text.toString()
        params["emergencyName"] = et_emergencyName.text.toString()
        params["emergencyPhone"] = et_emergencyPhone.text.toString()
        params["idCard"] = et_idCard.text.toString()
        params["name"] = et_name.text.toString()
        params["phone"] = et_userName.text.toString()
        params["roleName"] = tv_role.text.toString()
        params["userRole"] = roleID
        params["userName"] = et_userName.text.toString()
        params["password"] = "123456"
        params["code"] = et_code.text.toString()
        params["skillDescription"] = et_skillDescription.text.toString()
        params["dataScope"] = when {
            ctv_all.isChecked -> "1"
            ctv_own.isChecked -> "5"
            else -> "5"
        }
        params["remark"] = et_remarks.text.toString()
        params["madeRate"] = et_commission_coefficient.text.toString()
        params["madeAdd"] = et_madeAdd.text.toString()
        params["madeType"] = when (tv_madeType.text.toString()) {
            "每月" -> 1
            "每季" -> 2
            "每年" -> 3
            else -> 0
        }
        RetrofitUtils.get().postJson(
            UrlConstants.NewPersonUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    ToastUtils.showShort(t.msg)
                    finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private val getRoleList = {
        LoadingUtils.showLoading(this, "加载中")
        RetrofitUtils.get().getJson(
            UrlConstants.RoleListUrl, HashMap(), this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, RoleResponse::class.java)
                    if (t.code == 200) {
                        if (t.rows.isEmpty())
                            PopupWindowUtils.get().showSetConfirmAlertDialog(
                                mContext,
                                "角色档案为空，请添加部门角色后继续操作！",
                                "去添加",
                                ""
                            ) {
                                val intent = Intent(mContext, DepartmentActivity::class.java)
                                intent.putExtra("type", "mine")
                                startActivity(intent)
                            }
                        else
                            showRolePop(t.rows as ArrayList)
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().post(EBNewCustomer(true))
        if (mCountDownTimeUtil != null)
            mCountDownTimeUtil!!.cancel()
    }
}