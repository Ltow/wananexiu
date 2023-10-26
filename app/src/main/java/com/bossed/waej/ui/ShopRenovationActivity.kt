package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.ShopRenovationAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.eventbus.EBSelModelBack
import com.bossed.waej.http.UrlConstants.ShopUrl
import com.bossed.waej.javebean.ShopRenovationBean
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_shop_renovation.*
import org.greenrobot.eventbus.EventBus

class ShopRenovationActivity : BaseActivity(), OnClickListener {
    private lateinit var adapter: ShopRenovationAdapter
    private val list = ArrayList<ShopRenovationBean>()
    private var pageTemplateId = -1

    override fun getLayoutId(): Int {
        return R.layout.activity_shop_renovation
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_renovation)
        pageTemplateId = intent.getIntExtra("pageTemplateId", -1)
        rv_renovation.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        indicator.setWithRecyclerView(rv_renovation, LinearLayoutManager.HORIZONTAL)
        list.add(ShopRenovationBean("默认", pageTemplateId == 1, R.mipmap.icon_preview1))
        list.add(ShopRenovationBean("模板1", pageTemplateId == 2, R.mipmap.icon_preview2))
        list.add(ShopRenovationBean("模板2", pageTemplateId == 3, R.mipmap.icon_preview3))
        list.add(ShopRenovationBean("模板3", pageTemplateId == 4, R.mipmap.icon_preview4))
        list.add(ShopRenovationBean("模板4", pageTemplateId == 5, R.mipmap.icon_preview5))
        adapter = ShopRenovationAdapter(list)
        adapter.bindToRecyclerView(rv_renovation)
    }

    override fun initListener() {
        tb_renovation.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_status -> {
                    list.forEach {
                        it.isSelect = false
                    }
                    list[position].isSelect = true
                    adapter.setNewData(list)
                    pageTemplateId = position + 1
                }

                R.id.tv_load -> {
                    when (list[position].title) {
                        "默认" -> startActivity(Intent(this, PreviewActivity::class.java))
                        "模板1" -> startActivity(Intent(this, Preview2Activity::class.java))
                        "模板2" -> startActivity(Intent(this, Preview3Activity::class.java))
                        "模板3" -> startActivity(Intent(this, Preview4Activity::class.java))
                        "模板4" -> startActivity(Intent(this, Preview5Activity::class.java))
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_commit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                LoadingUtils.showLoading(this, "加载中...")
                val params = HashMap<String, Any>()
                params["id"] = intent.getStringExtra("shopId")
                params["fullname"] = intent.getStringExtra("fullname")
                params["pageTemplateId"] = pageTemplateId
                RetrofitUtils.get()
                    .putJson(ShopUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                        override fun onSuccess(s: String) {
                            LogUtils.d("tag", s)
                            val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                            if (t.code == 200) {
                                finish()
                                EventBus.getDefault().post(EBSelModelBack(true))
                            }
                            ToastUtils.showShort(t.msg)
                        }

                        override fun onFailed(e: String) {
                            ToastUtils.showShort(e)
                        }
                    })
            }
        }
    }
}