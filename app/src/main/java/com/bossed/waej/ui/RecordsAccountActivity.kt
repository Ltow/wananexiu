package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.RecordsAccountAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.UrlConstants.BankCardListUrl
import com.bossed.waej.javebean.RecordsAccountResponse
import com.bossed.waej.javebean.RecordsAccountRow
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_records_account.*

class RecordsAccountActivity : BaseActivity(), View.OnClickListener {
    private lateinit var adapter: RecordsAccountAdapter
    private val list = ArrayList<RecordsAccountRow>()

    override fun getLayoutId(): Int {
        return R.layout.activity_records_account
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_account)
        rv_account.layoutManager = LinearLayoutManager(this)
        adapter = RecordsAccountAdapter(list)
        adapter.bindToRecyclerView(rv_account)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        tb_account.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        adapter.setOnItemClickListener { adapter, view, position ->
            if (DoubleClicksUtils.get().isFastDoubleClick)
                return@setOnItemClickListener
            val intent = Intent(this, AddBankCardActivity::class.java)
            intent.putExtra("merchantNo", getIntent().getStringExtra("merchantNo"))
            intent.putExtra("id", list[position].id)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_commit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, AddBankCardActivity::class.java)
                intent.putExtra("merchantNo", getIntent().getStringExtra("merchantNo"))
                startActivity(intent)
            }
        }
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["merchantNo"] = intent.getStringExtra("merchantNo")
        params["status"] = "1"
        RetrofitUtils.get()
            .getJson(BankCardListUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, RecordsAccountResponse::class.java)
                    list.clear()
                    list.addAll(t.rows)
                    adapter.setNewData(list)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        getList()
    }
}