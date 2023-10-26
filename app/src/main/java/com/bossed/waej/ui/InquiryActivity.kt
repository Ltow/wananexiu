package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.InquiryAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBFinish
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.OrderMsgBean
import com.bossed.waej.javebean.Item
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.OnNoRepeatClickListener
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.DoubleClicksUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_inquiry.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.Serializable

class InquiryActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var inquiryAdapter: InquiryAdapter
    private val inquiryBean = ArrayList<Item>()

    override fun getLayoutId(): Int {
        return R.layout.activity_inquiry
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_inquiry)
        tv_car_no.text = intent.getStringExtra("carNo")
        tv_car_type.text = intent.getStringExtra("carType")
        tv_car_vin.text = intent.getStringExtra("carVin")
        rv_inquiry.layoutManager = LinearLayoutManager(this)
        inquiryAdapter = InquiryAdapter(inquiryBean)
        inquiryAdapter.bindToRecyclerView(rv_inquiry)
        inquiryAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        getItemList()
    }

    override fun initListener() {
        tb_inquiry.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        inquiryAdapter.setOnItemClickListener { adapter, view, position ->
            for (bean: Item in inquiryBean) {
                if (bean == inquiryBean[position])
                    bean.isSelect = !bean.isSelect
            }
            adapter.setNewData(inquiryBean)
        }
        cb_select_all.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                for (bean: Item in inquiryBean) {
                    bean.isSelect = true
                }
                inquiryAdapter.setNewData(inquiryBean)
            } else {
                for (bean: Item in inquiryBean) {
                    bean.isSelect = false
                }
                inquiryAdapter.setNewData(inquiryBean)
            }
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_go_inquiry -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val listId = ArrayList<Int>()
                for (item: Item in inquiryBean) {
                    if (item.isSelect) {
                        listId.add(item.id)
                    }
                }
                if (listId.isEmpty()) {
                    ToastUtils.showShort("请勾选至少一项内容")
                    return
                }
                val i = Intent(this, SelectFriendsActivity::class.java)
                i.putExtra("itemIds", listId as Serializable)
                i.putExtra("id", intent.getIntExtra("id", 1))
                startActivity(i)
            }
        }
    }

    private fun getItemList() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getInquiryMsg(intent.getIntExtra("id", 1))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<OrderMsgBean>() {
                override fun onSubscribe(d: Disposable) {
                    LoadingUtils.dismissLoading()
                }

                override fun onNext(t: OrderMsgBean) {
                    when (t.code) {
                        200 -> {
                            if (t.data.itemList != null) {
                                inquiryBean.addAll(t.data.itemList)
                                inquiryAdapter.setNewData(inquiryBean)
                            }
                        }
                        401->{
                            PopupWindowUtils.get().showLoginOutTimePop(this@InquiryActivity)
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

    @Subscribe
    fun onInquiryCallBack(eb: EBFinish) {
        if (eb.finished)
            finish()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}