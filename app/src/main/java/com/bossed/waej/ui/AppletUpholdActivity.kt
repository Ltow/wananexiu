package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.AppletUpholdAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.javebean.AppletUpholdBean
import com.bossed.waej.util.OnNoRepeatItemClickListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_applet_uphold.*

class AppletUpholdActivity : BaseActivity() {
    private lateinit var appletUpholdAdapter: AppletUpholdAdapter
    private val appletUpholdBean = ArrayList<AppletUpholdBean>()

    override fun getLayoutId(): Int {
        return R.layout.activity_applet_uphold
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_applet)
        rv_applet.layoutManager = LinearLayoutManager(this)
        appletUpholdAdapter = AppletUpholdAdapter(appletUpholdBean)
        appletUpholdAdapter.bindToRecyclerView(rv_applet)
        appletUpholdBean.clear()
        appletUpholdBean.add(AppletUpholdBean(R.mipmap.icon682, "门店基本信息"))
        appletUpholdBean.add(AppletUpholdBean(R.mipmap.icon692, "服务项目上下架"))
        appletUpholdBean.add(AppletUpholdBean(R.mipmap.icon631, "会员卡上下架"))
        appletUpholdBean.add(AppletUpholdBean(R.mipmap.icon632, "小程序二维码"))
        appletUpholdAdapter.setNewData(appletUpholdBean)
    }

    override fun initListener() {
        tb_applet.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        appletUpholdAdapter.onItemClickListener = object : OnNoRepeatItemClickListener {
            override fun onNoRepeatItemItemClick(
                adapter: BaseQuickAdapter<*, *>?,
                view: View?,
                position: Int
            ) {
                when (appletUpholdBean[position].name) {
                    "门店基本信息" -> {
                        startActivity(
                            Intent(
                                this@AppletUpholdActivity,
                                ShopMsgActivity::class.java
                            )
                        )
                    }
                    "服务项目上下架" -> {
                        startActivity(
                            Intent(
                                this@AppletUpholdActivity,
                                ItemUpholdActivity::class.java
                            )
                        )
                    }
                    "会员卡上下架" -> {
                        val intent =
                            Intent(this@AppletUpholdActivity, MemberManageActivity::class.java)
                        intent.putExtra("type", "applet")
                        startActivity(intent)
                    }
                    "小程序二维码" -> {
                    }
                }
            }
        }
    }
}