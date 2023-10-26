package com.bossed.waej.ui.preview

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.ZYTypeAdapter
import com.bossed.waej.adapter.ZYXMAdapter
import com.bossed.waej.base.BaseFragment
import com.bossed.waej.javebean.GoodsBean
import kotlinx.android.synthetic.main.fragment_zy_goods.*

class ZYXMFragment : BaseFragment() {
    private lateinit var typeAdapter: ZYTypeAdapter
    private val types = ArrayList<GoodsBean>()
    private lateinit var itemAdapter: ZYXMAdapter
    private val items = ArrayList<Int>()

    override fun lazyLoad() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_zy_goods
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        ll_bottom2.visibility = View.VISIBLE
        rv_type.layoutManager = LinearLayoutManager(requireContext())
        types.clear()
        types.add(GoodsBean("轮胎", true))
        types.add(GoodsBean("保养", false))
        types.add(GoodsBean("美容", false))
        types.add(GoodsBean("改装", false))
        typeAdapter = ZYTypeAdapter(types, 1)
        typeAdapter.bindToRecyclerView(rv_type)
        rv_goods.layoutManager = LinearLayoutManager(requireContext())
        items.clear()
        items.add(R.mipmap.icon_xm_pic1)
        items.add(R.mipmap.icon_xm_pic2)
        items.add(R.mipmap.icon_xm_pic1)
        items.add(R.mipmap.icon_xm_pic2)
        itemAdapter = ZYXMAdapter(items, 1)
        itemAdapter.bindToRecyclerView(rv_goods)
    }

    override fun initListener() {
        typeAdapter.setOnItemClickListener { adapter, view, position ->
            types.forEach {
                it.isSelect = false
            }
            types[position].isSelect = true
            adapter.setNewData(types)
            tv_title.text = types[position].name
        }
    }
}