package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.ExchangeAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBExchange
import com.bossed.waej.javebean.ExchangeData
import com.bossed.waej.javebean.ExchangeResponse
import com.bossed.waej.util.DoubleClicksUtils
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_exchange_success.*
import org.greenrobot.eventbus.EventBus

class ExchangeSuccessActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: ExchangeAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_exchange_success
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        val t = GsonUtils.fromJson(intent.getStringExtra("succ"), ExchangeResponse::class.java)
        rv_exchange.layoutManager = LinearLayoutManager(this)
        adapter = ExchangeAdapter(t.data as ArrayList)
        adapter.bindToRecyclerView(rv_exchange)
        adapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_commit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this, MyProductActivity::class.java))
                finish()
            }
            R.id.btn_cancel -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                EventBus.getDefault().post(EBExchange(true))
                finish()
            }
        }
    }
}