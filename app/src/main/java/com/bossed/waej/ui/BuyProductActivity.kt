package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.adapter.BuyProductAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBBuyBack
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.http.UrlConstants.SiteUrl
import com.bossed.waej.http.UrlConstants.TermTimeUrl
import com.bossed.waej.javebean.BuyProductData
import com.bossed.waej.javebean.BuyProductResponse
import com.bossed.waej.javebean.TermTimeResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_buy_product.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.math.BigDecimal

class BuyProductActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var buyProductAdapter: BuyProductAdapter
    private val buyList = ArrayList<BuyProductData>()
    private val siteList = ArrayList<BuyProductData>()
    private var isSelect = false
    private var priceDay = ""//站点每天价格
    private var priceYear = ""//站点每年价格
    private var date = ""//统一到期日期

    override fun getLayoutId(): Int {
        return R.layout.activity_buy_product
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).init()
        setMarginTop(tb_buy_product)
        BarUtils.setStatusBarLightMode(window, true)
        rv_buy_product.layoutManager = LinearLayoutManager(this)
        buyProductAdapter = BuyProductAdapter(buyList)
        buyProductAdapter.bindToRecyclerView(rv_buy_product)
        getList()
    }

    override fun initListener() {
        tb_buy_product.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        buyProductAdapter.setOnItemClickListener { adapter, view, position ->
            buyList[position].isSelect = !buyList[position].isSelect
            buyList[position].num = 1
            buyList[position].orderType = 1
            adapter.setNewData(buyList)
            count()
        }
        et_num.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                count()
            }
        })
    }

    private fun count() {
        val num =
            if (!TextUtils.isEmpty(et_num.text.toString()) && isSelect) et_num.text.toString() else "0"
        if (TextUtils.isEmpty(date)) {
            var proTotal = BigDecimal(0.0)
            for (data: BuyProductData in buyList) {
                if (data.isSelect)
                    proTotal += (BigDecimal(data.priceYear).multiply(BigDecimal(1)))
            }
            val siteTotal =
                BigDecimal(priceYear).multiply(BigDecimal(num))
            tv_total.text = siteTotal.add(proTotal).toString()
        } else {
            val surplusMills =
                TimeUtils.string2Millis(date) - TimeUtils.getNowMills()
            var proTotal = BigDecimal(0.0)
            for (data: BuyProductData in buyList) {
                if (data.isSelect)
                    proTotal += (BigDecimal(data.priceDay).multiply(BigDecimal(surplusMills / (1000 * 60 * 60 * 24))))
            }
            val total =
                BigDecimal(priceDay).multiply(BigDecimal(surplusMills / (1000 * 60 * 60 * 24)))
                    .multiply(
                        BigDecimal(num)
                    )
            tv_total.text = total.add(proTotal).toString()
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val sel = ArrayList<BuyProductData>()
                for (data: BuyProductData in buyList) {
                    if (data.isSelect)
                        sel.add(data)
                }
                if (isSelect) {
                    if (TextUtils.isEmpty(et_num.text.toString())) {
                        ToastUtils.showShort("请输入要购买的站点数量")
                        return
                    }
                    siteList[0].num = et_num.text.toString().toInt()
                    siteList[0].orderType = 2
                    sel.add(siteList[0])
                }
                if (sel.isEmpty())
                    return
                val intent = Intent(this, BuySettlementActivity::class.java)
                if (!TextUtils.isEmpty(date)) {
                    intent.putExtra("date", date)
                    intent.putExtra(
                        "surplus",
                        ((TimeUtils.string2Millis(date) - TimeUtils.getNowMills()) / (1000 * 60 * 60 * 24))
                    )
                }
                intent.putExtra("selList", GsonUtils.toJson(sel))
                startActivity(intent)
            }
            R.id.ll_site -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                isSelect = !isSelect
                ctv_sel_zd.isChecked = isSelect
                ll_site.setBackgroundResource(if (isSelect) R.mipmap.icon_product_bg else R.drawable.shape_corners_dp5)
                ll_num.visibility = if (isSelect) View.VISIBLE else View.GONE
                siteList[0].isSelect = isSelect
                count()
            }
            R.id.iv_alert -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showBuySiteAlert(this, iv_alert)
            }
        }
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        RetrofitUtils.get().getJson(
            UrlConstants.ProductListUrl, HashMap(), this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BuyProductResponse::class.java)
                    buyList.clear()
                    buyList.addAll(t.data)
                    buyProductAdapter.setNewData(buyList)
                    getSite()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getSite() {
        RetrofitUtils.get()
            .getJson(SiteUrl, HashMap(), this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BuyProductResponse::class.java)
                    siteList.addAll(t.data)
                    tv_name_site.text = t.data[0].packageName
                    tv_priceYear_site.text = "${t.data[0].priceYear}元/年"
                    tv_priceDay_site.text = "${t.data[0].priceDay}元/天"
                    tv_remark_site.text = t.data[0].remark
                    priceDay = t.data[0].priceDay
                    priceYear = t.data[0].priceYear
                    getTermTime()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getTermTime() {
        RetrofitUtils.get()
            .getJson(TermTimeUrl, HashMap(), this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, TermTimeResponse::class.java)
                    date = t.data
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMsgEvent(ev: EBBuyBack) {
        if (ev.isFinish)
            finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}