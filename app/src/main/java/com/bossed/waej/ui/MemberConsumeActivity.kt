package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.MemberConsumeAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.javebean.MemberConsumeBean
import com.bossed.waej.util.OnNoRepeatClickListener
import com.bossed.waej.util.DoubleClicksUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_member_consume.*

import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class MemberConsumeActivity : BaseActivity(), OnRefreshLoadMoreListener, OnNoRepeatClickListener {
    private lateinit var memberConsumeAdapter: MemberConsumeAdapter
    private val beans = ArrayList<MemberConsumeBean>()

    override fun getLayoutId(): Int {
        return R.layout.activity_member_consume
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_consume)
        rv_member_consume.layoutManager = LinearLayoutManager(this)
        beans.add(MemberConsumeBean("冀AL33G9"))
        beans.add(MemberConsumeBean("冀AL33G9"))
        beans.add(MemberConsumeBean("冀AL33G9"))
        memberConsumeAdapter = MemberConsumeAdapter(beans)
        memberConsumeAdapter.bindToRecyclerView(rv_member_consume)
    }

    override fun initListener() {
        srl_member_consume.setOnRefreshLoadMoreListener(this)
        tb_consume.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        refreshLayout.finishLoadMore()
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_member_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick) {
                    return
                }
                val intent = Intent(this, MemberListActivity::class.java)
                startActivity(intent)
            }
            R.id.tv_stored -> {
                if (DoubleClicksUtils.get().isFastDoubleClick) {
                    return
                }
                val intent = Intent(this, StoredActivity::class.java)
                startActivity(intent)
            }
            R.id.tv_scan -> {
                if (DoubleClicksUtils.get().isFastDoubleClick) {
                    return
                }
                //配置扫描时的基本参数
                val options = ScanOptions()
                options.apply {
                    setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)//图形码的格式：商品码、一维码、二维码、数据矩阵、全部类型
                    setPrompt("请将条形码置于取景框内扫描")
                    setCameraId(0) //0 后置摄像头  1 前置摄像头
                    setBeepEnabled(true)
                    setOrientationLocked(false)
                    captureActivity = ContinuousCaptureActivity::class.java
//                    setTimeout(5000)//设置超时时间
                    setBarcodeImageEnabled(false)//是否保存图片，扫描成功会截取扫描框的图形保存到手机并在result中返回路径->result.barcodeImagePath
                }
                //启动扫描二维码界面
                barcodeLauncher.launch(options)
            }
        }
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        //获取回调结果
        if (result.contents != null) {
//            "扫描结果: " + result.contents + ",${result.barcodeImagePath}"
//            val intent = Intent(this, ScanDetailActivity::class.java)
//            startActivity(intent)
//            if(result.barcodeImagePath.isNotEmpty()){
//                ivImage.setImageURI(Uri.parse(result.barcodeImagePath))
//            }
            ToastUtils.showShort("扫描结果：${result.contents}")
        }
    }
}