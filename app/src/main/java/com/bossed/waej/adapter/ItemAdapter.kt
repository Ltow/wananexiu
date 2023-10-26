package com.bossed.waej.adapter

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bossed.waej.R
import com.bossed.waej.javebean.ItemBean
import com.bossed.waej.javebean.ItemDispatchBean
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.States
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class ItemAdapter(states: Int, data: MutableList<ItemBean>) :
    BaseQuickAdapter<ItemBean, BaseViewHolder>(R.layout.layout_item_base_item, data) {
    private var states = 0

    init {
        this.states = states
    }

    override fun convert(helper: BaseViewHolder, item: ItemBean) {
//        val lingLiao = StringBuilder()
        val paiGong = StringBuilder()
        if (item.itemDispatchList != null && item.itemDispatchList.isNotEmpty())
            for (row: ItemDispatchBean in item.itemDispatchList) {
                when (row.type) {
//                    "2" -> {
//                        if (lingLiao.isNotEmpty())
//                            lingLiao.append(",")
//                        lingLiao.append(row.staffName)
//                    }
                    "3" -> {
                        if (paiGong.isNotEmpty())
                            paiGong.append(",")
                        paiGong.append(row.staffName)
                    }
                }
            }
        helper.setText(R.id.tv_name, item.itemName)
            .setText(R.id.tv_attrName, item.oem)
            .setText(R.id.tv_price, "￥${item.unitPrice}")
            .setText(R.id.tv_num, item.num.toString())
            .setText(
                R.id.tv_money,
                "￥${
                    BigDecimal(item.unitPrice).multiply(BigDecimal(item.num))
                        .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                }"
            )
            .setText(R.id.tv_cost, "￥${item.serviceFee}")
            .setText(R.id.tv_remark, "￥${item.shop4sPrice}")
            .setText(R.id.tv_4s_cost, "￥${item.shop4sServiceFee}")
            .setText(R.id.tv_num_4s, item.shop4sNum)
            .setText(R.id.tv_work_person, paiGong.toString())
            .setText(
                R.id.tv_total,BigDecimal(item.unitPrice).multiply(BigDecimal(item.num)).add(
                    BigDecimal(item.serviceFee)).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()
            )
            .addOnClickListener(R.id.iv_delete_item)
            .addOnClickListener(R.id.iv_edit_item)
            .addOnClickListener(R.id.iv_show_4s_price)
            .addOnClickListener(R.id.iv_show_oe)
            .addOnClickListener(R.id.tv_work_person)
            .addOnClickListener(R.id.iv_item_tag)
        val show4S = helper.getView<ImageView>(R.id.iv_show_4s_price)
        val showOE = helper.getView<ImageView>(R.id.iv_show_oe)
        when (states) {
            States.HISTORY -> {
                helper.getView<View>(R.id.iv_edit_item).visibility = View.GONE
                helper.getView<View>(R.id.iv_delete_item).visibility = View.GONE
                show4S.visibility = View.GONE
                showOE.visibility = View.GONE
            }
            States.RECEIVE -> {
                helper.getView<View>(R.id.iv_edit_item).visibility = View.VISIBLE
                helper.getView<View>(R.id.iv_delete_item).visibility = View.VISIBLE
                show4S.visibility =
                    if (item.type != 3) View.GONE else View.VISIBLE
                showOE.visibility =
                    if (item.type != 3) View.VISIBLE else View.GONE
                helper.getView<View>(R.id.ll_sel_person).visibility = View.GONE
            }
            States.SERVICE -> {
                helper.getView<View>(R.id.iv_edit_item).visibility = View.VISIBLE
                helper.getView<View>(R.id.iv_delete_item).visibility = View.VISIBLE
                show4S.visibility =
                    if (item.type != 3) View.GONE else View.VISIBLE
                showOE.visibility =
                    if (item.type != 3) View.VISIBLE else View.GONE
                helper.getView<View>(R.id.ll_sel_person).visibility = View.VISIBLE
            }
        }
        if (item.isShowOE) {
            helper.getView<View>(R.id.tv_remark).visibility = View.VISIBLE
            showOE.setImageResource(R.mipmap.icon_looked)
            showOE.isEnabled = false
        }
        if (item.isShow4s || !TextUtils.isEmpty(item.shop4sPrice) || !TextUtils.isEmpty(item.shop4sServiceFee)) {
            helper.getView<View>(R.id.tv_remark).visibility = View.VISIBLE
            helper.getView<View>(R.id.tv_4s_cost).visibility = View.VISIBLE
            helper.getView<View>(R.id.tv_num_4s).visibility = View.VISIBLE
            show4S.setImageResource(R.mipmap.icon_looked)
            show4S.isEnabled = false
        } else {
            helper.getView<View>(R.id.tv_remark).visibility = View.GONE
            helper.getView<View>(R.id.tv_4s_cost).visibility = View.GONE
            helper.getView<View>(R.id.tv_num_4s).visibility = View.GONE
        }
        when (item.type) {
            1 -> {
                helper.getView<TextView>(R.id.tv_type).text = "项目"
                helper.getView<View>(R.id.ll_attr).visibility = View.GONE
            }
            2 -> {
                helper.getView<TextView>(R.id.tv_type).text = "配件"
                helper.getView<View>(R.id.ll_attr).visibility = View.VISIBLE
            }
        }
        val itemTag = helper.getView<ImageView>(R.id.iv_item_tag)
        when (item.type) {
            3 -> itemTag.setImageResource(R.mipmap.icon_ordinary)
            4 -> itemTag.setImageResource(R.mipmap.icon_depth)
            0 -> itemTag.setImageResource(R.drawable.shape_corners_ffffff_dp3)
        }
    }
}