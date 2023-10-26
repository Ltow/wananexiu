package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.CollectionAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.CollectionFinishBean
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.CollectionResponse
import com.bossed.waej.javebean.CollectionRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.TextChangedListener
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_collection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.math.BigDecimal

class CollectionActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: CollectionAdapter
    private val list = ArrayList<CollectionRow>()
    private var isCheck = true
    private var customerId = -1
    private var searchContent = ""
    private var customerName = ""
    private var cardNo = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_collection
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_collection)
        rv_collection.layoutManager = LinearLayoutManager(this)
        adapter = CollectionAdapter(list)
        adapter.bindToRecyclerView(rv_collection)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        tb_collection.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                startActivity(Intent(this@CollectionActivity, CollectionSearchActivity::class.java))
            }
        })
        et_search.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (et_search.isFocused) {
                    if (isCheck) {
                        if (searchContent != s) {
                            tv_lxr.text = ""
                            tv_phone.text = ""
                            customerId = -1
                        }
                        PopupWindowUtils.get().checkKeHuList(et_search, this@CollectionActivity) {
                            isCheck = false
                            searchContent = it.customerName
                            et_search.setText(it.customerName)
                            et_search.setSelection(it.customerName.length)
                            this@CollectionActivity.customerName = it.customerName
                            this@CollectionActivity.cardNo = it.cardNo
                            tv_lxr.text = it.customerName
                            tv_phone.text = it.customerPhone
                            customerId = it.customerId
                            getOrderList(it.customerId)
                        }
                    } else isCheck = true
                }
            }
        })
        et_search.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                if (isCheck) {
                    if (searchContent != et_search.text.toString()) {
                        tv_lxr.text = ""
                        tv_phone.text = ""
                        customerId = -1
                    }
                    PopupWindowUtils.get().checkKeHuList(et_search, this@CollectionActivity) {
                        isCheck = false
                        searchContent = it.customerName
                        et_search.setText(it.customerName)
                        et_search.setSelection(it.customerName.length)
                        this@CollectionActivity.customerName = it.customerName
                        this@CollectionActivity.cardNo = it.cardNo
                        tv_lxr.text = it.customerName
                        tv_phone.text = it.customerPhone
                        customerId = it.customerId
                        getOrderList(it.customerId)
                    }
                } else isCheck = true
        }
        adapter.setOnItemClickListener { adapter, view, position ->
            list[position].isSelected = !list[position].isSelected
            adapter.setNewData(list)
            count()
        }
    }

    private fun count() {
        var total = BigDecimal(0.0)
        for (row: CollectionRow in list) {
            if (row.isSelected)
                total += BigDecimal(row.moneyOwe)
        }
        tv_total_money.text = "￥${total.setScale(2, BigDecimal.ROUND_HALF_DOWN)}"
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                var total = BigDecimal(0.0)
                val selOrders = ArrayList<Int>()
                for (row: CollectionRow in list) {
                    if (row.isSelected) {
                        selOrders.add(row.id.toInt())
                        total += BigDecimal(row.moneyOwe)
                    }
                }
                val intent = Intent(this, CollectionSettleActivity::class.java)
                intent.putIntegerArrayListExtra("sel", selOrders)
                intent.putExtra("customerId", customerId)
                intent.putExtra("money", if (selOrders.isEmpty()) 0.00 else total.toDouble())
                intent.putExtra("moneyOwe", tv_money.text.toString())
                intent.putExtra("customerName", customerName)
                intent.putExtra("cardNo", cardNo)
                startActivity(intent)
            }
        }
    }

    private fun getOrderList(id: Int) {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getReceivableOrder(id)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    val t = GsonUtils.fromJson(s, CollectionResponse::class.java)
                    tv_money.text = "￥${t.data.moneyOwe}"
                    list.clear()
                    list.addAll(t.data.list)
                    adapter.setNewData(list)
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMassageEvent(eb: CollectionFinishBean) {
        if (eb.isFinish)
            onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        PopupWindowUtils.get().dismiss()
    }
}