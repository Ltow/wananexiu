package com.bossed.waej.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.adapter.PageMenuAdapter
import com.bossed.waej.base.BaseFragment
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.FinanceResponse
import com.bossed.waej.javebean.PageMenuBean
import com.bossed.waej.javebean.TreeListBean
import com.bossed.waej.javebean.TreeListData
import com.bossed.waej.ui.*
import com.bossed.waej.util.CheckPermissionUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import kotlinx.android.synthetic.main.fragment_finance.*

class FinanceFragment : BaseFragment() {
    private lateinit var pageMenu1Adapter: PageMenuAdapter
    private val pageMenu1List = ArrayList<PageMenuBean>()
    private lateinit var pageMenu2Adapter: PageMenuAdapter
    private val pageMenu2List = ArrayList<PageMenuBean>()
    private lateinit var pageMenu3Adapter: PageMenuAdapter
    private val pageMenu3List = ArrayList<PageMenuBean>()

    override fun lazyLoad() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_finance
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        val layoutParams = ll_top.layoutParams as RelativeLayout.LayoutParams
        layoutParams.height = BarUtils.getStatusBarHeight() + ConvertUtils.dp2px(80f)
        ll_top.layoutParams = layoutParams
        rv_current_account.layoutManager = GridLayoutManager(requireContext(), 4)
//        pageMenu1List.clear()
//        pageMenu1List.add(PageMenuBean("应收款总览", R.mipmap.icon_ysk_zl, "0", false))
//        pageMenu1List.add(PageMenuBean("应付款总览", R.mipmap.icon_yfk_zl, "0", false))
//        pageMenu1List.add(PageMenuBean("收款", R.mipmap.icon_sk, "0", false))
//        pageMenu1List.add(PageMenuBean("付款", R.mipmap.icon_fk, "0", false))
//        pageMenu1List.add(PageMenuBean("收款减免", R.mipmap.icon_sk_jm, "0", false))
//        pageMenu1List.add(PageMenuBean("付款减免", R.mipmap.icon_fk_jm, "0", false))
        pageMenu1Adapter = PageMenuAdapter(pageMenu1List, 2)
        pageMenu1Adapter.bindToRecyclerView(rv_current_account)
        rv_bank_account.layoutManager = GridLayoutManager(requireContext(), 4)
//        pageMenu2List.clear()
//        pageMenu2List.add(PageMenuBean("银行账号维护", R.mipmap.icon_bank_zh_wh, "0", false))
//        pageMenu2List.add(PageMenuBean("日记账查询", R.mipmap.icon_rjz_cx, "0", false))
//        pageMenu2List.add(PageMenuBean("余额调整", R.mipmap.icon_ye_tz, "0", false))
//        pageMenu2List.add(PageMenuBean("日记账发生", R.mipmap.icon_rjz_fs, "0", false))
//        pageMenu2List.add(PageMenuBean("银行账号互转", R.mipmap.icon_bank_zh_hz, "0", false))
        pageMenu2Adapter = PageMenuAdapter(pageMenu2List, 2)
        pageMenu2Adapter.bindToRecyclerView(rv_bank_account)
        rv_cost.layoutManager = GridLayoutManager(requireContext(), 4)
//        pageMenu3List.clear()
//        pageMenu3List.add(PageMenuBean("费用类别设置", R.mipmap.icon_th_lb_sz, "0", false))
//        pageMenu3List.add(PageMenuBean("费用录入", R.mipmap.icon_fy_lr, "0", false))
//        pageMenu3List.add(PageMenuBean("费用查询统计", R.mipmap.icon_fy_cx_tj, "0", false))
        pageMenu3Adapter = PageMenuAdapter(pageMenu3List, 2)
        pageMenu3Adapter.bindToRecyclerView(rv_cost)
    }

    override fun initListener() {
        pageMenu1Adapter.setOnItemClickListener { adapter, view, position ->
            when (pageMenu1List[position].menu) {
                "应收款总览" -> {
                    if (CheckPermissionUtils.checkPermission("应收款总览", requireActivity()))
                        startActivity(Intent(requireContext(), ReceivableListActivity::class.java))
                }

                "应付款总览" -> {
                    if (CheckPermissionUtils.checkPermission("应付款总览", requireActivity()))
                        startActivity(Intent(requireContext(), CopeWithListActivity::class.java))
                }

                "收款" -> {
                    if (CheckPermissionUtils.checkPermission("收款", requireActivity()))
                        startActivity(Intent(requireContext(), CollectionActivity::class.java))
                }

                "付款" -> {
                    if (CheckPermissionUtils.checkPermission("付款", requireActivity()))
                        startActivity(Intent(requireContext(), PaymentActivity::class.java))
                }

                "收款减免" -> {
                    if (CheckPermissionUtils.checkPermission("收款减免", requireActivity()))
                        startActivity(
                            Intent(
                                requireContext(),
                                CollectionReductionActivity::class.java
                            )
                        )
                }

                "付款减免" -> {
                    if (CheckPermissionUtils.checkPermission("付款减免", requireActivity()))
                        startActivity(
                            Intent(
                                requireContext(),
                                PaymentReductionActivity::class.java
                            )
                        )
                }

                "退货收款" -> {
                    if (CheckPermissionUtils.checkPermission("退货收款", requireActivity()))
                        startActivity(Intent(requireContext(), BackCollectionActivity::class.java))
                }
            }
        }
        pageMenu2Adapter.setOnItemClickListener { adapter, view, position ->
            when (pageMenu2List[position].menu) {
                "银行账号维护" -> {
                    if (CheckPermissionUtils.checkPermission("银行账号维护", requireActivity())) {
                        val intent = Intent(requireContext(), BankAccountUpholdActivity::class.java)
                        intent.putExtra("type", "2")
                        startActivity(intent)
                    }
                }

                "日记账查询" -> {
                    if (CheckPermissionUtils.checkPermission("日记账查询", requireActivity()))
                        startActivity(Intent(requireContext(), JournalSearchActivity::class.java))
                }

                "余额调整" -> {
                    if (CheckPermissionUtils.checkPermission("余额调整", requireActivity()))
                        startActivity(Intent(requireContext(), BalanceAdjActivity::class.java))
                }

                "日记账发生" -> {
                    if (CheckPermissionUtils.checkPermission("日记账发生", requireActivity()))
                        startActivity(Intent(requireContext(), JournalOccurActivity::class.java))
                }

                "银行账号互转" -> {
                    if (CheckPermissionUtils.checkPermission("银行账号互转", requireActivity()))
                        startActivity(Intent(requireContext(), BankRotationActivity::class.java))
                }
            }
        }
        pageMenu3Adapter.setOnItemClickListener { adapter, view, position ->
            when (pageMenu3List[position].menu) {
                "费用类别设置" -> {
                    if (CheckPermissionUtils.checkPermission("费用类别设置", requireActivity()))
                        startActivity(Intent(requireContext(), CostTypeActivity::class.java))
                }

                "费用录入" -> {
                    if (CheckPermissionUtils.checkPermission("费用录入", requireActivity()))
                        startActivity(Intent(requireContext(), CostEntryActivity::class.java))
                }

                "费用查询统计" -> {
                    if (CheckPermissionUtils.checkPermission("费用查询统计", requireActivity()))
                        startActivity(Intent(requireContext(), CostCountActivity::class.java))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        BarUtils.setStatusBarLightMode(requireActivity(), false)
        RetrofitUtils.get().getJson(
            UrlConstants.MenuPermsUrl, java.util.HashMap(), requireActivity(),
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, TreeListBean::class.java)
                    pageMenu1List.clear()
                    pageMenu2List.clear()
                    pageMenu3List.clear()
                    for (data: TreeListData in t.data) {
                        if (data.menuName == "应收款总览") {
                            if (data.isPayPerm)
                                pageMenu1List.add(
                                    PageMenuBean(
                                        "应收款总览", R.mipmap.icon_ysk_zl, "0", true
                                    )
                                )
                            else
                                pageMenu1List.add(
                                    PageMenuBean(
                                        "应收款总览", R.mipmap.icon_ysk_zl, "0", false
                                    )
                                )
                        }
                        if (data.menuName == "应付款总览") {
                            if (data.isPayPerm)
                                pageMenu1List.add(
                                    PageMenuBean(
                                        "应付款总览", R.mipmap.icon_yfk_zl, "0", true
                                    )
                                )
                            else
                                pageMenu1List.add(
                                    PageMenuBean(
                                        "应付款总览", R.mipmap.icon_yfk_zl, "0", false
                                    )
                                )
                        }
                        if (data.menuName == "收款") {
                            if (data.isPayPerm)
                                pageMenu1List.add(PageMenuBean("收款", R.mipmap.icon_sk, "0", true))
                            else
                                pageMenu1List.add(
                                    PageMenuBean(
                                        "收款",
                                        R.mipmap.icon_sk,
                                        "0",
                                        false
                                    )
                                )
                        }
                        if (data.menuName == "付款") {
                            if (data.isPayPerm)
                                pageMenu1List.add(PageMenuBean("付款", R.mipmap.icon_fk, "0", true))
                            else
                                pageMenu1List.add(
                                    PageMenuBean(
                                        "付款",
                                        R.mipmap.icon_fk,
                                        "0",
                                        false
                                    )
                                )
                        }
                        if (data.menuName == "收款减免") {
                            if (data.isPayPerm)
                                pageMenu1List.add(
                                    PageMenuBean(
                                        "收款减免",
                                        R.mipmap.icon_sk_jm,
                                        "0",
                                        true
                                    )
                                )
                            else
                                pageMenu1List.add(
                                    PageMenuBean(
                                        "收款减免",
                                        R.mipmap.icon_sk_jm,
                                        "0",
                                        false
                                    )
                                )
                        }
                        if (data.menuName == "付款减免") {
                            if (data.isPayPerm)
                                pageMenu1List.add(
                                    PageMenuBean(
                                        "付款减免",
                                        R.mipmap.icon_fk_jm,
                                        "0",
                                        true
                                    )
                                )
                            else
                                pageMenu1List.add(
                                    PageMenuBean(
                                        "付款减免",
                                        R.mipmap.icon_fk_jm,
                                        "0",
                                        false
                                    )
                                )
                        }
                        if (data.menuName == "退货收款") {
                            if (data.isPayPerm)
                                pageMenu1List.add(
                                    PageMenuBean(
                                        "退货收款",
                                        R.mipmap.icon_fk_jm,
                                        "0",
                                        true
                                    )
                                )
                            else
                                pageMenu1List.add(
                                    PageMenuBean(
                                        "退货收款",
                                        R.mipmap.icon_fk_jm,
                                        "0",
                                        false
                                    )
                                )
                        }

                        if (data.menuName == "银行账号维护") {
                            if (data.isPayPerm)
                                pageMenu2List.add(
                                    PageMenuBean(
                                        "银行账号维护",
                                        R.mipmap.icon_bank_zh_wh,
                                        "0",
                                        true
                                    )
                                )
                            else
                                pageMenu2List.add(
                                    PageMenuBean(
                                        "银行账号维护",
                                        R.mipmap.icon_bank_zh_wh,
                                        "0",
                                        false
                                    )
                                )
                        }
                        if (data.menuName == "日记账查询") {
                            if (data.isPayPerm)
                                pageMenu2List.add(
                                    PageMenuBean(
                                        "日记账查询",
                                        R.mipmap.icon_rjz_cx,
                                        "0",
                                        true
                                    )
                                )
                            else
                                pageMenu2List.add(
                                    PageMenuBean(
                                        "日记账查询",
                                        R.mipmap.icon_rjz_cx,
                                        "0",
                                        false
                                    )
                                )
                        }
                        if (data.menuName == "余额调整") {
                            if (data.isPayPerm)
                                pageMenu2List.add(
                                    PageMenuBean(
                                        "余额调整",
                                        R.mipmap.icon_ye_tz,
                                        "0",
                                        true
                                    )
                                )
                            else
                                pageMenu2List.add(
                                    PageMenuBean(
                                        "余额调整",
                                        R.mipmap.icon_ye_tz,
                                        "0",
                                        false
                                    )
                                )
                        }
                        if (data.menuName == "日记账发生") {
                            if (data.isPayPerm)
                                pageMenu2List.add(
                                    PageMenuBean(
                                        "日记账发生",
                                        R.mipmap.icon_rjz_fs,
                                        "0",
                                        true
                                    )
                                )
                            else
                                pageMenu2List.add(
                                    PageMenuBean(
                                        "日记账发生",
                                        R.mipmap.icon_rjz_fs,
                                        "0",
                                        false
                                    )
                                )
                        }
                        if (data.menuName == "银行账号互转") {
                            if (data.isPayPerm)
                                pageMenu2List.add(
                                    PageMenuBean(
                                        "银行账号互转",
                                        R.mipmap.icon_bank_zh_hz,
                                        "0",
                                        true
                                    )
                                )
                            else
                                pageMenu2List.add(
                                    PageMenuBean(
                                        "银行账号互转",
                                        R.mipmap.icon_bank_zh_hz,
                                        "0",
                                        false
                                    )
                                )
                        }

                        if (data.menuName == "费用类别设置") {
                            if (data.isPayPerm)
                                pageMenu3List.add(
                                    PageMenuBean(
                                        "费用类别设置",
                                        R.mipmap.icon_th_lb_sz,
                                        "0",
                                        true
                                    )
                                )
                            else
                                pageMenu3List.add(
                                    PageMenuBean(
                                        "费用类别设置",
                                        R.mipmap.icon_th_lb_sz,
                                        "0",
                                        false
                                    )
                                )
                        }
                        if (data.menuName == "费用录入") {
                            if (data.isPayPerm)
                                pageMenu3List.add(
                                    PageMenuBean(
                                        "费用录入",
                                        R.mipmap.icon_fy_lr,
                                        "0",
                                        true
                                    )
                                )
                            else
                                pageMenu3List.add(
                                    PageMenuBean(
                                        "费用录入",
                                        R.mipmap.icon_fy_lr,
                                        "0",
                                        false
                                    )
                                )
                        }
                        if (data.menuName == "费用查询统计") {
                            if (data.isPayPerm)
                                pageMenu3List.add(
                                    PageMenuBean(
                                        "费用查询统计",
                                        R.mipmap.icon_fy_cx_tj,
                                        "0",
                                        true
                                    )
                                )
                            else
                                pageMenu3List.add(
                                    PageMenuBean(
                                        "费用查询统计",
                                        R.mipmap.icon_fy_cx_tj,
                                        "0",
                                        false
                                    )
                                )
                        }
                    }
                    pageMenu1Adapter.setNewData(pageMenu1List)
                    pageMenu2Adapter.setNewData(pageMenu2List)
                    pageMenu3Adapter.setNewData(pageMenu3List)
                    getCount()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getCount() {
        LoadingUtils.showLoading(requireActivity(), "加载中...")
        RetrofitUtils.get()
            .getJson("km/finace/getHome", HashMap(), requireActivity(),
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        LogUtils.d("tag", s)
                        val t = GsonUtils.fromJson(s, FinanceResponse::class.java)
                        tv_balance.text = t.data.balance
                        tv_payment.text = t.data.payment
                        tv_receivables.text = t.data.receivables
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }
}