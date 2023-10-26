package com.bossed.waej.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossed.waej.R
import com.bossed.waej.javebean.CarModelData
import com.bossed.waej.util.OnNoRepeatItemClickListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class CarSeriesAdapter(data: ArrayList<CarModelData>) :
    BaseQuickAdapter<CarModelData, BaseViewHolder>(R.layout.layout_item_car_series, data) {

    override fun convert(helper: BaseViewHolder, item: CarModelData) {
        helper.setText(R.id.tv_car_series, item.name)
        val recyclerView = helper.getView<RecyclerView>(R.id.rv_item_series)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        val seriesMsgAdapter = CarSeriesMsgAdapter(item.data)
        seriesMsgAdapter.bindToRecyclerView(recyclerView)
        seriesMsgAdapter.onItemClickListener = object : OnNoRepeatItemClickListener {
            override fun onNoRepeatItemItemClick(
                adapter: BaseQuickAdapter<*, *>?,
                view: View?,
                position: Int
            ) {
                onClickItemListener?.invoke(position, helper.layoutPosition)
            }
        }
    }

    private var onClickItemListener: ((position: Int, parentPosition: Int) -> Unit)? = null

    fun setOnClickItemListener(onClickItemListener: ((position: Int, parentPosition: Int) -> Unit)) {
        this.onClickItemListener = onClickItemListener
    }
}