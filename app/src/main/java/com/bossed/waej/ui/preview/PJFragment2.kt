package com.bossed.waej.ui.preview

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.EvaluateAdapter
import com.bossed.waej.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_ping_jia4.*

class PJFragment2 : BaseFragment() {
    private lateinit var adapter: EvaluateAdapter
    private val list = ArrayList<String>()

    override fun lazyLoad() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_ping_jia4
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        tv_pj1.visibility = View.VISIBLE
        rv_pingjia4.layoutManager = LinearLayoutManager(requireContext())
        list.clear()
        list.add("游客1")
        list.add("游客2")
        list.add("游客3")
        adapter = EvaluateAdapter(list, 2)
        adapter.bindToRecyclerView(rv_pingjia4)
    }
}