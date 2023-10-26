package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.MemberHandleContainAdapter
import com.bossed.waej.adapter.MemberShopAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants.NewVipUrl
import com.bossed.waej.javebean.*
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_member_handle.*

class MemberHandleActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var containAdapter: MemberHandleContainAdapter
    private val containBean = ArrayList<KmShopClubCardItems>()
    private lateinit var shopAdapter: MemberShopAdapter
    private val shopBean = ArrayList<ShopRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_member_handle
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_member_handle)
        rv_member_contain.layoutManager = GridLayoutManager(this, 3)
        containAdapter = MemberHandleContainAdapter(containBean)
        containAdapter.bindToRecyclerView(rv_member_contain)
        containAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        rv_member_shop.layoutManager = LinearLayoutManager(this)
        shopAdapter = MemberShopAdapter(shopBean)
        shopAdapter.bindToRecyclerView(rv_member_shop)
        shopAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        getVipMsg()
    }

    override fun initListener() {
        tb_member_handle.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        cb_forever.setOnCheckedChangeListener { buttonView, isChecked ->
            cb_term.isChecked = !isChecked
            tv_expire_time.isEnabled = !isChecked
            tv_expire_time.text = ""
        }
        cb_term.setOnCheckedChangeListener { buttonView, isChecked ->
            cb_forever.isChecked = !isChecked
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_add_give -> {
//                showAddGivePop()
            }
            R.id.tv_expire_time -> {
//                selectDate(tv_expire_time, "到期日期")
            }
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                handle()
            }
        }
    }

//    private fun showAddGivePop() {
//        val popWindow = object : BasePopupWindow(this) {
//            override fun initShowAnimation(): Animation {
//                return getTranslateVerticalAnimation(1f, 0f, 500)
//            }
//
//            override fun getClickToDismissView(): View {
//                return popupView!!.findViewById(R.id.rl_pop_member_content)
//            }
//
//            @SuppressLint("InflateParams")
//            override fun onCreatePopupView(): View {
//                popupView =
//                    LayoutInflater.from(context).inflate(R.layout.layout_pop_gift_add, null)
//                val name = popupView!!.findViewById<EditText>(R.id.et_zp_name)
//                val number = popupView!!.findViewById<EditText>(R.id.et_zp_number)
//                val materialCost = popupView!!.findViewById<EditText>(R.id.et_materialCost)
//                popupView!!.findViewById<View>(R.id.tv_cancel).setOnClickListener {
//                    dismiss()
//                }
//                val time = popupView!!.findViewById<TextView>(R.id.tv_expire_time)
//                time.setOnClickListener {
////                    selectDateAndTime(time, "到期日期")
//                }
//                popupView!!.findViewById<View>(R.id.tv_sure_manual).setOnClickListener {
//                    if (TextUtils.isEmpty(name.text.toString()) || TextUtils.isEmpty(number.text.toString()))
//                        return@setOnClickListener
//                    val item = KmShopClubCardItems()
//                    item.itemName = name.text.toString()
//                    item.availableQuantity = number.text.toString().toFloat()
//                    item.materialCost = materialCost.text.toString().toFloat()
//                    item.flagGive = true
//                    containBean.add(item)
//                    containAdapter.setNewData(containBean)
//                    dismiss()
//                }
//                return popupView!!
//            }
//
//            override fun initAnimaView(): View {
//                return popupView!!.findViewById(R.id.rl_pop_member_content)
//            }
//
//            override fun initExitAnimation(): Animation {
//                return getTranslateVerticalAnimation(0f, 1f, 500)
//            }
//        }
//        popWindow.showPopupWindow()
//    }

    private fun handle() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["carNo"] = tv_car_no.text.toString()
        params["cardNo"] = intent.getStringExtra("cardNo")!!
        params["cardName"] = tv_vip_name.text.toString()
        params["password"] = et_card_pass.text.toString()
        params["username"] = et_kh_name.text.toString()
        params["phone"] = et_kh_phone.text.toString()
        params["remark"] = et_remark.text.toString()
        params["cardId"] = intent.getIntExtra("id", 0)
        params["kmShopCardItemsList"] = containBean
        params["tremTime"] =
            when {
                cb_forever.isChecked -> "null"
                cb_term.isChecked -> tv_expire_time.text.toString()
                else -> ""
            }
        val sb = StringBuilder()
        for (row: ShopRow in shopBean) {
            if (row.isSelect) {
                if (sb.isNotEmpty())
                    sb.append(",")
                sb.append(row.fullname)
            }
        }
        params["canUseShopIds"] = sb.toString()
        RetrofitUtils.get()
            .postJson(NewVipUrl, params, this,object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    ToastUtils.showShort("办理成功")
                    finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }

            })
    }

    private fun getVipMsg() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getVipCardMsg(intent.getIntExtra("id", 0))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<VipCardMsgBean>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: VipCardMsgBean) {
                    when (t.code) {
                        200 -> {
                            tv_vip_name.text = t.data.cardName
                            containBean.addAll(t.data.kmShopClubCardItemsList)
                            containAdapter.setNewData(containBean)
                            getShopList()
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@MemberHandleActivity)
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
                    LoadingUtils.dismissLoading()
                }
            })
    }

    private fun getShopList() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getShopList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<MemberShopBean>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: MemberShopBean) {
                    when (t.code) {
                        200 -> {
                            shopBean.clear()
                            shopBean.addAll(t.rows)
                            shopAdapter.setNewData(shopBean)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@MemberHandleActivity)
                        }
                        else -> {
                            ToastUtils.showShort(t.msg)
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

}