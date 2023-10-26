package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.DepartListAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.DepartListData
import com.bossed.waej.javebean.DepartListResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_dispatch.*

class PersonListActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var departAdapter: DepartListAdapter
    private val departList = ArrayList<DepartListData>()

    override fun getLayoutId(): Int {
        return R.layout.activity_dispatch
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_dispatch)
        rv_depart.layoutManager = LinearLayoutManager(this)
        departAdapter = DepartListAdapter(departList)
        departAdapter.bindToRecyclerView(rv_depart)
        ll_bottom.visibility =
            if (intent.getStringExtra("type") == "register") View.VISIBLE else View.GONE
        tb_dispatch.title = if (intent.getStringExtra("type") == "register") "请完善企员工信息" else "员工列表"
    }

    override fun initListener() {
        tb_dispatch.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                val intent = Intent(this@PersonListActivity, PersonDataActivity::class.java)
                intent.putExtra("personType", "new")
                startActivity(intent)
            }
        })
    }


    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_skip -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this, RegisterSuccessActivity::class.java))
                finish()
            }
            R.id.tv_next -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, SupplierListActivity::class.java)
                intent.putExtra("type", "1")//传1表示从注册流程过去
                startActivity(intent)
            }
        }
    }

    private val getDepartList: () -> Unit = {
        LoadingUtils.showLoading(this, "加载中")
        val params = HashMap<String, String>()
        RetrofitUtils.get().getJson(
            UrlConstants.DepartListUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, DepartListResponse::class.java)
                    departList.clear()
                    departList.addAll(t.data)
                    departAdapter.setNewData(departList)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        getDepartList()
    }

}