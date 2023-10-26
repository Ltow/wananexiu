package com.bossed.waej.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.adapter.MyProductAdapter
import com.bossed.waej.adapter.PageMenuAdapter
import com.bossed.waej.base.BaseFragment
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.*
import com.bossed.waej.ui.*
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_page.*
import java.util.HashMap

class PageFragment : BaseFragment() {
    private lateinit var pageMenu1Adapter: PageMenuAdapter
    private val pageMenu1List = ArrayList<PageMenuBean>()
    private lateinit var pageMenu2Adapter: PageMenuAdapter
    private val pageMenu2List = ArrayList<PageMenuBean>()
    private lateinit var productAdapter: MyProductAdapter
    private val productData = ArrayList<MyProductData>()
    private lateinit var purchaseAdapter: PageMenuAdapter
    private val purchaseList = ArrayList<PageMenuBean>()
    private lateinit var inventoryAdapter: PageMenuAdapter
    private val inventoryList = ArrayList<PageMenuBean>()

    override fun lazyLoad() {
        RetrofitUtils.get().getJson(
            UrlConstants.TermRemindUrl, HashMap(),
            requireActivity(), object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, TermRemindResponse::class.java)
                    if (t.code == 200) {
                        productData.addAll(t.data)
                        showExpirePop()
                    }
                }

                override fun onFailed(e: String) {
                    if (e != "暂无到期产品！")
                        ToastUtils.showShort(e)
                }
            })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_page
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        setTopMargin(tb_page)
        val layoutParams: RelativeLayout.LayoutParams =
            v_page.layoutParams as RelativeLayout.LayoutParams
        layoutParams.height = layoutParams.height + BarUtils.getStatusBarHeight()
        v_page.layoutParams = layoutParams
        rv_page_order.layoutManager = GridLayoutManager(requireContext(), 4)
        pageMenu1List.clear()
        pageMenu1List.add(PageMenuBean("接车开单", R.mipmap.icon691, "0", true))
        pageMenu1List.add(PageMenuBean("服务中工单", R.mipmap.icon70, "0", true))
        pageMenu1List.add(PageMenuBean("历史工单", R.mipmap.icon_history, "0", true))
        pageMenu1List.add(PageMenuBean("客户档案", R.mipmap.icon_hyxf, "0", true))
//        pageMenu1List.add(PageMenuBean("会员列表", R.mipmap.icon_hyxf, "0", true))
//        pageMenuBean1.add(PageMenuBean1("临时业务", R.mipmap.icon_lsyw))
        pageMenu1Adapter = PageMenuAdapter(pageMenu1List, 1)
        pageMenu1Adapter.bindToRecyclerView(rv_page_order)

        rv_page_jh.layoutManager = GridLayoutManager(requireContext(), 4)
//        purchaseList.clear()
//        purchaseList.add(PageMenuBean("进货领料", R.mipmap.icon_picking, "0", false))
//        purchaseList.add(PageMenuBean("领料追溯查询", R.mipmap.icon_picking_search, "0", false))
//        purchaseList.add(PageMenuBean("进货单", R.mipmap.icon_purchase, "0", true))
//        purchaseList.add(PageMenuBean("进货查询", R.mipmap.icon_jh_cx, "0", true))
//        purchaseList.add(PageMenuBean("进货退回单", R.mipmap.icon_jh_th, "0", true))
//        purchaseList.add(PageMenuBean("进货退回查询", R.mipmap.icon_th_cx, "0", true))
        purchaseAdapter = PageMenuAdapter(purchaseList, 1)
        purchaseAdapter.bindToRecyclerView(rv_page_jh)

        rv_page_kc.layoutManager = GridLayoutManager(requireContext(), 4)
//        inventoryList.clear()
//        inventoryList.add(PageMenuBean("配件信息维护", R.mipmap.icon_part_info, "0", true))
//        inventoryList.add(PageMenuBean("库存商品一览表", R.mipmap.icon_kc_list, "0", true))
//        inventoryList.add(PageMenuBean("盘点单", R.mipmap.icon_check, "0", true))
//        inventoryList.add(PageMenuBean("盘点单查询", R.mipmap.icon_pd_cx, "0", true))
        inventoryAdapter = PageMenuAdapter(inventoryList, 1)
        inventoryAdapter.bindToRecyclerView(rv_page_kc)

        rv_page_alert.layoutManager = GridLayoutManager(requireContext(), 4)
//        pageMenu2List.clear()
//        pageMenu2List.add(PageMenuBean("保险提醒", R.mipmap.icon_alert_bx, "0", false))
//        pageMenu2List.add(PageMenuBean("年检提醒", R.mipmap.icon_alert_nj, "0", false))
//        pageMenu2List.add(PageMenuBean("保养提醒", R.mipmap.icon_alert_by, "0", false))
        pageMenu2Adapter = PageMenuAdapter(pageMenu2List, 1)
        pageMenu2Adapter.bindToRecyclerView(rv_page_alert)
        getBVDCUserId()

    }

    override fun initListener() {
        pageMenu1Adapter.setOnItemClickListener { adapter, view, position ->
            when (pageMenu1List[position].menu) {
                "接车开单" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    val intent = Intent(activity, NewReceptionActivity::class.java)
                    intent.putExtra("orderStatus", "new")
                    startActivity(intent)
                }

                "服务中工单" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    startActivity(Intent(activity, OrderListActivity::class.java))
                }

                "历史工单" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    val intent = Intent(activity, OrderHistoryActivity::class.java)
                    intent.putExtra("orderStatus", 1)
                    intent.putExtra("orderType", 2)
                    startActivity(intent)
                }

                "客户档案" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    startActivity(Intent(activity, CustomerListActivity::class.java))
                }
            }
        }
        pageMenu2Adapter.setOnItemClickListener { adapter, view, position ->
            when (pageMenu2List[position].menu) {
                "保险提醒" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    if (CheckPermissionUtils.checkPermission("保险提醒", requireActivity()))
                        startActivity(Intent(activity, BXAlertActivity::class.java))
                }

                "年检提醒" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    if (CheckPermissionUtils.checkPermission("年检提醒", requireActivity()))
                        startActivity(Intent(activity, NJAlertActivity::class.java))
                }

                "保养提醒" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    if (CheckPermissionUtils.checkPermission("保养提醒", requireActivity()))
                        startActivity(Intent(activity, BYAlertActivity::class.java))
                }
            }
        }
        purchaseAdapter.setOnItemClickListener { adapter, view, position ->
            when (purchaseList[position].menu) {
                "进货领料" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    if (CheckPermissionUtils.checkPermission("进货领料", requireActivity()))
                        startActivity(Intent(requireContext(), PricingListActivity::class.java))
                }

                "领料追溯查询" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    if (CheckPermissionUtils.checkPermission("领料追溯查询", requireActivity()))
                        startActivity(Intent(requireContext(), PickingListActivity::class.java))
                }

                "进货单" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    if (CheckPermissionUtils.checkPermission("进货单", requireActivity()))
                        startActivity(Intent(requireContext(), PurchaseListActivity::class.java))
                }

                "进货查询" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    if (CheckPermissionUtils.checkPermission("进货查询", requireActivity())) {
                        val intent = Intent(requireContext(), PurchaseHistoryActivity::class.java)
                        intent.putExtra("type", "1")
                        intent.putExtra("listType", "0")
                        startActivity(intent)
                    }
                }

                "进货退回单" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    if (CheckPermissionUtils.checkPermission("进货退回单", requireActivity()))
                        startActivity(Intent(requireContext(), PurchaseBackActivity::class.java))
                }

                "进货退回查询" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    if (CheckPermissionUtils.checkPermission("进货退回查询", requireActivity()))
                        startActivity(
                            Intent(
                                requireContext(),
                                PurchaseBackHistoryActivity::class.java
                            )
                        )
                }
            }
        }
        inventoryAdapter.setOnItemClickListener { adapter, view, position ->
            when (inventoryList[position].menu) {
                "配件信息维护" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    if (CheckPermissionUtils.checkPermission("配件信息维护", requireActivity()))
                        startActivity(Intent(requireContext(), PartListActivity::class.java))
                }

                "库存商品一览表" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    if (CheckPermissionUtils.checkPermission("库存商品一览表", requireActivity()))
                        startActivity(Intent(requireContext(), StockListActivity::class.java))
                }

                "盘点单" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    if (CheckPermissionUtils.checkPermission("盘点单", requireActivity()))
                        startActivity(Intent(requireContext(), CheckActivity::class.java))
                }

                "盘点单查询" -> {
                    if (DoubleClicksUtils.get().isFastDoubleClick)
                        return@setOnItemClickListener
                    if (CheckPermissionUtils.checkPermission("盘点单查询", requireActivity()))
                        startActivity(Intent(requireContext(), CheckHistoryActivity::class.java))
                }
            }
        }
    }

    private fun getData() {
        RetrofitUtils.get().getJson(
            UrlConstants.Page_Msg, HashMap(), requireActivity(),
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, HomePageBean::class.java)
                    SPUtils.getInstance().put("nickName", t.data.sysUser.nickName)
                    SPUtils.getInstance().put("shopId", t.data.kmShopHomeVo.id)//店铺id
                    SPUtils.getInstance().put("userId", t.data.sysUser.userId)
                    SPUtils.getInstance()
                        .put("madeType", t.data.kmShopHomeVo.madeType)//提成方法 1-按销售额 2-按利润
                    SPUtils.getInstance()
                        .put("madeFeeRate", t.data.kmShopHomeVo.madeFeeRate)//工时费提成比例
                    SPUtils.getInstance()
                        .put("madeRate", t.data.kmShopHomeVo.madeRate)//项目提成比例
                    tv_numInShop.text = t.data.numInShop ?: ""
                    tv_numOnShop.text = t.data.numOnShop
                    tv_turnoverDay.text = t.data.turnoverDay
                    tv_turnoverMonth.text = t.data.turnoverMonth
                    tv_tenure.text = "保有车辆  ${t.data.numCar}辆"
                    tv_numWorkorder.text = "累计完工  ${t.data.numWorkorder}辆"
                    tv_shop_name.text = t.data.kmShopHomeVo.fullname
                    SPUtils.getInstance().put("shopName", t.data.kmShopHomeVo.fullname)
                    SPUtils.getInstance().put("inviteCode", t.data.kmShopHomeVo.inviteCode)
                    pageMenu1List[1].subscript = t.data.workOrderInService
                    pageMenu1Adapter.setNewData(pageMenu1List)
                    pageMenu2List[0].subscript = t.data.remindInsure
                    pageMenu2List[1].subscript = t.data.remindMaintenance//保养
                    pageMenu2List[2].subscript = t.data.remindDue//年检
                    pageMenu2Adapter.setNewData(pageMenu2List)
                    purchaseList[0].subscript = t.data.workOrderPrice
                    purchaseAdapter.setNewData(purchaseList)
                    if (TextUtils.isEmpty(t.data.kmShopHomeVo.doorPhoto)) {
                        val drawable = TextDrawable.builder()
                            .beginConfig()
                            .withBorder(2) /* thickness in px */
                            .endConfig()
                            .buildRound(
                                t.data.kmShopHomeVo.fullname.substring(0, 1),
                                Color.parseColor("#4988FA")
                            )
                        civ_page.setImageDrawable(drawable)
                    } else {
                        GlideUtils.get()
                            .loadShopLogo(
                                activity!!,
                                t.data.kmShopHomeVo.doorPhoto,
                                civ_page
                            )
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }


    /**
     * 获取 bvdcUserID
     * http://bvdcapi.bsd128.com/bvdcPro.ashx?method=GetUserID&phone=13833127518&pwd=12345678
     */
    private fun getBVDCUserId() {
        val params = HashMap<String, String>()
        params["method"] = "GetUserID"
        params["phone"] = SPUtils.getInstance().getString("username")
        params["pwd"] = SPUtils.getInstance().getString("password")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BvdcUserIdResponse::class.java)
                    if (t.code == "1")
                        SPUtils.getInstance().put("bvdcUserID", t.outStr)
                    else
                        ToastUtils.showShort(t.message)
                    getMenuList()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun showExpirePop() {
        val popWindow =
            PopWindow.Builder(requireContext()).setView(R.layout.layout_pop_expire)
                .setWidthAndHeight(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ).setOutsideTouchable(true).setAnimStyle(R.style.BottomAnimation)
                .setBackGroundLevel(0.5f)
                .setChildrenView { contentView, pop ->
                    productAdapter = MyProductAdapter(productData)
                    val recyclerView = contentView.findViewById<RecyclerView>(R.id.rv_product)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    productAdapter.bindToRecyclerView(recyclerView)
                    contentView.findViewById<View>(R.id.tv_confirm).setOnClickListener {
                        pop.dismiss()
                        startActivity(Intent(requireActivity(), BuyProductActivity::class.java))
                    }
                    contentView.findViewById<View>(R.id.iv_close)
                        .setOnClickListener { pop.dismiss() }
                }
                .create()
        popWindow.isClippingEnabled = false
        popWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    private fun getMenuList() {
        LoadingUtils.showLoading(requireActivity(), "加载中...")
        RetrofitUtils.get().getJson(
            UrlConstants.MenuPermsUrl, HashMap(), requireActivity(),
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, TreeListBean::class.java)
                    purchaseList.clear()
                    inventoryList.clear()
                    pageMenu2List.clear()
                    for (data: TreeListData in t.data) {
                        if (data.menuName == "进货领料")
                            if (data.isPayPerm)
                                purchaseList.add(
                                    PageMenuBean(
                                        "进货领料",
                                        R.mipmap.icon_picking,
                                        "0",
                                        true
                                    )
                                )
                            else
                                purchaseList.add(
                                    PageMenuBean(
                                        "进货领料",
                                        R.mipmap.icon_picking,
                                        "0",
                                        false
                                    )
                                )
                        if (data.menuName == "领料追溯查询")
                            if (data.isPayPerm)
                                purchaseList.add(
                                    PageMenuBean(
                                        "领料追溯查询",
                                        R.mipmap.icon_picking_search,
                                        "0",
                                        true
                                    )
                                )
                            else
                                purchaseList.add(
                                    PageMenuBean(
                                        "领料追溯查询",
                                        R.mipmap.icon_picking_search,
                                        "0",
                                        false
                                    )
                                )
                        if (data.menuName == "进货单")
                            if (data.isPayPerm)
                                purchaseList.add(
                                    PageMenuBean(
                                        "进货单",
                                        R.mipmap.icon_purchase,
                                        "0",
                                        true
                                    )
                                )
                            else
                                purchaseList.add(
                                    PageMenuBean(
                                        "进货单",
                                        R.mipmap.icon_purchase,
                                        "0",
                                        false
                                    )
                                )
                        if (data.menuName == "进货查询")
                            if (data.isPayPerm)
                                purchaseList.add(
                                    PageMenuBean(
                                        "进货查询",
                                        R.mipmap.icon_jh_cx,
                                        "0",
                                        true
                                    )
                                )
                            else
                                purchaseList.add(
                                    PageMenuBean(
                                        "进货查询",
                                        R.mipmap.icon_jh_cx,
                                        "0",
                                        false
                                    )
                                )
                        if (data.menuName == "进货退回单")
                            if (data.isPayPerm)
                                purchaseList.add(
                                    PageMenuBean(
                                        "进货退回单",
                                        R.mipmap.icon_jh_th,
                                        "0",
                                        true
                                    )
                                )
                            else
                                purchaseList.add(
                                    PageMenuBean(
                                        "进货退回单",
                                        R.mipmap.icon_jh_th,
                                        "0",
                                        false
                                    )
                                )
                        if (data.menuName == "进货退回查询")
                            if (data.isPayPerm)
                                purchaseList.add(
                                    PageMenuBean(
                                        "进货退回查询",
                                        R.mipmap.icon_th_cx,
                                        "0",
                                        true
                                    )
                                )
                            else
                                purchaseList.add(
                                    PageMenuBean(
                                        "进货退回查询",
                                        R.mipmap.icon_th_cx,
                                        "0",
                                        false
                                    )
                                )
//
                        if (data.menuName == "配件信息维护")
                            if (data.isPayPerm)
                                inventoryList.add(
                                    PageMenuBean(
                                        "配件信息维护",
                                        R.mipmap.icon_part_info,
                                        "0",
                                        true
                                    )
                                )
                            else
                                inventoryList.add(
                                    PageMenuBean(
                                        "配件信息维护",
                                        R.mipmap.icon_part_info,
                                        "0",
                                        false
                                    )
                                )
                        if (data.menuName == "库存商品一览表")
                            if (data.isPayPerm)
                                inventoryList.add(
                                    PageMenuBean(
                                        "库存商品一览表",
                                        R.mipmap.icon_kc_list,
                                        "0",
                                        true
                                    )
                                )
                            else
                                inventoryList.add(
                                    PageMenuBean(
                                        "库存商品一览表",
                                        R.mipmap.icon_kc_list,
                                        "0",
                                        false
                                    )
                                )
                        if (data.menuName == "盘点单")
                            if (data.isPayPerm)
                                inventoryList.add(
                                    PageMenuBean(
                                        "盘点单",
                                        R.mipmap.icon_check,
                                        "0",
                                        true
                                    )
                                )
                            else
                                inventoryList.add(
                                    PageMenuBean(
                                        "盘点单",
                                        R.mipmap.icon_check,
                                        "0",
                                        false
                                    )
                                )
                        if (data.menuName == "盘点单查询")
                            if (data.isPayPerm)
                                inventoryList.add(
                                    PageMenuBean(
                                        "盘点单查询",
                                        R.mipmap.icon_pd_cx,
                                        "0",
                                        true
                                    )
                                )
                            else
                                inventoryList.add(
                                    PageMenuBean(
                                        "盘点单查询",
                                        R.mipmap.icon_pd_cx,
                                        "0",
                                        false
                                    )
                                )

                        if (data.menuName == "保险提醒")
                            if (data.isPayPerm)
                                pageMenu2List.add(
                                    PageMenuBean(
                                        "保险提醒",
                                        R.mipmap.icon_alert_bx,
                                        "0",
                                        true
                                    )
                                )
                            else
                                pageMenu2List.add(
                                    PageMenuBean(
                                        "保险提醒",
                                        R.mipmap.icon_alert_bx,
                                        "0",
                                        false
                                    )
                                )
                        if (data.menuName == "年检提醒")
                            if (data.isPayPerm)
                                pageMenu2List.add(
                                    PageMenuBean(
                                        "年检提醒",
                                        R.mipmap.icon_alert_nj,
                                        "0",
                                        true
                                    )
                                )
                            else
                                pageMenu2List.add(
                                    PageMenuBean(
                                        "年检提醒",
                                        R.mipmap.icon_alert_nj,
                                        "0",
                                        false
                                    )
                                )
                        if (data.menuName == "保养提醒")
                            if (data.isPayPerm)
                                pageMenu2List.add(
                                    PageMenuBean(
                                        "保养提醒",
                                        R.mipmap.icon_alert_by,
                                        "0",
                                        true
                                    )
                                )
                            else
                                pageMenu2List.add(
                                    PageMenuBean(
                                        "保养提醒",
                                        R.mipmap.icon_alert_by,
                                        "0",
                                        false
                                    )
                                )
                    }
                    purchaseAdapter.setNewData(purchaseList)
                    inventoryAdapter.setNewData(inventoryList)
                    pageMenu2Adapter.setNewData(pageMenu2List)
                    getData()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        ImmersionBar.with(this)
            .flymeOSStatusBarFontColor(R.color.color_black)
            .statusBarDarkFont(true)
            .init()
        getMenuList()
    }

}