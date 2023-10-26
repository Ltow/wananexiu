package com.bossed.waej.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.MaineMenuContentAdapter
import com.bossed.waej.adapter.MineMenuTopAdapter
import com.bossed.waej.base.BaseMVPFragment
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.contract.MineContract
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.MineMenuBean
import com.bossed.waej.javebean.MineResponse
import com.bossed.waej.presenter.MinePresenter
import com.bossed.waej.ui.AccountActivity
import com.bossed.waej.ui.BuyProductActivity
import com.bossed.waej.ui.DepartmentActivity
import com.bossed.waej.ui.FeedbackActivity
import com.bossed.waej.ui.LoginActivity
import com.bossed.waej.ui.MyProductActivity
import com.bossed.waej.ui.PerInfoActivity
import com.bossed.waej.ui.PersonListActivity
import com.bossed.waej.ui.RemitAccountActivity
import com.bossed.waej.ui.ShopMsgActivity
import com.bossed.waej.ui.SupplierListActivity
import com.bossed.waej.ui.SystemSettingsActivity
import com.bossed.waej.ui.WalletActivity
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.GlideUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.OnNoRepeatItemClickListener
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2021-07-22
 * Time: 11:23
 */
class MineFragment : BaseMVPFragment<MinePresenter>(), MineContract.MineView, View.OnClickListener {
    private lateinit var topAdapter: MineMenuTopAdapter
    private val topBean = ArrayList<MineMenuBean>()
    private lateinit var contentAdapter: MaineMenuContentAdapter
    private val contentBean = ArrayList<MineMenuBean>()
    private var avatar = ""
    private var nickName = ""
    private var phonenumber = ""
    private var userType = ""
    private var merchantNo = ""//银联商户号
//    private var status = -1//分账审核状态 0-草稿 1-审核通过 2-审核驳回  3-审核中


    override fun lazyLoad() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        setTopMargin(tb_mine)
        rv_mine_menu_top.layoutManager = GridLayoutManager(activity, 4)
        topAdapter = MineMenuTopAdapter(topBean)
        topAdapter.bindToRecyclerView(rv_mine_menu_top)
        topBean.clear()
        topBean.add(MineMenuBean("电子健康档案", R.mipmap.icon_records))
        topBean.add(MineMenuBean("产品开通", R.mipmap.icon_activation))
        topBean.add(MineMenuBean("供应商管理", R.mipmap.icon_supplier))
//        topBean.add(MineMenuBean("供应商供货流水", R.mipmap.icon_supplier_daybook))
        topBean.add(MineMenuBean("我的产品", R.mipmap.icon_my_product))
        topBean.add(MineMenuBean("意见反馈", R.mipmap.icon1356))
//        topBean.add(MineMenuBean("系统设置", R.mipmap.icon1357))
        topAdapter.setNewData(topBean)
        rv_mine_menu_content.layoutManager = GridLayoutManager(activity, 4)
        contentAdapter = MaineMenuContentAdapter(contentBean)
        contentAdapter.bindToRecyclerView(rv_mine_menu_content)
        contentBean.clear()
        contentBean.add(MineMenuBean("门店信息", R.mipmap.icon1353))
        contentBean.add(MineMenuBean("部门角色", R.mipmap.icon_department))
        contentBean.add(MineMenuBean("员工管理", R.mipmap.icon_role))
        contentBean.add(MineMenuBean("账号管理", R.mipmap.icon1352))
//        contentBean.add(MineMenuBean("供应商管理", R.mipmap.icon1359))
//        contentBean.add(MineMenuBean("客户管理", R.mipmap.icon13592))
//        contentBean.add(MineMenuBean("提成设置", R.mipmap.icon1355))
//        contentBean.add(MineMenuBean("意见反馈", R.mipmap.icon1356))
//        contentBean.add(MineMenuBean("系统设置", R.mipmap.icon1357))
//        contentBean.add(MineMenuBean("小程序二维码", R.mipmap.icon1360))
        contentAdapter.setNewData(contentBean)
    }

    override fun initListener() {
        tv_per_info.setOnClickListener(this)
        niv_mine.setOnClickListener(this)
        tv_system_settings.setOnClickListener(this)
        btn_logout.setOnClickListener(this)
        tv_wallet.setOnClickListener(this)
        topAdapter.onItemClickListener = object : OnNoRepeatItemClickListener {
            override fun onNoRepeatItemItemClick(
                adapter: BaseQuickAdapter<*, *>?,
                view: View?,
                position: Int
            ) {
                when (topBean[position].name) {
                    "电子健康档案" -> {
                        if (userType == "qx_user") {
                            ToastUtils.showShort("暂无使用权限")
                            return
                        }
                        ToastUtils.showShort("功能正在开发中")
                    }

                    "意见反馈" -> {
                        if (userType == "qx_user") {
                            ToastUtils.showShort("暂无使用权限")
                            return
                        }
                        val intent = Intent(activity, FeedbackActivity::class.java)
                        startActivity(intent)
                    }
//                    "系统设置" -> {
//                        val intent = Intent(activity, SystemSettingsActivity::class.java)
//                        startActivity(intent)
//                    }
                    "产品开通" -> {
                        if (userType == "qx_user") {
                            ToastUtils.showShort("暂无使用权限")
                            return
                        }
                        startActivity(Intent(activity, BuyProductActivity::class.java))
                    }

                    "供应商管理" -> {
                        if (userType == "qx_user") {
                            ToastUtils.showShort("暂无使用权限")
                            return
                        }
                        val intent = Intent(activity, SupplierListActivity::class.java)
                        intent.putExtra("type", "2")//传1表示从注册流程过去
                        startActivity(intent)
                    }

                    "我的产品" -> {
                        if (userType == "qx_user") {
                            ToastUtils.showShort("暂无使用权限")
                            return
                        }
                        startActivity(Intent(activity, MyProductActivity::class.java))
                    }
                }
            }
        }
        contentAdapter.onItemClickListener = object : OnNoRepeatItemClickListener {
            override fun onNoRepeatItemItemClick(
                adapter: BaseQuickAdapter<*, *>?,
                view: View?,
                position: Int
            ) {
                when (contentBean[position].name) {
                    "门店信息" -> {
                        if (userType == "qx_user") {
                            ToastUtils.showShort("暂无使用权限")
                            return
                        }
                        startActivity(Intent(activity, ShopMsgActivity::class.java))
                    }

                    "部门角色" -> {
                        if (userType == "qx_user") {
                            ToastUtils.showShort("暂无使用权限")
                            return
                        }
                        val intent = Intent(activity, DepartmentActivity::class.java)
                        intent.putExtra("type", "mine")
                        startActivity(intent)
                    }

                    "员工管理" -> {
                        if (userType == "qx_user") {
                            ToastUtils.showShort("暂无使用权限")
                            return
                        }
                        val intent = Intent(activity, PersonListActivity::class.java)
                        intent.putExtra("type", "mine")
                        startActivity(intent)
                    }

                    "账号管理" -> {
                        if (userType == "qx_user") {
                            ToastUtils.showShort("暂无使用权限")
                            return
                        }
                        startActivity(Intent(activity, AccountActivity::class.java))
                    }
//                    "供应商管理" -> {
//                        val intent = Intent(activity, SupplierListActivity::class.java)
//                        intent.putExtra("selSupplier", "archives")
//                        startActivity(intent)
//                    }
//                    "客户管理" -> {
//                        startActivity(Intent(activity, CustomerListActivity::class.java))
//                    }
//                    "提成设置" -> {
//                        val intent = Intent(activity, CommissionSettingsActivity::class.java)
//                        startActivity(intent)
//                    }
//                    "意见反馈" -> {
//                        val intent = Intent(activity, FeedbackActivity::class.java)
//                        startActivity(intent)
//                    }
//                    "系统设置" -> {
//                        val intent = Intent(activity, SystemSettingsActivity::class.java)
//                        startActivity(intent)
//                    }
//                    "小程序二维码" -> {
//                    }
                }
            }
        }
        tv_inviteCode.setOnClickListener(this)
    }

    override fun onExitSuccess(success: BaseResponse) {
        when (success.code) {
            200 -> {
                requireActivity().finish()
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
            }

            401 -> {
                PopupWindowUtils.get().showLoginOutTimePop(mActivity)
            }

            else -> {
                ToastUtils.showShort(success.msg)
            }
        }
    }

    override fun onUserInfoSuccess(success: MineResponse) {
        when (success.code) {
            200 -> {
                if (TextUtils.isEmpty(success.data.sysUser.avatar))
                    GlideUtils.get().loadHead(requireActivity(), "", niv_mine)
                else
                    GlideUtils.get()
                        .loadHead(requireActivity(), success.data.sysUser.avatar, niv_mine)
                avatar = success.data.sysUser.avatar
                nickName = success.data.sysUser.nickName
                phonenumber = success.data.sysUser.phonenumber
                tv_shop_name.text = success.data.kmShopHomeVo.fullname
                SPUtils.getInstance().put("fullname", success.data.kmShopHomeVo.fullname)
                tv_name.text = success.data.sysUser.phonenumber
                tv_user.text = success.data.sysUser.nickName
                tv_money.text = success.data.kmShopWallet.balance.toString()
                userType = success.data.sysUser.userType
                merchantNo = success.data.kmShopHomeVo.merchantNo
//                status = success.data.kmShopHomeVo.status
                tv_inviteCode.text = success.data.kmShopHomeVo.inviteCode
            }

            401 -> {
                PopupWindowUtils.get().showLoginOutTimePop(requireActivity())
            }

            else -> {
                ToastUtils.showShort(success.msg)
            }
        }
    }

    override fun onReLogin() {
        PopupWindowUtils.get().showLoginOutTimePop(requireActivity())
    }

    override fun showError(e: String) {
        ToastUtils.showShort(e)
    }

    override fun getPresenter(): MinePresenter {
        return MinePresenter()
    }

    override fun onResume() {
        super.onResume()
        BarUtils.setStatusBarLightMode(requireActivity(), false)
        mPresenter?.getUserInfo(requireActivity())
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_wallet -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
//                when (status) {
//                    0 -> {
//                        PopupWindowUtils.get().showSetConfirmAlertDialog(
//                            requireContext(),
//                            "请先完善商户信息！",
//                            "去完善",
//                            "#3477fc"
//                        ) {
//                            startActivity(
//                                Intent(
//                                    requireContext(),
//                                    RemitAccountActivity::class.java
//                                )
//                            )
//                        }
//                    }
//
//                    1 -> {
                        val intent = Intent(requireContext(), WalletActivity::class.java)
                        intent.putExtra("merchantNo", merchantNo)
                        startActivity(intent)
//                    }
//
//                    2 -> {
//                        startActivity(Intent(requireContext(), RemitAccountActivity::class.java))
//                    }
//
//                    3 -> {
//                        ToastUtils.showShort("商户分账信息正在审核中,请耐心等待！")
//                    }
//                }
            }

            R.id.tv_per_info -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(requireActivity(), PerInfoActivity::class.java)
                intent.putExtra("avatar", avatar)
                intent.putExtra("nickName", nickName)
                intent.putExtra("phonenumber", phonenumber)
                launcher.launch(intent)
            }

            R.id.niv_mine -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showPhotoPop(avatar, requireContext(), ll_content)
            }

            R.id.tv_system_settings -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(activity, SystemSettingsActivity::class.java)
                startActivity(intent)
            }

            R.id.btn_logout -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                mPresenter?.exit(requireActivity())
            }

            R.id.tv_inviteCode -> {//邀请码
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (TextUtils.isEmpty(tv_inviteCode.text.toString())) {
                    PopupWindowUtils.get().showEditPop(
                        requireContext(), ll_content, "添加邀请码", "邀请码", "请输入邀请码"
                    ) {
                        LoadingUtils.showLoading(requireActivity(), "加载中...")
                        val params = HashMap<String, Any>()
                        params["id"] = SPUtils.getInstance().getInt("shopId")
                        params["fullname"] = SPUtils.getInstance().getString("fullname")
                        params["inviteCode"] = it
                        RetrofitUtils.get()
                            .putJson(UrlConstants.UpdateShopMsgUrl, params, requireActivity(),
                                object : RetrofitUtils.OnCallBackListener {
                                    override fun onSuccess(s: String) {
                                        val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                                        if (t.code == 200) {
                                            tv_inviteCode.text = it
                                        }
                                        ToastUtils.showShort(t.msg)
                                    }

                                    override fun onFailed(e: String) {
                                        ToastUtils.showShort(e)
                                    }
                                })
                    }
                } else {

                }
            }
        }
    }

    private val launcher = registerForActivityResult(StartActivityForResult()) {
        when (it.resultCode) {
            AppCompatActivity.RESULT_OK -> {
                LoadingUtils.showLoading(requireActivity(), "加载中...")
                val params = HashMap<String, Any>()
                params["userId"] = SPUtils.getInstance().getInt("userId")
                params["avatar"] = it.data?.getStringExtra("imgUrl")!!
                RetrofitUtils.get().postJson(
                    UrlConstants.UpdateUserUrl, params, requireActivity(),
                    object : RetrofitUtils.OnCallBackListener {
                        override fun onSuccess(s: String) {
                            val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                            if (t.code == 200) {
                                avatar = it.data?.getStringExtra("imgUrl")!!
                                GlideUtils.get().loadHead(
                                    requireContext(), it.data?.getStringExtra("imgUrl")!!, niv_mine
                                )
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

}