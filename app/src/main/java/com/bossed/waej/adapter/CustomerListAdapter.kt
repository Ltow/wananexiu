package com.bossed.waej.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossed.waej.R
import com.bossed.waej.javebean.Car
import com.bossed.waej.javebean.CustomerListRow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CustomerListAdapter(data: MutableList<CustomerListRow>) :
    BaseQuickAdapter<CustomerListRow, BaseViewHolder>(R.layout.layout_item_customer_list, data) {
    private val carList = ArrayList<Car>()
    private var isShow = true

    override fun convert(helper: BaseViewHolder, item: CustomerListRow) {
        helper.setText(R.id.tv_name, item.customerName + " " + item.customerPhone)
            .addOnClickListener(R.id.iv_delete_item)
//        val flayout = helper.getView<FlexboxLayout>(R.id.fl_customer)
//        val layout = LayoutInflater.from(mContext)
//            .inflate(R.layout.layout_item_tags_textview, null)
//        val textView = layout.findViewById<TextView>(R.id.tv_tag)
//        textView.text = "标签"
//        flayout.addView(layout)
        val recyclerView = helper.getView<RecyclerView>(R.id.rv_cus_cars)
        val carListAdapter = CarListAdapter(item.carList as ArrayList)
        val textView = helper.getView<TextView>(R.id.tv_arrow)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        carListAdapter.bindToRecyclerView(recyclerView)
        val imageView = helper.getView<ImageView>(R.id.iv_close)
        if (item.carList.size > 1)
            helper.getView<View>(R.id.ll_bottom).visibility = View.VISIBLE
        else
            helper.getView<View>(R.id.ll_bottom).visibility = View.GONE
        helper.getView<View>(R.id.ll_bottom).setOnClickListener {
            carList.clear()
            if (isShow) {
                isShow = false
                carList.add(item.carList[0])
                imageView.setImageResource(R.mipmap.icon_close_down)
                textView.text = "更多车辆"
            } else {
                isShow = true
                carList.addAll(item.carList)
                imageView.setImageResource(R.mipmap.icon_open_up)
                textView.text = "收起"
            }
            carListAdapter.setNewData(carList)
        }
    }
}