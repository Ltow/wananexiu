package com.bossed.waej.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossed.waej.R
import com.bossed.waej.javebean.BankAccountRow
import com.bossed.waej.javebean.SelAccountBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SelAccountAdapter(data: MutableList<SelAccountBean>) :
    BaseQuickAdapter<SelAccountBean, BaseViewHolder>(R.layout.layout_item_sel_account, data) {
    override fun convert(helper: BaseViewHolder, item: SelAccountBean) {
        helper.setText(R.id.tv_type, item.methodName)
//            .setText(R.id.tv_name, item.name)
        val recyclerView = helper.getView<RecyclerView>(R.id.rv_account)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        val adapter = SelAccountAdapter2(item.rows as MutableList<BankAccountRow>)
        adapter.bindToRecyclerView(recyclerView)
        adapter.setOnItemClickListener { adapter, view, position ->
            listener!!.invoke(item.rows[position])
        }
    }

    private var listener: ((BankAccountRow) -> Unit)? = null
    fun setOnItemClickListener(listener: ((BankAccountRow) -> Unit)) {
        this.listener = listener
    }
}