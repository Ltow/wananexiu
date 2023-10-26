package com.bossed.waej.ui.preview

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.EvaluateAdapter
import com.bossed.waej.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_ping_jia.*

class PJFragment : BaseFragment() {
    private lateinit var adapter: EvaluateAdapter
    private val list = ArrayList<String>()

    override fun lazyLoad() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_ping_jia
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        rv_pj.layoutManager = LinearLayoutManager(requireContext())
        list.clear()
        list.add("游客1")
        list.add("游客2")
        list.add("游客3")
        adapter = EvaluateAdapter(list, 1)
        adapter.bindToRecyclerView(rv_pj)
    }
}