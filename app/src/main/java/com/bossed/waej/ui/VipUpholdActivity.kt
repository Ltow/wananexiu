package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.VipManageAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.javebean.VipCardRow
import com.bossed.waej.util.OnNoRepeatItemClickListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_vip_uphold.*

class VipUpholdActivity : BaseActivity() {
    private lateinit var vipManageAdapter: VipManageAdapter
    private val vipManageBean = ArrayList<VipCardRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_vip_uphold
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_applet_vip)
        rv_applet_vip.layoutManager = LinearLayoutManager(this)
        vipManageAdapter = VipManageAdapter(1, vipManageBean)
        vipManageAdapter.bindToRecyclerView(rv_applet_vip)
        vipManageAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        tb_applet_vip.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                startActivity(Intent(this@VipUpholdActivity, VipOffActivity::class.java))
            }
        })
        vipManageAdapter.onItemClickListener = object : OnNoRepeatItemClickListener {
            override fun onNoRepeatItemItemClick(
                adapter: BaseQuickAdapter<*, *>?,
                view: View?,
                position: Int
            ) {
                when (view?.id) {
                    R.id.tv_off -> {
                    }
                }
            }
        }
    }
}