package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.MaintainProposalAdapter
import com.bossed.waej.adapter.MaintainTypeAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBSelMaintain
import com.bossed.waej.javebean.*
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_maintain_proposal.*
import org.greenrobot.eventbus.EventBus

class MaintainProposalActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var typeAdapter: MaintainTypeAdapter
    private val typeBeans = ArrayList<MaintainCate>()
    private lateinit var proposalAdapter: MaintainProposalAdapter

    //    private val proposalBeans = ArrayList<MaintainItem>()
    private val proposalBeans = ArrayList<RecommendDs1>()
    private var allBeans = ArrayList<RecommendDs1>()
//    private var jyItems = ArrayList<RecommendDs1>()
//    private val allItem = ArrayList<MaintainItem>()

    override fun getLayoutId(): Int {
        return R.layout.activity_maintain_proposal
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_add_proposal)
        rv_proposal_type.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        typeAdapter = MaintainTypeAdapter(typeBeans)
        typeAdapter.bindToRecyclerView(rv_proposal_type)
        typeAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        rv_proposal_content.layoutManager = LinearLayoutManager(this)
        proposalAdapter = MaintainProposalAdapter(proposalBeans)
        proposalAdapter.bindToRecyclerView(rv_proposal_content)
        proposalAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        typeBeans.add(MaintainCate("", "", 1, "", 1, "需更换", 0, "", 0, 0, "", "", true))
        typeBeans.add(MaintainCate("", "", 0, "", 1, "需检查", 0, "", 0, 0, "", "", false))
        typeAdapter.setNewData(typeBeans)
        allBeans = this.intent.getParcelableArrayListExtra("list")!!
        checkSelectAllState()
        switchType(1)
    }

    override fun initListener() {
        tb_add_proposal.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                val intent = Intent(this@MaintainProposalActivity, BookActivity::class.java)
                intent.putExtra("vin", getIntent().getStringExtra("vinCode"))
                startActivity(intent)
            }
        })
        typeAdapter.setOnItemClickListener { adapter, view, position ->
            for (bean: MaintainCate in typeBeans) {
                bean.isSelect = bean == typeBeans[position]
            }
            adapter.setNewData(typeBeans)
            switchType(typeBeans[position].id)
        }
        proposalAdapter.setOnItemClickListener { adapter, view, position ->
            proposalBeans[position].isSel = !proposalBeans[position].isSel
            adapter.setNewData(proposalBeans)
            for (item: RecommendDs1 in allBeans) {
                if (item.保养项目ID == proposalBeans[position].保养项目ID)
                    item.isSel = proposalBeans[position].isSel
            }
            checkSelectAllState()
        }
        ctv_maintain_all.setOnClickListener(this)
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.ctv_maintain_all -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                ctv_maintain_all.isChecked = !ctv_maintain_all.isChecked
                if (ctv_maintain_all.isChecked)
                    for (bean: RecommendDs1 in proposalBeans)
                        bean.isSel = true
                else
                    for (bean: RecommendDs1 in proposalBeans)
                        bean.isSel = false
                for (all: RecommendDs1 in allBeans)
                    for (bean: RecommendDs1 in proposalBeans)
                        if (all.保养项目名称 == bean.保养项目名称)
                            all.isSel = bean.isSel
                proposalAdapter.setNewData(proposalBeans)
            }
            R.id.tv_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                finish()
            }
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                LoadingUtils.showLoading(this, "加载中...")
                val selItem = ArrayList<RecommendDs1>()
                for (item: RecommendDs1 in allBeans) {
                    if (item.isSel)
                        selItem.add(item)
                }
                if (selItem.isEmpty()) {
                    ToastUtils.showShort("请选择项目")
                    return
                }
                EventBus.getDefault().post(EBSelMaintain(selItem))
                finish()
            }
        }
    }

    /**
     * 切换类别列表
     */
    private fun switchType(position: Int) {
        proposalBeans.clear()
        for (item: RecommendDs1 in allBeans) {
            if (item.更换标识 == position.toString()) {
                proposalBeans.add(item)
            }
        }
        proposalAdapter.setNewData(proposalBeans)
        checkSelectAllState()
    }

    /**
     * 检测全选状态
     */
    private fun checkSelectAllState() {
        var isAll = true
        for (item: RecommendDs1 in proposalBeans) {
            if (!item.isSel) {
                isAll = false
                break
            }
        }
        if (proposalBeans.isEmpty())
            isAll = false
        ctv_maintain_all.isChecked = isAll
    }
}