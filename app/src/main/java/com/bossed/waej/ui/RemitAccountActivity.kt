package com.bossed.waej.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.IdTypeAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.customview.LoadingDialog
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.eventbus.EBLaKaLaBack
import com.bossed.waej.eventbus.EBSign
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.http.UrlConstants.GetBankCardInfoUrl
import com.bossed.waej.http.UrlConstants.MerchantListUrl
import com.bossed.waej.http.UrlConstants.NewMerchantUrl
import com.bossed.waej.javebean.BankCardInfoResponse
import com.bossed.waej.javebean.Children
import com.bossed.waej.javebean.ChildrenX
import com.bossed.waej.javebean.CitiesBean
import com.bossed.waej.javebean.City
import com.bossed.waej.javebean.ContractResponse
import com.bossed.waej.javebean.IdTypeBean
import com.bossed.waej.javebean.PostFileResponse
import com.bossed.waej.javebean.RemitAccountListResponse
import com.bossed.waej.javebean.RemitAccountResponse
import com.bossed.waej.util.DateFormatUtils
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.GetJsonDataUtil
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
import kotlinx.android.synthetic.main.activity_remit_account.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class RemitAccountActivity : BaseActivity(), View.OnClickListener, View.OnLongClickListener {
    private var options1Items: ArrayList<City> = ArrayList() //省
    private val options2Items = ArrayList<ArrayList<Children>>() //市
    private val options3Items = ArrayList<ArrayList<ArrayList<ChildrenX>>>() //区
    private var larIdType = "01"//证件类型
    private var merRegDistCode = ""//商户地区代码
    private var options1 = -1
    private var options2 = -1
    private var options3 = -1
    private var id = ""//商户id
    private var auditStatus = -1//0-草稿 1-审核通过 2-审核驳回  3-审核中 4-合同审核中 5-签约中
    private var signUrl = ""
    private var selectPicType = 0
    private var idCardFront = ""//身份证正面
    private var lklIdCardFront = ""//身份证正面
    private var idCardBack = ""//身份证背面
    private var lklIdCardBack = ""//身份证背面
    private var bankCard = ""//银行卡
    private var lklBankCard = ""//银行卡
    private lateinit var loadingDialog: LoadingDialog

    override fun getLayoutId(): Int {
        return R.layout.activity_remit_account
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_remit_account)
        loadingDialog = LoadingDialog(this, "加载中...")
        loadingDialog.setCanceledOnTouchOutside(false)
        val t =
            GsonUtils.fromJson(GetJsonDataUtil.getJson(this, "cities.json"), CitiesBean::class.java)
        //添加一级数据
        options1Items.addAll(t.cities)
        options1Items.forEach {
            val city = ArrayList<Children>()//二级
            city.addAll(it.children)
            //添加二级数据
            options2Items.add(city)
            val childrenXList = ArrayList<ArrayList<ChildrenX>>()//三级
            it.children.forEach { itt ->
                val areaList = ArrayList<ChildrenX>()
                if (itt.children == null || itt.children!!.isEmpty())
                    areaList.add(ChildrenX("", ""))//如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                else
                    areaList.addAll(itt.children!!)
                childrenXList.add(areaList)
            }
            //添加三级数据
            options3Items.add(childrenXList)
        }
        getList()
    }

    override fun initListener() {
        tb_remit_account.setOnTitleBarListener(object : OnTitleBarListener {
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
        iv_card.setOnLongClickListener(this)
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

            R.id.iv_card -> {
                PopupWindowUtils.get().showConfirmPop(this, "是否更换当前图片？") {
                    onClick(tv_card)
                }
            }
        }
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ctv_personal -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_personal.isChecked = true
                ctv_enterprise.isChecked = false
                ll_public.visibility = View.GONE
                rl_legal.visibility = View.VISIBLE
            }

            R.id.ctv_enterprise -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_personal.isChecked = false
                ctv_enterprise.isChecked = true
                ll_public.visibility = View.VISIBLE
                rl_legal.visibility = View.GONE
            }

            R.id.ctv_yes -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_yes.isChecked = true
                ctv_no.isChecked = false
                ll_card.visibility = View.GONE
            }

            R.id.ctv_no -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_no.isChecked = true
                ctv_yes.isChecked = false
                ll_card.visibility = View.VISIBLE
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

            R.id.tv_card -> {//银行卡
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, SelectPicActivity::class.java)
                intent.putExtra("selNum", 1)
                intent.putExtra("selMode", PictureConfig.SINGLE)
                picLauncher.launch(intent)
                selectPicType = 3
            }

            R.id.iv_card -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showPhotoPop(bankCard, this, ll_content)
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

            R.id.iv_search -> {//查询银行卡信息
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (!TextUtils.isEmpty(et_acctNo.text.toString()))
                    getBankCardInfo()
            }

            R.id.tv_id_type -> {//证件类型
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                showIdTypePop()
            }

            R.id.tv_id_start -> {//身份证开始日期
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_id_start.text = DateFormatUtils.get().formatDate(it)
                }
            }

            R.id.tv_id_end -> {//身份证结束日期
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_id_end.text = DateFormatUtils.get().formatDate(it)
                }
            }

            R.id.tv_license_start -> {//营业执照开始日期
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_license_start.text = DateFormatUtils.get().formatDate(it)
                }
            }

            R.id.tv_license_end -> {//营业执照结束日期
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showSelDateTimePop(this, 2) {
                    tv_license_end.text = DateFormatUtils.get().formatDate(it)
                }
            }

            R.id.tv_city -> {//地区
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val pvOptions: OptionsPickerView<*> = OptionsPickerBuilder(
                    this
                ) { options1, options2, options3, v -> //返回的分别是三个级别的选中位置
                    tv_city.text =
                        "${options1Items[options1].pickerViewText}/${options2Items[options1][options2].pickerViewText}/${options3Items[options1][options2][options3].pickerViewText}"
                    merRegDistCode =
                        if (TextUtils.isEmpty(options3Items[options1][options2][options3].value)) options2Items[options1][options2].value!! else
                            options3Items[options1][options2][options3].value!!
                    this.options1 = options1
                    this.options2 = options2
                    this.options3 = options3
                }
                    .setTitleText("城市选择")
                    .setDividerColor(Color.BLACK)
                    .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                    .setContentTextSize(14)
                    .setTitleSize(15)
                    .setTitleBgColor(Color.parseColor("#ffffff"))
                    .setSubCalSize(13)
                    .setCancelColor(Color.parseColor("#3477fc"))
                    .setSubmitColor(Color.parseColor("#3477fc"))
                    .build<Any>()
                pvOptions.setPicker(
                    options1Items as List<Nothing>?,
                    options2Items as List<Nothing>?,
                    options3Items as List<Nothing>?
                )
                if (options1 != -1 && options2 != -1 && options3 != -1)
                    pvOptions.setSelectOptions(options1, options2, options3)
                pvOptions.show()
            }

            R.id.btn_commit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when (auditStatus) {
                    4 -> {
                        if (TextUtils.isEmpty(signUrl)) {
                            if (isComplete())
                                if (TextUtils.isEmpty(id))
                                    new()
                                else
                                    update()
                            else
                                ToastUtils.showShort("必填项不能为空")
                        } else {
                            val intent = Intent(this, SignActivity::class.java)
                            intent.putExtra("signUrl", signUrl)
                            startActivity(intent)
                        }
                    }

                    else -> {
                        if (isComplete())
                            if (TextUtils.isEmpty(id))
                                new()
                            else
                                update()
                        else
                            ToastUtils.showShort("必填项不能为空")
                    }
                }
            }
        }
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
//        when (selectPicType) {
//            0 -> {
//
//            }
//
//            1 -> {
//
//            }
//
//            3 -> {
//
//            }
//            4 -> {
//                Glide.with(this@ShopInfoActivity).load(imgLocalPath).into(iv_door_head)
//                doorPhoto = imgUrl
//                tv_door_head.visibility = View.GONE
//            }
//
//            5 -> {
//                Glide.with(this@ShopInfoActivity).load(imgLocalPath).into(iv_shop_pic)
//                shopImage = imgUrl
//                tv_shop_pic.visibility = View.GONE
//            }
//        }
        OssUtils.get().asyncPutImage(objectKey, imgLocalPath, this,
            object : OssUtils.OnOssCallBackListener {
                override fun onSuccess(float: Float) {
                    dialog.dismiss()
                    Thread {
                        kotlin.run {
                            Looper.prepare()
                            LoadingUtils.showLoading(this@RemitAccountActivity, "加载中...")
                            val params = HashMap<String, Any>()
                            params["file"] = imgUrl
                            params["type"] = when (selectPicType) {
                                0 -> "ID_CARD_FRONT"
                                1 -> "ID_CARD_BEHIND"
                                3 -> "BANK_CARD"
                                else -> ""
                            }
                            RetrofitUtils.get().postJson(
                                UrlConstants.PostFileUrl, params, this@RemitAccountActivity,
                                object : RetrofitUtils.OnCallBackListener {
                                    override fun onSuccess(s: String) {
                                        LogUtils.d("tag", s)
                                        val t = GsonUtils.fromJson(s, PostFileResponse::class.java)
                                        when (selectPicType) {
                                            0 -> {
                                                Glide.with(this@RemitAccountActivity)
                                                    .load(imgLocalPath).into(iv_sfz_zm)
                                                idCardFront = imgUrl
                                                tv_sfz_zm.visibility = View.GONE
                                                lklIdCardFront = t.data!!.url!!
                                            }

                                            1 -> {
                                                lklIdCardBack = t.data!!.url!!
                                                Glide.with(this@RemitAccountActivity)
                                                    .load(imgLocalPath).into(iv_sfz_bm)
                                                idCardBack = imgUrl
                                                tv_sfz_bm.visibility = View.GONE
                                            }

                                            3 -> {
                                                lklBankCard = t.data!!.url!!
                                                Glide.with(this@RemitAccountActivity)
                                                    .load(imgLocalPath).into(iv_card)
                                                bankCard = imgUrl
                                                tv_card.visibility = View.GONE
                                            }
                                        }
                                    }

                                    override fun onFailed(e: String) {
                                        PopupWindowUtils.get()
                                            .showConfirmPop(
                                                this@RemitAccountActivity,
                                                "上传失败，请重新上传！"
                                            ) {
                                                when (selectPicType) {
                                                    0 -> onClick(tv_sfz_zm)
                                                    1 -> onClick(tv_sfz_bm)
                                                    3 -> onClick(tv_card)
                                                }
                                            }
                                    }
                                })
                            Looper.loop()
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

    /**
     * 证件类型弹窗
     */
    private fun showIdTypePop() {
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
                contentView.findViewById<TextView>(R.id.tv_title).text = "证件类型"
                if (BarUtils.isNavBarVisible(window)) {
                    BarUtils.setNavBarLightMode(window, true)
                    val layoutParams = rv_sel_role.layoutParams as LinearLayout.LayoutParams
                    layoutParams.bottomMargin = BarUtils.getNavBarHeight()
                    rv_sel_role.layoutParams = layoutParams
                }
                rv_sel_role.layoutManager = LinearLayoutManager(this)
                val items = ArrayList<IdTypeBean>()
                items.add(IdTypeBean("身份证", "01"))
                items.add(IdTypeBean("护照", "02"))
                items.add(IdTypeBean("港澳通行证", "03"))
                items.add(IdTypeBean("台胞证", "04"))
                items.add(IdTypeBean("其它证件", "99"))
                val adapter = IdTypeAdapter(items, tv_id_type.text.toString())
                adapter.bindToRecyclerView(rv_sel_role)
                adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
                adapter.setOnItemClickListener { _, _, position ->
                    tv_id_type.text = items[position].name
                    larIdType = items[position].id
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
     * 新增商户
     */
    private fun new() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["merRegName"] = et_merRegName.text.toString()//注册名称
        params["merBizName"] = et_merBizName.text.toString()//经营名称
        params["merContactName"] = et_merContactName.text.toString()//联系人
        params["merContactMobile"] = et_merContactMobile.text.toString()//手机号
        params["merRegDistCode"] = merRegDistCode//地区代码
        val stringBuilder = StringBuilder()
        stringBuilder.append(options1)
        stringBuilder.append(",")
        stringBuilder.append(options2)
        stringBuilder.append(",")
        stringBuilder.append(options3)
        params["merRegDistCodeCache"] = stringBuilder.toString()//地区代码游标
        params["merRegAddr"] = et_merRegAddr.text.toString()//详细地址
        params["larIdType"] = larIdType//证件类型
        params["larName"] = et_larName.text.toString()//姓名
        params["larIdcard"] = et_larIdcard.text.toString()//证件号码
        params["larIdcardStDt"] = tv_id_start.text.toString()//证件开始日期
        params["larIdcardExpDt"] = tv_id_end.text.toString()//证件有效期
        params["bankCard"] = bankCard
        params["lklBankCard"] = lklBankCard
        if (ctv_personal.isChecked) {
            params["acctTypeCode"] = 58
            if (ctv_yes.isChecked)
                params["isSamePerson"] = 1
            if (ctv_no.isChecked) {
                params["isSamePerson"] = 0
                params["idCardFront"] = idCardFront
                params["idCardBack"] = idCardBack
                params["lklIdCardFront"] = lklIdCardFront
                params["lklIdCardBack"] = lklIdCardBack
            }
        }
        if (ctv_enterprise.isChecked) {
            params["acctTypeCode"] = 57
            params["merBlisName"] = et_merBlisName.text.toString()//营业执照名称
            params["merBlis"] = et_merBlis.text.toString()//营业执照号
            params["merBlisStDt"] = tv_license_start.text.toString()//营业执照开始日期
            params["merBlisExpDt"] = tv_license_end.text.toString()////营业执照有效期
        }
        params["acctName"] = et_acctName.text.toString()//银行卡名称
        params["openningBankCode"] = et_openningBankCode.text.toString()//开户行号
        params["openningBankName"] = et_openningBankName.text.toString()//开户行名称
        params["clearingBankCode"] = et_clearingBankCode.text.toString()//清算行号
        params["acctNo"] = et_acctNo.text.toString()//银行卡号
        params["remark"] = et_remark.text.toString()
        RetrofitUtils.get()
            .postJson(NewMerchantUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, RemitAccountResponse::class.java)
                    put(t.data!!.id!!.toInt())
//                    Thread {
//                        kotlin.run {
//
//                        }
//                    }.start()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    /**
     * 修改
     */
    private fun update() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["id"] = id
        params["merRegName"] = et_merRegName.text.toString()//注册名称
        params["merBizName"] = et_merBizName.text.toString()//经营名称
        params["merContactName"] = et_merContactName.text.toString()//联系人
        params["merContactMobile"] = et_merContactMobile.text.toString()//手机号
        params["merRegDistCode"] = merRegDistCode//地区代码
        val stringBuilder = StringBuilder()
        stringBuilder.append(options1)
        stringBuilder.append(",")
        stringBuilder.append(options2)
        stringBuilder.append(",")
        stringBuilder.append(options3)
        params["merRegDistCodeCache"] = stringBuilder.toString()//地区代码游标
        params["merRegAddr"] = et_merRegAddr.text.toString()//详细地址
        params["larIdType"] = larIdType//证件类型
        params["larName"] = et_larName.text.toString()//姓名
        params["larIdcard"] = et_larIdcard.text.toString()//证件号码
        params["larIdcardStDt"] = tv_id_start.text.toString()//证件开始日期
        params["larIdcardExpDt"] = tv_id_end.text.toString()//证件有效期
        params["bankCard"] = bankCard
        params["lklBankCard"] = lklBankCard
        if (ctv_personal.isChecked) {
            params["acctTypeCode"] = 58
            if (ctv_yes.isChecked)
                params["isSamePerson"] = 1
            if (ctv_no.isChecked) {
                params["isSamePerson"] = 0
                params["idCardFront"] = idCardFront
                params["idCardBack"] = idCardBack
                params["lklIdCardFront"] = lklIdCardFront
                params["lklIdCardBack"] = lklIdCardBack
            }
        }
        if (ctv_enterprise.isChecked) {
            params["acctTypeCode"] = 57
            params["merBlisName"] = et_merBlisName.text.toString()//营业执照名称
            params["merBlis"] = et_merBlis.text.toString()//营业执照号
            params["merBlisStDt"] = tv_license_start.text.toString()//营业执照开始日期
            params["merBlisExpDt"] = tv_license_end.text.toString()////营业执照有效期
        }
        params["acctName"] = et_acctName.text.toString()//银行卡名称
        params["openningBankCode"] = et_openningBankCode.text.toString()//开户行号
        params["openningBankName"] = et_openningBankName.text.toString()//开户行名称
        params["clearingBankCode"] = et_clearingBankCode.text.toString()//清算行号
        params["acctNo"] = et_acctNo.text.toString()//银行卡号
        params["remark"] = et_remark.text.toString()
        RetrofitUtils.get()
            .putJson(NewMerchantUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, RemitAccountResponse::class.java)
                    Handler().post {
                        put(id.toInt())
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    /**
     * 商户列表
     */
    private fun getList() {
        loadingDialog.show()
        RetrofitUtils.get()
            .getJson(MerchantListUrl, HashMap(), this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, RemitAccountListResponse::class.java)
                    if (t.rows != null && t.rows!!.isNotEmpty()) {
                        id = t.rows!![0].id!!
                        et_merRegName.setText(t.rows!![0].merRegName)
                        et_merBizName.setText(t.rows!![0].merBizName)
                        et_merContactName.setText(t.rows!![0].merContactName)
                        et_merContactMobile.setText(t.rows!![0].merContactMobile)
                        merRegDistCode = t.rows!![0].merRegDistCode!!
                        if (!TextUtils.isEmpty(t.rows!![0].merRegDistCodeCache)) {
                            val options = t.rows!![0].merRegDistCodeCache!!.split(",")
                            options1 = options[0].toInt()
                            options2 = options[1].toInt()
                            options3 = options[2].toInt()
                            tv_city.text =
                                "${options1Items[options1].pickerViewText}/${options2Items[options1][options2].pickerViewText}/${options3Items[options1][options2][options3].pickerViewText}"
                        }
                        et_merRegAddr.setText(t.rows!![0].merRegAddr)
                        larIdType = t.rows!![0].larIdType!!
                        tv_id_type.text = when (larIdType) {
                            "01" -> "身份证"
                            "02" -> "护照"
                            "03" -> "港澳通行证"
                            "04" -> "台胞证"
                            "99" -> "其它证件"
                            else -> "身份证"
                        }
                        et_larName.setText(t.rows!![0].larName)
                        et_larIdcard.setText(t.rows!![0].larIdcard)
                        tv_id_start.text = t.rows!![0].larIdcardStDt
                        tv_id_end.text = t.rows!![0].larIdcardExpDt
                        bankCard =
                            if (TextUtils.isEmpty(t.rows!![0].bankCard)) "" else t.rows!![0].bankCard!!
                        Glide.with(this@RemitAccountActivity).load(bankCard)
                            .into(iv_card)
                        tv_card.visibility =
                            if (TextUtils.isEmpty(bankCard)) View.VISIBLE else View.GONE
                        lklBankCard = t.rows!![0].lklBankCard!!
                        when (t.rows!![0].acctTypeCode) {
                            "58" -> {
                                ctv_personal.isChecked
                                when (t.rows!![0].isSamePerson) {
                                    0 -> {
                                        ctv_no.isChecked = true
                                        ctv_yes.isChecked = false
                                        ll_card.visibility = View.VISIBLE
                                        idCardFront = t.rows!![0].idCardFront!!
                                        Glide.with(this@RemitAccountActivity).load(idCardFront)
                                            .into(iv_sfz_zm)
                                        tv_sfz_zm.visibility =
                                            if (TextUtils.isEmpty(idCardFront)) View.VISIBLE else View.GONE
                                        idCardBack = t.rows!![0].idCardBack!!
                                        Glide.with(this@RemitAccountActivity).load(idCardBack)
                                            .into(iv_sfz_bm)
                                        tv_sfz_bm.visibility =
                                            if (TextUtils.isEmpty(idCardBack)) View.VISIBLE else View.GONE
                                        lklIdCardFront = t.rows!![0].lklIdCardFront!!
                                        lklIdCardBack = t.rows!![0].lklIdCardBack!!
                                    }

                                    1 -> {
                                        ctv_yes.isChecked = true
                                        ctv_no.isChecked = false
                                    }
                                }
                            }

                            "57" -> {
                                ctv_enterprise.isChecked
                                ll_public.visibility = View.VISIBLE
                                et_merBlisName.setText(t.rows!![0].merBlisName)
                                et_merBlis.setText(t.rows!![0].merBlis)
                                tv_license_start.text = t.rows!![0].merBlisStDt!!
                                tv_license_end.text = t.rows!![0].merBlisExpDt!!
                            }
                        }
                        et_acctName.setText(t.rows!![0].acctName)
                        et_acctNo.setText(t.rows!![0].acctNo)
                        et_openningBankCode.setText(t.rows!![0].openningBankCode)
                        et_openningBankName.setText(t.rows!![0].openningBankName)
                        et_clearingBankCode.setText(t.rows!![0].clearingBankCode)
                        et_remark.setText(t.rows!![0].remark)
                        auditStatus = t.rows!![0].auditStatus!!
                        getInfo()
                    } else {
                        loadingDialog.dismiss()
                    }
                    if (TextUtils.isEmpty(et_merRegName.text.toString()))
                        et_merRegName.setText(intent.getStringExtra("businessName"))
                    if (TextUtils.isEmpty(et_merBizName.text.toString()))
                        et_merBizName.setText(intent.getStringExtra("fullname"))
                    if (TextUtils.isEmpty(et_merContactName.text.toString()))
                        et_merContactName.setText(intent.getStringExtra("contactName"))
                    if (TextUtils.isEmpty(et_merContactMobile.text.toString()))
                        et_merContactMobile.setText(intent.getStringExtra("contactPhone"))
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                    loadingDialog.dismiss()
                }
            })
    }

    /**
     * 获取商户信息
     */
    private fun getInfo() {
        Api.getInstance().getApiService()
            .getMerchantInfo(id.toInt())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, RemitAccountResponse::class.java)
                    try {
                        when (t.code) {
                            200 -> {
                                auditStatus = t.data!!.auditStatus!!
                                if (!TextUtils.isEmpty(t.data!!.signUrl))
                                    signUrl = t.data!!.signUrl!!
                            }

//                        500 -> {
//                            rl_review.visibility = View.GONE
//                            nsv_remit.visibility = View.VISIBLE
//                            btn_commit.text = "保存"
//                            ToastUtils.showShort(t.msg)
//                        }

                            401 -> {
                                PopupWindowUtils.get()
                                    .showLoginOutTimePop(this@RemitAccountActivity)
                            }

                            else -> {
                                if (t.msg != null)
                                    ToastUtils.showShort(t.msg)
                                else
                                    ToastUtils.showShort("异常（代码：${t.code}）")
                            }
                        }
                    } finally {
                        when (auditStatus) {
                            1 -> {
                                tv_merCupNo.text =
                                    if (t.data!!.merCupNo == null) "" else t.data!!.merCupNo!!
                                rl_merCupNo.visibility = View.VISIBLE
                            }

                            2 -> {
                                PopupWindowUtils.get().showConfirmPop(
                                    this@RemitAccountActivity,
                                    t.data!!.failCause!!
                                ) {

                                }
                                rl_review.visibility = View.GONE
                                nsv_remit.visibility = View.VISIBLE
                                btn_commit.text = "保存"
                            }

                            3 -> {//3-审核中
                                rl_review.visibility = View.VISIBLE
                                nsv_remit.visibility = View.GONE
                                tv_review.text = "店铺信息已提交审核\n审核时间一般在1~3个工作日"
                                btn_commit.visibility = View.GONE
                            }

//                                4 -> {//4-合同审核中
//                                    rl_review.visibility = View.VISIBLE
//                                    nsv_remit.visibility = View.GONE
//                                    tv_review.text = "签约中\n请前往签约界面进行签约！"
//                                    btn_commit.text = "去签约"
//                                }

                            else -> {
                                rl_review.visibility = View.GONE
                                nsv_remit.visibility = View.VISIBLE
                                btn_commit.text = "保存"
                            }
                        }
                        loadingDialog.dismiss()
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    loadingDialog.dismiss()
                }
            })
    }

    /**
     * 提交合同
     */
    private fun put(id: Int) {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .putContract(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val response = GsonUtils.fromJson(s, ContractResponse::class.java)
                    when (response.code) {
                        200 -> {
                            if (TextUtils.isEmpty(response.data!!.signUrl))
                                ToastUtils.showShort(response.msg)
                            else {
                                val intent =
                                    Intent(this@RemitAccountActivity, SignActivity::class.java)
                                intent.putExtra("signUrl", response.data!!.signUrl)
                                startActivity(intent)
                            }
                        }

                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@RemitAccountActivity)
                        }

                        else -> {
                            if (response.msg != null)
                                ToastUtils.showShort(response.msg)
                            else
                                ToastUtils.showShort("异常（代码：${response.code}）")
                        }
                    }
                    LoadingUtils.dismissLoading()
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message!!)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    /**
     * 根据银行卡号查询卡信息
     */
    private fun getBankCardInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["cardNo"] = et_acctNo.text.toString()
        RetrofitUtils.get()
            .getJson(GetBankCardInfoUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BankCardInfoResponse::class.java)
                    et_acctName.setText(t.data!!.cardName)
                    et_openningBankCode.setText(t.data!!.bankCode)
                    et_openningBankName.setText(t.data!!.bankName)
                    et_clearingBankCode.setText(t.data!!.clearingBankCode)
                    ToastUtils.showShort(t.msg)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun isComplete(): Boolean {
        return when {
            TextUtils.isEmpty(et_merRegName.text.toString()) -> false
            TextUtils.isEmpty(et_merBizName.text.toString()) -> false
            TextUtils.isEmpty(et_merContactName.text.toString()) -> false
            TextUtils.isEmpty(et_merContactMobile.text.toString()) -> false
            TextUtils.isEmpty(merRegDistCode) -> false
            TextUtils.isEmpty(et_merRegAddr.text.toString()) -> false
            TextUtils.isEmpty(et_larName.text.toString()) -> false
            TextUtils.isEmpty(et_larIdcard.text.toString()) -> false
            TextUtils.isEmpty(tv_id_start.text.toString()) -> false
            TextUtils.isEmpty(tv_id_end.text.toString()) -> false
            TextUtils.isEmpty(et_acctName.text.toString()) -> false
            TextUtils.isEmpty(et_openningBankCode.text.toString()) -> false
            TextUtils.isEmpty(et_openningBankName.text.toString()) -> false
            TextUtils.isEmpty(et_clearingBankCode.text.toString()) -> false
            TextUtils.isEmpty(et_acctNo.text.toString()) -> false
            TextUtils.isEmpty(bankCard) -> false
            ctv_personal.isChecked && ctv_no.isChecked -> when {
                TextUtils.isEmpty(idCardFront) -> false
                TextUtils.isEmpty(idCardBack) -> false
                else -> true
            }

            ctv_enterprise.isChecked -> when {
                TextUtils.isEmpty(et_merBlisName.text.toString()) -> false
                TextUtils.isEmpty(et_merBlis.text.toString()) -> false
                TextUtils.isEmpty(tv_license_start.text.toString()) -> false
                TextUtils.isEmpty(tv_license_end.text.toString()) -> false
                else -> true
            }

            else -> true
        }
    }

    @Subscribe
    fun onMessageEvent(eb: EBSign) {
        if (eb.isBack)
            getList()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        EventBus.getDefault().post(EBLaKaLaBack(true))
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

//    override fun onResume() {
//        super.onResume()
//        getList()
//    }

}