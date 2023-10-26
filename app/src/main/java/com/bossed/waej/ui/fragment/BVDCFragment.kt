package com.bossed.waej.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.adapter.BvdcAdapter
import com.bossed.waej.base.BaseFragment
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.BvdcUserIdResponse
import com.bossed.waej.ui.*
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_bvdc.*
import java.util.HashMap

class BVDCFragment : BaseFragment() {
    private lateinit var bvdcAdapter: BvdcAdapter
    private val arrayList = ArrayList<Int>()

    override fun lazyLoad() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_bvdc
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        setTopMargin(tb_bvdc)
        val layoutParams = iv_bvdc_top.layoutParams as RelativeLayout.LayoutParams
        layoutParams.height = layoutParams.height + BarUtils.getStatusBarHeight()
        iv_bvdc_top.layoutParams = layoutParams
        arrayList.clear()
        arrayList.add(R.mipmap.icon_bvdc_oe_search)
        arrayList.add(R.mipmap.icon_bvdc_vin)
        arrayList.add(R.mipmap.icon_bvdc_item)
        arrayList.add(R.mipmap.icon_bvdc_book)
        arrayList.add(R.mipmap.icon_applicable)
//        arrayList.add(R.mipmap.icon_bvdc_oe)
//        arrayList.add(R.mipmap.icon_bvdc_fixed)
        arrayList.add(R.mipmap.icon_bvdc_part)
        arrayList.add(R.mipmap.icon_bvdc_replace)
        arrayList.add(R.mipmap.icon_bvdc_total)
        arrayList.add(R.mipmap.icon_search_balance)
        rv_bvdc.layoutManager = GridLayoutManager(activity, 4)
        bvdcAdapter = BvdcAdapter(arrayList)
        bvdcAdapter.bindToRecyclerView(rv_bvdc)
    }

    override fun initListener() {
        bvdcAdapter.setOnItemClickListener { adapter, view, position ->
            when (position) {
                0 -> {
                    val intent = Intent(activity, OESearchActivity::class.java)
                    intent.putExtra("oe", "")
                    startActivity(intent)
                }
                1 -> {
                    startActivity(Intent(activity, VINSearchActivity::class.java))
                }
                2 -> {
                    val intent = Intent(activity, RecommendActivity::class.java)
                    intent.putExtra("vin", "")
                    startActivity(intent)
                }
                3 -> {
                    val intent = Intent(activity, BookActivity::class.java)
                    intent.putExtra("vin", "")
                    startActivity(intent)
                }
                4 -> {
                    val intent = Intent(activity, ApplicableModelActivity::class.java)
                    intent.putExtra("oe", "")
                    intent.putExtra("brandId", "")
                    intent.putExtra("brandName", "")
                    startActivity(intent)
                }
                5 -> {
                    val intent = Intent(activity, PartSearchActivity::class.java)
                    intent.putExtra("vin", "")
                    startActivity(intent)
                }
                6 -> {
                    val intent = Intent(activity, ReplaceActivity::class.java)
                    intent.putExtra("oe", "")
                    intent.putExtra("brandId", "")
                    intent.putExtra("brandName", "")
                    startActivity(intent)
                }
                7 -> {
                    startActivity(Intent(activity, UseTypeCountActivity::class.java))
                }
                8 -> {
                    startActivity(Intent(activity, BalanceActivity::class.java))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        BarUtils.setStatusBarLightMode(requireActivity(), false)
    }
}