package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.MyProductAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants.MyProductUrl
import com.bossed.waej.javebean.MyProductData
import com.bossed.waej.javebean.MyProductResponse
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_my_product.*

class MyProductActivity : BaseActivity() {
    private lateinit var productAdapter: MyProductAdapter
    private val productData = ArrayList<MyProductData>()

    override fun getLayoutId(): Int {
        return R.layout.activity_my_product
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        setMarginTop(tb_my_product)
        BarUtils.setStatusBarLightMode(window, true)
        rv_my_product.layoutManager = LinearLayoutManager(this)
        productAdapter = MyProductAdapter(productData)
        productAdapter.bindToRecyclerView(rv_my_product)
        getList()
    }

    override fun initListener() {
        tb_my_product.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                startActivity(Intent(this@MyProductActivity, BuyHistoryActivity::class.java))
            }
        })
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        RetrofitUtils.get()
            .getJson(MyProductUrl, HashMap(), this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, MyProductResponse::class.java)
                    if (t.code == 200) {
                        if (t.data != null && t.data.isNotEmpty()) {
                            tv_termTime.text = "统一到期日期：${t.data[0].termTime}"
                            productData.addAll(t.data)
                            productAdapter.setNewData(productData)
                        }
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}