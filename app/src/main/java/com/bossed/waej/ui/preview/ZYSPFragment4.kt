package com.bossed.waej.ui.preview

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.ZYSPAdapter
import com.bossed.waej.adapter.ZYTypeAdapter
import com.bossed.waej.base.BaseFragment
import com.bossed.waej.javebean.GoodsBean
import kotlinx.android.synthetic.main.fragment_zy_goods.*

class ZYSPFragment4 : BaseFragment() {
    private lateinit var typeAdapter: ZYTypeAdapter
    private val types = ArrayList<GoodsBean>()
    private lateinit var goodsAdapter: ZYSPAdapter
    private val goods = ArrayList<Int>()

    override fun lazyLoad() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_zy_goods
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        rl_bottom.visibility = View.VISIBLE
        rv_type.layoutManager = LinearLayoutManager(requireContext())
        types.clear()
        types.add(GoodsBean("轮胎", true))
        types.add(GoodsBean("保养", false))
        types.add(GoodsBean("美容", false))
        types.add(GoodsBean("改装", false))
        typeAdapter = ZYTypeAdapter(types, 4)
        typeAdapter.bindToRecyclerView(rv_type)
        rv_goods.layoutManager = LinearLayoutManager(requireContext())
        goods.clear()
        goods.add(R.drawable.icon_goods_pic1)
        goods.add(R.drawable.icon_goods_pic2)
        goods.add(R.drawable.icon_goods_pic1)
        goods.add(R.drawable.icon_goods_pic2)
        goodsAdapter = ZYSPAdapter(goods, 2)
        goodsAdapter.bindToRecyclerView(rv_goods)
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