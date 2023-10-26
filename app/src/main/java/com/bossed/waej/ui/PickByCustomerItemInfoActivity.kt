package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.SupplyPriceAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.javebean.ItemBean
import com.bossed.waej.util.DoubleClicksUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_pricing_msg.*
import java.math.BigDecimal

class PickByCustomerItemInfoActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: SupplyPriceAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_pricing_msg
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_pricing_msg)
        tb_pricing_msg.title = "单项进货领料"
        tv_cancel.visibility = View.GONE
        tv_confirm.text = "返回"
        rv_pricing_msg.layoutManager = LinearLayoutManager(this)
        val item = GsonUtils.fromJson(intent.getStringExtra("item"), ItemBean::class.java)
        tv_item_name.text = item.itemName
        tv_oe.text = item.oem
        tv_num.text = item.num.toString()
        tv_price.text = item.unitPrice.toString()
        tv_cost.text = item.serviceFee.toString()
        tv_money_item.text =
            BigDecimal(item.itemMoney.toString()).add(BigDecimal(item.serviceFee.toString()))
                .toString()
        adapter = SupplyPriceAdapter(item.supplyPriceList)
        adapter.bindToRecyclerView(rv_pricing_msg)
        v_line.visibility = View.VISIBLE
    }

    override fun initListener() {
        tb_pricing_msg.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }
        }
    }
}