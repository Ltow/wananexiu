package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.MemberDetailAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.javebean.MemberDetailBean
import com.bossed.waej.javebean.MemberDetailItemBean
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_member_detail.*

class MemberDetailActivity : BaseActivity() {
    private lateinit var memberDetailAdapter: MemberDetailAdapter
    private val beans = ArrayList<MemberDetailBean>()
    private val itemBeans = ArrayList<MemberDetailItemBean>()

    override fun getLayoutId(): Int {
        return R.layout.activity_member_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_member_detail)
        rv_member_detail.layoutManager = LinearLayoutManager(this)
        itemBeans.add(MemberDetailItemBean("项目"))
        itemBeans.add(MemberDetailItemBean("项目"))
        itemBeans.add(MemberDetailItemBean("项目"))
        itemBeans.add(MemberDetailItemBean("项目"))
        beans.add(MemberDetailBean("会员卡", itemBeans))
        beans.add(MemberDetailBean("会员卡", itemBeans))
        beans.add(MemberDetailBean("会员卡", itemBeans))
//        memberDetailAdapter = MemberDetailAdapter(beans)
        memberDetailAdapter.bindToRecyclerView(rv_member_detail)
        memberDetailAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        tb_member_detail.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        memberDetailAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
//                R.id.rl_history -> {
//                    val intent = Intent(this, CardHistoryActivity::class.java)
//                    startActivity(intent)
//                }
            }
        }
    }
}