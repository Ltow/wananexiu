package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.OrderXMAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBDispatch
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants.CommissionUrl
import com.bossed.waej.javebean.PersonRow
import com.bossed.waej.javebean.ItemDispatchBean
import com.bossed.waej.javebean.JieCheItem
import com.bossed.waej.javebean.JieCheResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_commission_msg.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class DispatchCommissionMsgActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var xmAdapter: OrderXMAdapter
    private val xmBean = ArrayList<JieCheItem>()
    private var dispatchPosition = -1

    override fun getLayoutId(): Int {
        return R.layout.activity_commission_msg
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_commission)
        rv_commission_xm.layoutManager = LinearLayoutManager(this)
        xmAdapter = OrderXMAdapter(xmBean, 3)
        xmAdapter.bindToRecyclerView(rv_commission_xm)
        xmAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        getOrderMsg()
    }

    override fun initListener() {
        tb_commission.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                val intent =
                    Intent(this@DispatchCommissionMsgActivity, OrderHistoryActivity::class.java)
                intent.putExtra("orderStatus", 1)
                intent.putExtra("orderType", 2)
                startActivity(intent)
            }
        })
        xmAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.tv_dispatch) {
                dispatchPosition = position
//                val intent = Intent(this, PersonListActivity::class.java)
//                intent.putExtra("personType", "dispatch")
//                startActivity(intent)
            }
        }
        xmAdapter.setOnDeleteDispatchChangeListener {
            countHJ()
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_save -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when (judgeProportionIsInteger()) {
                    1 -> {
                        ToastUtils.showShort("配件提成总比例不能大于1")
                    }
                    2 -> {
                        ToastUtils.showShort("工时费提成总比例不能大于1")
                    }
                    3 -> {
                        saveCommission(2)
                    }
                }
            }
            R.id.tv_sure -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when (judgeProportionIsInteger()) {
                    1 -> {
                        ToastUtils.showShort("配件提成总比例不能大于1")
                    }
                    2 -> {
                        ToastUtils.showShort("工时费提成总比例不能大于1")
                    }
                    3 -> {
                        saveCommission(3)
                    }
                }
            }
        }
    }

    private fun saveCommission(status: Int) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["orderStatus"] = status
        params["id"] = intent.getIntExtra("id", 0)
        params["itemList"] = xmBean
        RetrofitUtils.get()
            .postJson(CommissionUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    when (status) {
                        2 -> {
                            ToastUtils.showShort("保存成功")
                        }
                        3 -> {
                            ToastUtils.showShort("派工成功")
                            finish()
                        }
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getOrderMsg() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getOrderMsg(intent.getIntExtra("id", 0))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(s: String) {
                    val t = GsonUtils.fromJson(s, JieCheResponse::class.java)
                    when (t.code) {
                        200 -> {
                            tv_car_no.text = t.data.cardNo
                            et_car_type.text = t.data.carName
                            tv_pj_hj.text = "配件收入${t.data.itemMoney}元"
                            tv_bd_sr.text = "本单收入${t.data.orderMoney}元"
                            tv_gsf.text = "工时费${t.data.itemServiceFee}元"
                            tv_hj.text = "${t.data.orderMoney}"
                            tv_grossProfitMoney.text = "${t.data.itemMadeFee}"
                            tv_moneyTotalItem.text = "${t.data.itemMadeMoney}"
                            tv_profit.text = "${t.data.netProfitMoney}"
                            if (t.data.itemList != null) {
//                                xmBean.addAll(t.data.itemList)
//                                xmAdapter.setNewData(xmBean)
                            }
                        }
                        401 -> {
                            PopupWindowUtils.get()
                                .showLoginOutTimePop(this@DispatchCommissionMsgActivity)
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

    private fun countHJ() {
        var xmCommissionTotal = 0.0
        var gsCommissionTotal = 0.0
        var costPrice = 0.0
        for (item: JieCheItem in xmBean) {
            costPrice += item.costPrice
            for (bean: ItemDispatchBean in item.itemDispatchList) {
                xmCommissionTotal += bean.madeMoney!!
                gsCommissionTotal += bean.madeFee!!
            }
        }
        tv_moneyTotalItem.text = String.format("%.2f", xmCommissionTotal)
        tv_grossProfitMoney.text = String.format("%.2f", gsCommissionTotal)
        tv_profit.text = String.format(
            "%.2f",
            tv_hj.text.toString().toDouble() - costPrice - xmCommissionTotal - gsCommissionTotal
        )
    }

    private fun judgeProportionIsInteger(): Int {
        for (item: JieCheItem in xmBean) {
            var xmProportion = 0.0
            var gsProportion = 0.0
            for (bean: ItemDispatchBean in item.itemDispatchList) {
                xmProportion += bean.madeRate!!
                gsProportion += bean.madeFeeRate!!
                if (xmProportion > 1)
                    return 1
                if (gsProportion > 1)
                    return 2
            }
        }
        return 3
    }

    @Subscribe
    fun onDispatchCallBack(eb: EBDispatch) {
        for (row: PersonRow in eb.list) {
            xmBean[dispatchPosition].itemDispatchList.add(
                ItemDispatchBean(
                    "", "", "", xmBean[dispatchPosition].id,
                    "", 0.0, 0.1, 0.0, 0.1,
                    intent.getIntExtra("id", -1), "", "", "", row.id, row.name,
                    row.phone, "", "", "", "",""
                )
            )
        }
        xmAdapter.setNewData(xmBean)
    }


    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}