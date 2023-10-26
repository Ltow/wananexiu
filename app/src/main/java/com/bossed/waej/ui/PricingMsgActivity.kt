package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.PricingMsgAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.javebean.EBAddPricing
import com.bossed.waej.javebean.ItemBean
import com.bossed.waej.javebean.PartListRow
import com.bossed.waej.javebean.SupplyPriceBean
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.PopupWindowUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_pricing_msg.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.math.BigDecimal

class PricingMsgActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: PricingMsgAdapter
    private val list = ArrayList<SupplyPriceBean>()
    private var isCheck = true
    private var position = -1

    override fun getLayoutId(): Int {
        return R.layout.activity_pricing_msg
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_pricing_msg)
        val footView = LayoutInflater.from(this).inflate(R.layout.layout_footer_view_item, null)
        footView.findViewById<TextView>(R.id.tv_add_item).setOnClickListener(this)
        footView.findViewById<TextView>(R.id.tv_add_item).text = "添加供应商"
        rv_pricing_msg.layoutManager = LinearLayoutManager(this)
        adapter = PricingMsgAdapter(list)
        adapter.bindToRecyclerView(rv_pricing_msg)
        adapter.setFooterView(footView)
        LogUtils.d("tag", intent.getStringExtra("item"))
        val item = GsonUtils.fromJson(intent.getStringExtra("item"), ItemBean::class.java)
        tv_item_name.text = item.itemName
        tv_oe.text = item.oem
        tv_num.text = item.num.toString()
        tv_price.text = item.unitPrice.toString()
        tv_cost.text = item.serviceFee.toString()
        tv_money_item.text =
            BigDecimal(item.itemMoney.toString()).add(BigDecimal(item.serviceFee.toString()))
                .toString()
        list.addAll(item.supplyPriceList)
        adapter.setNewData(list)
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
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_delete -> {
                    PopupWindowUtils.get().showConfirmPop(this, "确定删除此供应商内容？") {
                        list.removeAt(position)
                        adapter.setNewData(list)
                    }
                }

                R.id.iv_select -> {
                    this.position = position
                    startActivity(Intent(mContext, SelectPartActivity3::class.java))
                }
            }
        }
        adapter.setOnChangeListener { editText, i ->
            if (isCheck && editText.isFocused) {
                PopupWindowUtils.get()
                    .checkGongYingShangPop(editText, mActivity) {
                        editText.clearFocus()
                        isCheck = false
                        list[i].name = it.name
                        if (it.name == "公司库存") {
                            list[i].supplierId = null
                            this.position = i
                            startActivity(Intent(mContext, SelectPartActivity3::class.java))
                        } else
                            list[i].supplierId = it.id.toInt()
                        adapter.setNewData(list)
                        KeyboardUtils.hideSoftInput(window)
                    }
            } else
                isCheck = true
        }
        adapter.setOnChangeListener2 { string, i ->
            if (isCheck)
                list[i].name = string
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_add_item -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val bean = SupplyPriceBean()
                bean.partName = tv_item_name.text.toString()
                list.add(bean)
                adapter.setNewData(list)
            }

            R.id.tv_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }

            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                for (i: Int in list.indices) {
                    if (TextUtils.isEmpty(list[i].partName)) {
                        ToastUtils.showShort("请输入 第${i + 1}条 配件名称")
                        return
                    }
                    if (list[i].num == null) {
                        ToastUtils.showShort("请输入 第${i + 1}条 配件数量")
                        return
                    }
                    if (list[i].unitPrice == null) {
                        ToastUtils.showShort("请输入 第${i + 1}条 进货单价")
                        return
                    }
                }
                EventBus.getDefault().post(EBAddPricing(list))
                finish()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(row: PartListRow) {
        list[position].partName = row.name!!
        list[position].num = 1.00
        list[position].unitPrice = row.purchasePrice!!.toDouble()
        list[position].supplyPrice = 0.00
        list[position].partId = row.id!!.toInt()
        adapter.setNewData(list)
    }

    override fun onDestroy() {
        super.onDestroy()
        PopupWindowUtils.get().dismiss()
        EventBus.getDefault().unregister(this)
    }
}