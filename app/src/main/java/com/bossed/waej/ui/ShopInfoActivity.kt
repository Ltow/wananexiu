package com.bossed.waej.ui

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.TimePicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.IdTypeAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.eventbus.EBLaKaLaBack
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.http.UrlConstants.PostFileUrl
import com.bossed.waej.http.UrlConstants.ShopUrl
import com.bossed.waej.javebean.IdTypeBean
import com.bossed.waej.javebean.PostFileResponse
import com.bossed.waej.javebean.ShopInfoResponse
import com.bossed.waej.ui.amap.AMapActivity
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.OssUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.luck.picture.lib.config.PictureConfig
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_remit_account.ll_content
import kotlinx.android.synthetic.main.activity_shop_info.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * 店铺详细信息
 * @author 刘磊
 */
class ShopInfoActivity : BaseActivity(), View.OnClickListener, OnLongClickListener {
    private var selectPicType = 0
    private var businessBegin = ""
    private var businessEnd = ""
    private var idCardFront = ""//身份证正面
    private var lklIdCardFront = ""//拉卡拉身份证正面
    private var idCardBack = ""//身份证背面
    private var lklIdCardBack = ""//拉卡拉身份证背面
    private var businessLicense = ""//营业执照
    private var lklBusinessLicense = ""//拉卡拉营业执照
    private var doorPhoto = ""//门头照
    private var lklDoorPhoto = ""//拉卡拉门头照
    private var shopImage = ""//门店照
    private var longitude = ""
    private var latitude = ""
    private var province = ""
    private var city = ""
    private var county = ""
    private var auditStatus = 0//拉卡拉审核状态
    private val shopTypes = ArrayList<IdTypeBean>()
    private var shopType = ""

    init {
        shopTypes.add(IdTypeBean("商用车（货车）", "SY"))
        shopTypes.add(IdTypeBean("乘用车（私家车）", "CY"))
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_shop_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_shop_info)
        getInfo()
    }

    override fun initListener() {
        tb_shop_info.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        iv_sfz_zm.setOnLongClickListener(this)
        iv_sfz_bm.setOnLongClickListener(this)
        iv_license.setOnLongClickListener(this)
        iv_door_head.setOnLongClickListener(this)
        iv_shop_pic.setOnLongClickListener(this)
    }

    override fun onLongClick(v: View?): Boolean {
        when (v?.id) {
            R.id.iv_sfz_zm -> {
                PopupWindowUtils.get().showConfirmPop(this, "是否更换当前图片？") {
                    onClick(tv_sfz_zm)
                }
            }

            R.id.iv_sfz_bm -> {
                PopupWindowUtils.get().showConfirmPop(this, "是否更换当前图片？") {
                    onClick(tv_sfz_bm)
                }
            }

            R.id.iv_license -> {
                PopupWindowUtils.get().showConfirmPop(this, "是否更换当前图片？") {
                    onClick(tv_license)
                }
            }

            R.id.iv_door_head -> {
                PopupWindowUtils.get().showConfirmPop(this, "是否更换当前图片？") {
                    onClick(tv_door_head)
                }
            }

            R.id.iv_shop_pic -> {
                PopupWindowUtils.get().showConfirmPop(this, "是否更换当前图片？") {
                    onClick(tv_shop_pic)
                }
            }

        }
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_commit -> {//提交
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when (auditStatus) {
                    3 -> startActivity(
                        Intent(
                            this@ShopInfoActivity,
                            RemitAccountActivity::class.java
                        )
                    )

                    4 -> startActivity(
                        Intent(
                            this@ShopInfoActivity,
                            RemitAccountActivity::class.java
                        )
                    )

                    else -> commit()
                }
            }

            R.id.tv_shop_type -> {//店铺类型
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                showTypePop()
            }

            R.id.iv_sfz_zm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showPhotoPop(idCardFront, this, ll_content)
            }

            R.id.iv_sfz_bm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showPhotoPop(idCardBack, this, ll_content)
            }

            R.id.iv_license -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showPhotoPop(businessLicense, this, ll_content)
            }

            R.id.iv_door_head -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showPhotoPop(doorPhoto, this, ll_content)
            }

            R.id.iv_shop_pic -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showPhotoPop(shopImage, this, ll_content)
            }


            R.id.tv_sfz_zm -> {//负责人身份证-正面
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, SelectPicActivity::class.java)
                intent.putExtra("selNum", 1)
                intent.putExtra("selMode", PictureConfig.SINGLE)
                picLauncher.launch(intent)
                selectPicType = 0
            }

            R.id.tv_sfz_bm -> {//负责人身份证-背面
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, SelectPicActivity::class.java)
                intent.putExtra("selNum", 1)
                intent.putExtra("selMode", PictureConfig.SINGLE)
                picLauncher.launch(intent)
                selectPicType = 1
            }

            R.id.tv_license -> {//营业执照
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, SelectPicActivity::class.java)
                intent.putExtra("selNum", 1)
                intent.putExtra("selMode", PictureConfig.SINGLE)
                picLauncher.launch(intent)
                selectPicType = 2
            }

            R.id.tv_door_head -> {//门头照
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, SelectPicActivity::class.java)
                intent.putExtra("selNum", 1)
                intent.putExtra("selMode", PictureConfig.SINGLE)
                picLauncher.launch(intent)
                selectPicType = 4
            }

            R.id.tv_shop_pic -> {//店铺照片
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, SelectPicActivity::class.java)
                intent.putExtra("selNum", 1)
                intent.putExtra("selMode", PictureConfig.SINGLE)
                picLauncher.launch(intent)
                selectPicType = 5
            }

            R.id.tv_remit_account -> {//提现账号
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this, RemitAccountActivity::class.java))
            }

            R.id.tv_address -> {//地址
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PermissionUtils.permissionGroup(PermissionConstants.LOCATION)
                    .callback(object : PermissionUtils.FullCallback {
                        override fun onGranted(granted: MutableList<String>) {
                            LogUtils.d("tag", granted)
                            val intent = Intent(this@ShopInfoActivity, AMapActivity::class.java)
                            intent.putExtra("longitude", longitude)
                            intent.putExtra("latitude", latitude)
                            aMapLauncher.launch(intent)
                        }

                        override fun onDenied(
                            deniedForever: MutableList<String>,
                            denied: MutableList<String>
                        ) {
                            LogUtils.d("tag", deniedForever)
                            LogUtils.d("tag", denied)
                            if (deniedForever.size == 3 && denied.size == 3) {
                                ToastUtils.showShort("请手动开启定位权限，否则将无法正常获取定位信息！")
                            }
                        }
                    }).request()
            }

            R.id.tv_business_hours -> {//营业时间
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                showBusinessTimePop()
            }
        }
    }

    /**
     * 店铺类型
     */
    private fun showTypePop() {
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
                contentView.findViewById<TextView>(R.id.tv_title).text = "店铺类型"
                if (BarUtils.isNavBarVisible(window)) {
                    BarUtils.setNavBarLightMode(window, true)
                    val layoutParams = rv_sel_role.layoutParams as LinearLayout.LayoutParams
                    layoutParams.bottomMargin = BarUtils.getNavBarHeight()
                    rv_sel_role.layoutParams = layoutParams
                }
                rv_sel_role.layoutManager = LinearLayoutManager(this)
                val adapter = IdTypeAdapter(shopTypes, tv_shop_type.text.toString())
                adapter.bindToRecyclerView(rv_sel_role)
                adapter.setOnItemClickListener { _, _, position ->
                    tv_shop_type.text = shopTypes[position].name
                    shopType = shopTypes[position].id
                    pop.dismiss()
                }
                contentView.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                    pop.dismiss()
                }
            }
            .create()
        popWindow.isClippingEnabled = false
        popWindow.showAtLocation(ll_content, Gravity.BOTTOM, 0, 0)
    }

    /**
     * 营业时间选择器
     */
    private fun showBusinessTimePop() {
        val popWindow = PopWindow.Builder(this)
            .setView(R.layout.layout_pop_time_picker)
            .setWidthAndHeight(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            .setOutsideTouchable(true)
            .setAnimStyle(R.style.BottomAnimation)
            .setBackGroundLevel(0.5f)
            .setChildrenView { contentView, pop ->
                val begin = contentView.findViewById<TimePicker>(R.id.timepicker)
                begin.setIs24HourView(true)
                begin.descendantFocusability = TimePicker.FOCUS_BLOCK_DESCENDANTS  //设置点击事件不弹键盘
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    begin.hour =
                        if (businessBegin.isEmpty()) 8 else businessBegin.split(Regex(":"))[0].toInt()
                    begin.minute =
                        if (businessBegin.isEmpty()) 0 else businessBegin.split(Regex(":"))[1].toInt()
                }
                val end = contentView.findViewById<TimePicker>(R.id.timepicker2)
                end.setIs24HourView(true)
                end.descendantFocusability = TimePicker.FOCUS_BLOCK_DESCENDANTS
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    end.hour =
                        if (businessEnd.isEmpty()) 18 else businessEnd.split(Regex(":"))[0].toInt()
                    end.minute =
                        if (businessEnd.isEmpty()) 0 else businessEnd.split(Regex(":"))[1].toInt()
                }
                contentView.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
                    pop.dismiss()
                }
                contentView.findViewById<TextView>(R.id.tv_sure_manual).setOnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        businessBegin =
                            "${checkTime(begin.hour)}:${checkTime(begin.minute)}"
                        businessEnd =
                            "${checkTime(end.hour)}:${checkTime(end.minute)}"
                        tv_business_hours.text = "$businessBegin~$businessEnd"
                    }
                    pop.dismiss()
                }
            }
            .create()
        popWindow.isClippingEnabled = false
        popWindow.showAtLocation(ll_content, Gravity.BOTTOM, 0, 0)
    }

    private fun checkTime(time: Int): String {
        return if (time < 10)
            "0$time"
        else
            "$time"
    }

    private val picLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                RESULT_OK -> {
                    val bundle = it.data!!.extras
                    ossPost(bundle!!.getString("name")!!, bundle.getString("path")!!)
                }
            }
        }
    private val aMapLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                RESULT_OK -> {
                    val bundle = it.data!!.extras
                    longitude = bundle!!.getString("longitude")!!
                    latitude = bundle.getString("latitude")!!
                    province = bundle.getString("province")!!
                    city = bundle.getString("city")!!
                    county = bundle.getString("county")!!
                    tv_address.text = "${province}/${city}/${county}"
                    et_address.setText(bundle.getString("address"))
                }
            }
        }

    /**
     * 上传图片
     */
    private fun ossPost(imgName: String, imgLocalPath: String) {
        val dialog = Dialog(this, R.style.Dialog)
        val contentView = View.inflate(this, R.layout.layout_progress_dialog, null)
        dialog.setContentView(contentView)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val progressBar = contentView.findViewById<ProgressBar>(R.id.pb_progress)
        dialog.show()
        val objectKey = "waej3/" + SPUtils.getInstance()
            .getInt("shopId") + "/" + System.currentTimeMillis() + imgName
        val imgUrl = UrlConstants.BaseOssUrl + objectKey
        when (selectPicType) {
            5 -> {
                Glide.with(this@ShopInfoActivity).load(imgLocalPath).into(iv_shop_pic)
                shopImage = imgUrl
                tv_shop_pic.visibility = View.GONE
            }
        }
        OssUtils.get().asyncPutImage(objectKey, imgLocalPath, this,
            object : OssUtils.OnOssCallBackListener {
                override fun onSuccess(float: Float) {
                    dialog.dismiss()
                    ToastUtils.showShort("上传成功，用时：${float}秒")
                    if (selectPicType == 5)
                        return
                    Thread {
                        kotlin.run {
                            LoadingUtils.showLoading(this@ShopInfoActivity, "加载中...")
                            val params = HashMap<String, Any>()
                            params["file"] = imgUrl
                            params["type"] = when (selectPicType) {
                                0 -> "FR_ID_CARD_FRONT"
                                1 -> "FR_ID_CARD_BEHIND"
                                2 -> "BUSINESS_LICENCE"
                                4 -> "MERCHANT_PHOTO"
                                else -> ""
                            }
                            RetrofitUtils.get().postJson(PostFileUrl, params, this@ShopInfoActivity,
                                object : RetrofitUtils.OnCallBackListener {
                                    override fun onSuccess(s: String) {
                                        LogUtils.d("tag", s)
                                        val t = GsonUtils.fromJson(s, PostFileResponse::class.java)
                                        when (selectPicType) {
                                            0 -> {
                                                lklIdCardFront = t.data!!.url!!
                                                Glide.with(this@ShopInfoActivity).load(imgLocalPath)
                                                    .into(iv_sfz_zm)
                                                idCardFront = imgUrl
                                                tv_sfz_zm.visibility = View.GONE
                                            }

                                            1 -> {
                                                lklIdCardBack = t.data!!.url!!
                                                Glide.with(this@ShopInfoActivity).load(imgLocalPath)
                                                    .into(iv_sfz_bm)
                                                idCardBack = imgUrl
                                                tv_sfz_bm.visibility = View.GONE
                                            }

                                            2 -> {
                                                lklBusinessLicense = t.data!!.url!!
                                                Glide.with(this@ShopInfoActivity).load(imgLocalPath)
                                                    .into(iv_license)
                                                businessLicense = imgUrl
                                                tv_license.visibility = View.GONE
                                            }

                                            4 -> {
                                                lklDoorPhoto = t.data!!.url!!
                                                Glide.with(this@ShopInfoActivity).load(imgLocalPath)
                                                    .into(iv_door_head)
                                                doorPhoto = imgUrl
                                                tv_door_head.visibility = View.GONE
                                            }
                                        }
                                    }

                                    override fun onFailed(e: String) {
                                        PopupWindowUtils.get()
                                            .showConfirmPop(
                                                this@ShopInfoActivity,
                                                "上传失败，请重新上传！"
                                            ) {
                                                when (selectPicType) {
                                                    0 -> onClick(tv_sfz_zm)
                                                    1 -> onClick(tv_sfz_bm)
                                                    2 -> onClick(tv_license)
                                                    4 -> onClick(tv_door_head)
                                                }
                                            }
                                    }
                                })
                        }
                    }.start()
                }

                override fun onFailed(e: String) {
                    dialog.dismiss()
                    ToastUtils.showShort(e)
                }

                override fun onProgress(progress: Int) {
                    progressBar.progress = progress
                }
            })
    }

    private fun commit() {
        when {
            TextUtils.isEmpty(tv_shop_type.text.toString()) -> ToastUtils.showShort("店铺类型不能为空")
            TextUtils.isEmpty(et_fullname.text.toString()) -> ToastUtils.showShort("店铺名称不能为空")
            TextUtils.isEmpty(et_businessName.text.toString()) -> ToastUtils.showShort("商户注册名称不能为空")
            TextUtils.isEmpty(et_contactName.text.toString()) -> ToastUtils.showShort("负责人不能为空")
            TextUtils.isEmpty(idCardFront) -> ToastUtils.showShort("身份证正面不能为空")
            TextUtils.isEmpty(idCardBack) -> ToastUtils.showShort("身份证背面不能为空")
//            TextUtils.isEmpty(bankCard) -> ToastUtils.showShort("请上传银行卡照片")
            TextUtils.isEmpty(et_contactPhone.text.toString()) -> ToastUtils.showShort("负责人电话不能为空")
            TextUtils.isEmpty(et_address.text.toString()) -> ToastUtils.showShort("店铺地址不能为空")
            TextUtils.isEmpty(tv_business_hours.text.toString()) -> ToastUtils.showShort("营业时间不能为空")
            TextUtils.isEmpty(businessLicense) -> ToastUtils.showShort("营业执照不能为空")
            TextUtils.isEmpty(doorPhoto) -> ToastUtils.showShort("请上传店铺门头照")
            TextUtils.isEmpty(shopImage) -> ToastUtils.showShort("请上传店铺照片")
            TextUtils.isEmpty(et_shopPhone.text.toString()) -> ToastUtils.showShort("店铺热线不能为空")
            else -> {
                val params = HashMap<String, Any>()
                params["id"] = intent.getStringExtra("id")
                params["fullname"] = et_fullname.text.toString()
                params["businessName"] = et_businessName.text.toString()
                params["shopType"] = shopType
                params["contactName"] = et_contactName.text.toString()
                params["idCardFront"] = idCardFront
                params["idCardBack"] = idCardBack
                params["contactPhone"] = et_contactPhone.text.toString()
                params["province"] = province
                params["city"] = city
                params["county"] = county
                params["address"] = et_address.text.toString()
                params["businessTime"] = tv_business_hours.text.toString()
                params["businessBegin"] = businessBegin
                params["businessEnd"] = businessEnd
                params["businessLicense"] = businessLicense
//                params["withdrawalAccount"] = tv_remit_account.text.toString()
                params["doorPhoto"] = doorPhoto
                val imageList = ArrayList<HashMap<String, String>>()
                val hashMap = HashMap<String, String>()
                hashMap["imageUrl"] = shopImage
                imageList.add(hashMap)
                params["kmShopImageVoList"] = imageList
                params["shopPhone"] = et_shopPhone.text.toString()
                params["lklDoorPhoto"] = lklDoorPhoto
                params["lklBusinessLicense"] = lklBusinessLicense
                params["lklIdCardBack"] = lklIdCardBack
                params["lklIdCardFront"] = lklIdCardFront
                LoadingUtils.showLoading(this, "加载中...")
                RetrofitUtils.get()
                    .putJson(ShopUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                        override fun onSuccess(s: String) {
                            LogUtils.d("tag", s)
                            val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                            if (t.code == 200) {
                                val intent =
                                    Intent(this@ShopInfoActivity, RemitAccountActivity::class.java)
                                if (auditStatus == 0) {//状态是0的时候默认将四个字段传过去
                                    intent.putExtra("fullname", et_fullname.text.toString())
                                    intent.putExtra("businessName", et_businessName.text.toString())
                                    intent.putExtra("contactName", et_contactName.text.toString())
                                    intent.putExtra("contactPhone", et_contactPhone.text.toString())
                                } else {
                                    intent.putExtra("fullname", "")
                                    intent.putExtra("businessName", "")
                                    intent.putExtra("contactName", "")
                                    intent.putExtra("contactPhone", "")
                                }
                                startActivity(intent)
                            }
                            ToastUtils.showShort(t.msg)
                        }

                        override fun onFailed(e: String) {
                            ToastUtils.showShort(e)
                        }
                    })
            }
        }
    }

    private fun getInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getShopInfo(intent.getStringExtra("id").toInt())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, ShopInfoResponse::class.java)
                    when (t.code) {
                        200 -> {
                            auditStatus =
                                if (t.data.auditStatus == null) 0 else t.data.auditStatus!!
                            shopType = t.data.shopType
                            shopTypes.forEach {
                                if (it.id == shopType)
                                    tv_shop_type.text = it.name
                            }
                            et_fullname.setText(t.data.fullname)
                            et_businessName.setText(t.data.businessName)
                            et_contactName.setText(t.data.contactName)
                            idCardFront = t.data.idCardFront
                            setTextViewVisibility(tv_sfz_zm, idCardFront)
                            Glide.with(this@ShopInfoActivity).load(t.data.idCardFront)
                                .into(iv_sfz_zm)
                            idCardBack = t.data.idCardBack
                            setTextViewVisibility(tv_sfz_bm, idCardBack)
                            Glide.with(this@ShopInfoActivity).load(t.data.idCardBack)
                                .into(iv_sfz_bm)
                            et_contactPhone.setText(t.data.contactPhone)
                            province = t.data.province
                            city = t.data.city
                            county = t.data.county
                            if (!TextUtils.isEmpty(t.data.province) && !TextUtils.isEmpty(t.data.city) && !TextUtils.isEmpty(
                                    t.data.county
                                )
                            )
                                tv_address.text =
                                    "${t.data.province}/${t.data.city}/${t.data.county}"
                            et_address.setText(t.data.address)
                            tv_business_hours.text = t.data.businessTime
                            businessBegin = t.data.businessBegin
                            businessEnd = t.data.businessEnd
                            businessLicense = t.data.businessLicense
                            setTextViewVisibility(tv_license, businessLicense)
                            Glide.with(this@ShopInfoActivity).load(t.data.businessLicense)
                                .into(iv_license)
//                            tv_remit_account.text = t.data.withdrawalAccount
//                            Glide.with(this@ShopInfoActivity).load(t.data.)
                            doorPhoto = t.data.doorPhoto
                            setTextViewVisibility(tv_door_head, doorPhoto)
                            Glide.with(this@ShopInfoActivity).load(t.data.doorPhoto)
                                .into(iv_door_head)
                            if (t.data.kmShopImageVoList != null && t.data.kmShopImageVoList!!.isNotEmpty())
                                shopImage = t.data.kmShopImageVoList!![0].imageUrl
                            setTextViewVisibility(tv_shop_pic, shopImage)
                            Glide.with(this@ShopInfoActivity).load(shopImage).into(iv_shop_pic)
                            et_shopPhone.setText(t.data.shopPhone)
//                            ctv_beidou.isChecked = false
                            longitude = t.data.longitude
                            latitude = t.data.latitude
                            lklIdCardFront = t.data.lklIdCardFront
                            lklIdCardBack = t.data.lklIdCardBack
                            lklBusinessLicense = t.data.lklBusinessLicense
                            lklDoorPhoto = t.data.lklDoorPhoto
                            when (auditStatus) {
                                3 -> {//审核中
                                    rl_review.visibility = View.VISIBLE
                                    nsl_shop.visibility = View.GONE
                                    btn_commit.text = "获取审核结果"
                                }

                                4 -> {
                                    rl_review.visibility = View.VISIBLE
                                    nsl_shop.visibility = View.GONE
                                    tv_review.text = "签约中\n请前往签约界面进行签约！"
                                    btn_commit.text = "去签约"
                                }
                            }
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@ShopInfoActivity)
                        }

                        else -> {
                            if (t.msg != null)
                                ToastUtils.showShort(t.msg)
                            else
                                ToastUtils.showShort("异常（代码：${t.code}）")
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    private fun setTextViewVisibility(textView: TextView, string: String) {
        textView.visibility =
            if (TextUtils.isEmpty(string)) View.VISIBLE else View.GONE
    }

    @Subscribe
    fun onMassageEvent(eb: EBLaKaLaBack) {
        if (eb.back)
            getInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}