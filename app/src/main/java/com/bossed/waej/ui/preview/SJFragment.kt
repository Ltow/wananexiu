package com.bossed.waej.ui.preview

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.PreviewShopPicAdapter
import com.bossed.waej.adapter.WorkerAdapter
import com.bossed.waej.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_shang_jia.*

class SJFragment : BaseFragment() {
    private lateinit var picAdapter: PreviewShopPicAdapter
    private val pics = ArrayList<Int>()
    private lateinit var workerAdapter: WorkerAdapter
    private val workers = ArrayList<String>()

    override fun lazyLoad() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_shang_jia
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        rv_pic.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        pics.clear()
        pics.add(R.mipmap.icon_preview_pic1)
        pics.add(R.mipmap.icon_preview_pic2)
        pics.add(R.mipmap.icon_preview_pic3)
        picAdapter = PreviewShopPicAdapter(pics)
        picAdapter.bindToRecyclerView(rv_pic)
        rv_worker.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        workers.clear()
        workers.add("张三")
        workers.add("李四")
        workers.add("王五")
        workerAdapter = WorkerAdapter(workers, 1)
        workerAdapter.bindToRecyclerView(rv_worker)
    }
}