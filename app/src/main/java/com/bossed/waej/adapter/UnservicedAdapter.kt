package com.bossed.waej.adapter

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossed.waej.R
import com.bossed.waej.javebean.KmOrderGood
import com.bossed.waej.javebean.OnlineOrderListRow
import com.bossed.waej.util.GlideUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class UnservicedAdapter(mutableList: MutableList<OnlineOrderListRow>) :
    BaseQuickAdapter<OnlineOrderListRow, BaseViewHolder>(
        R.layout.layout_item_my_order,
        mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: OnlineOrderListRow) {
        helper.setText(R.id.tv_orderSn, item.orderSn)
            .setText(
                R.id.tv_status, when (item.shippingStatus) {
                    "1" -> "服务中"
                    else -> ""
                }
            )
            .setTextColor(
                R.id.tv_status, when (item.shippingStatus) {
                    "1" -> Color.RED
                    "2" -> Color.parseColor("#666666")
                    "3" -> Color.parseColor("#3477fc")
                    else -> Color.GRAY
                }
            )
            .setText(R.id.tv_money, "￥${item.orderMoney}")
        val recyclerView = helper.getView<RecyclerView>(R.id.rv_member_detail)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        val beans = ArrayList<KmOrderGood>()
        val memberDetailAdapter = MemberDetailAdapter(beans)
        memberDetailAdapter.bindToRecyclerView(recyclerView)
        memberDetailAdapter.setOnItemClickListener { adapter, view, position ->
            listener!!.invoke(helper.adapterPosition)
        }
        if (TextUtils.isEmpty(item.packageId)) {
            beans.addAll(item.kmOrderGoods!!)
        } else {
            val good = KmOrderGood()
            good.goodsName = item.packageName
            good.goodsNum = "1"
            good.goodsLogo = item.logo
            good.summary = item.summary
            beans.add(good)
        }
        memberDetailAdapter.setNewData(beans)
    }

    private var listener: ((position: Int) -> Unit?)? = null

    fun setOnItemClick(listener: ((position: Int) -> Unit?)) {
        this.listener = listener
    }
}