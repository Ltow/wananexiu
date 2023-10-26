package com.bossed.waej.adapter

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossed.waej.R
import com.bossed.waej.javebean.ItemDispatchBean
import com.bossed.waej.javebean.JieCheItem
import com.bossed.waej.util.PopupWindowUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.lang.StringBuilder

class OrderXMAdapter(data: ArrayList<JieCheItem>, var type: Int) :
    BaseQuickAdapter<JieCheItem, BaseViewHolder>(
        R.layout.layout_item_jieche_xm, data
    ) {

    override fun convert(helper: BaseViewHolder, item: JieCheItem) {
        helper.setText(R.id.tv_peij_name, item.itemName)
            .setText(R.id.tv_amount, "X${item.num}")
            .setText(R.id.tv_oe_item, "OE号：${item.oem}")
            .setText(R.id.tv_bz, "备注：${item.remark}")
            .setText(R.id.tv_price, "￥${item.unitPrice}")
            .setText(R.id.tv_gsf, "￥${item.serviceFee}")
            .setText(R.id.tv_xj, "￥${item.itemMoney}")
            .addOnClickListener(R.id.iv_edit_item)
            .addOnClickListener(R.id.iv_delete_item)
            .addOnClickListener(R.id.tv_dispatch)
        if (type == 0)
            helper.getView<View>(R.id.ll_dispatch).visibility = View.GONE
        else
            helper.getView<View>(R.id.ll_dispatch).visibility = View.VISIBLE
        if (item.itemDispatchList == null || item.itemDispatchList.isEmpty()) {
            helper.getView<TextView>(R.id.tv_dispatch).text = "派工："
            return
        }
        val builder = StringBuilder()
        for (bean: ItemDispatchBean in item.itemDispatchList) {
            if (builder.isNotEmpty())
                builder.append(",")
            builder.append(bean.staffName)
        }
        helper.getView<TextView>(R.id.tv_dispatch).text = "派工：${builder}"
        val recyclerView = helper.getView<RecyclerView>(R.id.rv_ti_cheng)
        if (type == 3) {
            recyclerView.visibility = View.VISIBLE
            helper.getView<View>(R.id.iv_edit_item).visibility = View.INVISIBLE
            helper.getView<View>(R.id.iv_delete_item).visibility = View.INVISIBLE
        } else recyclerView.visibility = View.GONE
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        val tcAdapter =
            DispatchTiChengAdapter(item.itemDispatchList, item.itemMoney, item.grossProfitMoney)
        tcAdapter.bindToRecyclerView(recyclerView)
        tcAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.iv_delete_tc) {
                PopupWindowUtils.get().showConfirmPop(mContext as Activity, "确认删除此派工人员？") {
                    item.itemDispatchList.removeAt(position)
                    adapter.setNewData(item.itemDispatchList)
                    notifyDataSetChanged()
                    onDeleteDispatchChangeListener?.invoke(helper.layoutPosition)
                }
            }
        }
        tcAdapter.setOnTextChangeListener {
            onDeleteDispatchChangeListener?.invoke(helper.layoutPosition)
        }
    }

    private var onDeleteDispatchChangeListener: ((position: Int) -> Unit)? = null

    fun setOnDeleteDispatchChangeListener(onDeleteDispatchChangeListener: ((position: Int) -> Unit)) {
        this.onDeleteDispatchChangeListener = onDeleteDispatchChangeListener
    }
}