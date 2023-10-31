package com.bossed.waej.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.OnlineMenuAdapter
import com.bossed.waej.adapter.PageMenuAdapter
import com.bossed.waej.adapter.ReservationAdapter
import com.bossed.waej.base.BaseFragment
import com.bossed.waej.customview.MyAlertDialog
import com.bossed.waej.eventbus.EBBackPage
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.KmAppointRecordVo
import com.bossed.waej.javebean.OnlineMenuBean
import com.bossed.waej.javebean.OnlineResponse
import com.bossed.waej.javebean.PageMenuBean
import com.bossed.waej.ui.*
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.hjq.bar.OnTitleBarListener
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.android.synthetic.main.fragment_online.*
import org.greenrobot.eventbus.EventBus

class OnlineFragment : BaseFragment(), OnClickListener {
    private lateinit var pageMenu1Adapter: PageMenuAdapter
    private val pageMenu1List = ArrayList<PageMenuBean>()
    private lateinit var onlineMenuAdapter: OnlineMenuAdapter
    private val menu2List = ArrayList<OnlineMenuBean>()
    private lateinit var reservationAdapter: ReservationAdapter
    private val reservationList = ArrayList<KmAppointRecordVo>()
    private var shopId = ""
    private var freecheckitems = ""
    private var fullname = ""
    private var waejStatus = 0//拉卡拉平台审核状态
    private var shopMiniQrcode = ""//小程序二维码
    private var pageTemplateId = -1//小程序展示模板id
    private var score = 0

    override fun lazyLoad() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_online
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        setTopMargin(tb_online)
        rv_online.layoutManager = GridLayoutManager(requireContext(), 4)
        pageMenu1List.clear()
        pageMenu1List.add(PageMenuBean("扫码核销", R.mipmap.icon_sm_hx, "0", true))
        pageMenu1List.add(PageMenuBean("服务中订单", R.mipmap.icon_wfw, "0", true))
        pageMenu1List.add(PageMenuBean("我的订单", R.mipmap.icon_ls_gd, "0", true))
        pageMenu1List.add(PageMenuBean("我的评价", R.mipmap.icon_wd_pj, "0", true))
        pageMenu1Adapter = PageMenuAdapter(pageMenu1List, 2)
        pageMenu1Adapter.bindToRecyclerView(rv_online)

        rv_shop_data.layoutManager = GridLayoutManager(requireContext(), 2)
        menu2List.clear()
        menu2List.add(OnlineMenuBean(R.mipmap.icon_dp_xx, "店铺信息", "维护"))
        menu2List.add(OnlineMenuBean(R.mipmap.icon_mf_jc, "免费检测", "项目"))
        menu2List.add(OnlineMenuBean(R.mipmap.icon_zy_sp, "自营商品", "维护"))
        menu2List.add(OnlineMenuBean(R.mipmap.icon_zy_tc, "自营项目", "维护"))
        onlineMenuAdapter = OnlineMenuAdapter(menu2List)
        onlineMenuAdapter.bindToRecyclerView(rv_shop_data)

        rv_reservation.layoutManager = LinearLayoutManager(requireContext())
        reservationAdapter = ReservationAdapter(reservationList)
        reservationAdapter.bindToRecyclerView(rv_reservation)
        reservationAdapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        rl_todo_after.setOnClickListener(this)
        tv_platform.setOnClickListener(this)
        tb_online.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                val intent = Intent(requireContext(), AppletQRCodeActivity::class.java)
                intent.putExtra("shopMiniQrcode", shopMiniQrcode)
                intent.putExtra("fullname", fullname)
                intent.putExtra("shopId", shopId)
                intent.putExtra("pageTemplateId", pageTemplateId)
                startActivity(intent)
            }
        })
        pageMenu1Adapter.setOnItemClickListener { adapter, view, position ->
            when (pageMenu1List[position].menu) {
                "扫码核销" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    //配置扫描时的基本参数
                    val options = ScanOptions()
                    options.apply {
                        setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)//图形码的格式：商品码、一维码、二维码、数据矩阵、全部类型
                        setPrompt("请将二维码置于取景框内扫描")
                        setCameraId(0) //0 后置摄像头  1 前置摄像头
                        setBeepEnabled(true)
                        setOrientationLocked(false)
                        captureActivity = ContinuousCaptureActivity::class.java
//                    setTimeout(5000)//设置超时时间
                        setBarcodeImageEnabled(false)//是否保存图片，扫描成功会截取扫描框的图形保存到手机并在result中返回路径->result.barcodeImagePath
                    }
                    //启动扫描二维码界面
                    barcodeLauncher.launch(options)
                }

                "服务中订单" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    startActivity(Intent(requireContext(), UnservicedListActivity::class.java))
                }

                "我的订单" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    startActivity(Intent(requireContext(), MyOrderActivity::class.java))
                }

                "我的评价" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    val intent = Intent(requireContext(), MyEvaluationActivity::class.java)
                    intent.putExtra("score", score.toFloat())
                    startActivity(intent)
                }
            }
        }
        onlineMenuAdapter.setOnItemClickListener { adapter, view, position ->
            when (menu2List[position].name) {
                "店铺信息" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    val intent = Intent(requireContext(), ShopInfoActivity::class.java)
                    intent.putExtra("id", shopId)
                    startActivity(intent)
                }

                "免费检测" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    val intent = Intent(requireContext(), FreeTestingActivity::class.java)
                    intent.putExtra("id", shopId)
                    intent.putExtra("freecheckitems", freecheckitems)
                    intent.putExtra("fullname", fullname)
                    startActivity(intent)
                }

                "自营商品" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    val intent = Intent(requireContext(), SelfSupportActivity::class.java)
                    intent.putExtra("shopId", shopId)
                    startActivity(intent)
                }

                "自营项目" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    startActivity(Intent(requireContext(), SelfPackageActivity::class.java))
                }
            }
        }
        tv_more.setOnClickListener(this)
        reservationAdapter.setOnItemClickListener { adapter, view, position ->
            if (DoubleClicksUtils.get().isFastDoubleClick)
                return@setOnItemClickListener
            val intent = Intent(requireContext(), ReservationInfoActivity::class.java)
            intent.putExtra("id", reservationList[position].id)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_more -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(requireContext(), ReservationMoreActivity::class.java))
            }

            R.id.rl_todo_after -> {//售后订单
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(requireContext(), TodoAfterActivity::class.java))
            }

            R.id.tv_platform -> {//平台
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(requireContext(), PlatformActivity::class.java)
                intent.putExtra("waejStatus", waejStatus)
                startActivity(intent)
            }
        }
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        //获取回调结果
        if (result.contents != null) {
//            "扫描结果: " + result.contents + ",${result.barcodeImagePath}"
            val intent = Intent(requireContext(), ScanDetailActivity::class.java)
            intent.putExtra("scanResult", result.contents)
            intent.putExtra("type", 1)//0从列表跳转  1扫码跳转
            startActivity(intent)
//            if(result.barcodeImagePath.isNotEmpty()){
//                ivImage.setImageURI(Uri.parse(result.barcodeImagePath))
//            }
        }
    }

    private fun getInfo() {
        LoadingUtils.showLoading(requireActivity(), "加载中...")
        RetrofitUtils.get().getJson(
            UrlConstants.ShopPageUrl, HashMap(), requireActivity(),
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, OnlineResponse::class.java)
                    if (t.data.kmShop.freecheckitems != null)
                        freecheckitems = t.data.kmShop.freecheckitems!!
                    pageMenu1List[1].subscript = t.data.shopCount.pendOrderNum!!
                    pageMenu1Adapter.setNewData(pageMenu1List)
                    fullname = t.data.kmShop.fullname
                    shopId = t.data.kmShop.id
                    score = t.data.kmShop.score
                    pageTemplateId = t.data.kmShop.pageTemplateId
                    shopMiniQrcode =
                        if (TextUtils.isEmpty(t.data.kmShop.shopMiniQrcode)) "" else t.data.kmShop.shopMiniQrcode!!
                    waejStatus = t.data.kmShop.waejStatus
                    tv_today_money.text = t.data.shopCount.turnoverTotle
                    tv_accumulate_money.text = t.data.shopCount.turnoverDay
                    reservationList.clear()
                    reservationList.addAll(t.data.kmAppointRecordVo)
                    reservationAdapter.setNewData(reservationList)
                    if (TextUtils.isEmpty(t.data.kmShop.merchantNo)) {
                        val dialog = MyAlertDialog(
                            requireContext(),
                            "根据相关规定，开展线上业务功能需完善相关信息。请点击下方“去完善”按钮完善信息"
                        )
                        dialog.setCanceledOnTouchOutside(false)
                        dialog.setCancelable(false)
                        dialog.setConfirm("去完善")
                        dialog.setCancel("返回首页")
                        dialog.setConfirmTextColor("#3477FC")
                        dialog.setOnDialogListener {
                            onConfirm {
                                val intent = Intent(requireContext(), ShopInfoActivity::class.java)
                                intent.putExtra("id", shopId)
                                startActivity(intent)
                                dialog.dismiss()
                            }
                            onCancel {
                                dialog.dismiss()
                                EventBus.getDefault().post(EBBackPage(true))
                            }
                        }
                        dialog.show()
                    } else {
                        if (TextUtils.isEmpty(SPUtils.getInstance().getString("review")))
                            PopupWindowUtils.get()
                                .showOnlyConfirmPop(
                                    requireActivity(),
                                    "系统提示",
                                    "店铺审核已通过。"
                                ) {
                                    SPUtils.getInstance().put("review", "true")
                                }
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        BarUtils.setStatusBarLightMode(requireActivity(), true)
        getInfo()
    }


}