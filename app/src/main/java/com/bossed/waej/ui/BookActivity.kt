package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.BookAdapter
import com.bossed.waej.adapter.BookItemAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBOCRCallBack
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.BookDs1
import com.bossed.waej.javebean.BookResponse
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_book.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.HashMap

class BookActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var adapter: BookAdapter
    private val bookItems = ArrayList<BookDs1>()
    private lateinit var bookItemAdapter: BookItemAdapter
    private val replaces = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_book
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_book_search)
        rv_book.layoutManager = LinearLayoutManager(this)
        adapter = BookAdapter(bookItems)
        adapter.bindToRecyclerView(rv_book)
        rv_item_book.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        bookItemAdapter = BookItemAdapter(replaces)
        bookItemAdapter.bindToRecyclerView(rv_item_book)
        et_vin.setText(intent.getStringExtra("vin"))
    }

    override fun initListener() {
        tb_book_search.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.ic_scan -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, OCRScanActivity::class.java)
                intent.putExtra("ocrType", 2)
                startActivity(intent)
            }
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (TextUtils.isEmpty(et_vin.text.toString()))
                    ToastUtils.showShort("请输入vin码")
                else
                    PopupWindowUtils.get()
                        .getBalance(this, object : PopupWindowUtils.OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                getData()
                            }

                            override fun onCancel() {
                            }
                        })
            }
        }
    }

    private fun getData() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "1303"
        params["inStr1"] = et_vin.text.toString()
        params["setName"] = getIMEI(this)
        params["Czy"] = SPUtils.getInstance().getString("username")
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BookResponse::class.java)
                    bookItems.clear()
                    replaces.clear()
                    if (t.code == "1") {
                        rl_model.visibility = View.VISIBLE
                        tv_model.text = t.outStr
                        bookItems.addAll(t.result.ds1)
                        replaces.addAll(t.result.ds1[0].maintMileageTitle.split(","))
                        bookItemAdapter.setAllData(t.result.ds1 as ArrayList<BookDs1>)
                    } else
                        ToastUtils.showShort(t.message)
                    adapter.setNewData(bookItems)
                    bookItemAdapter.setNewData(replaces)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    @Subscribe
    fun onMessageEvent(eb: EBOCRCallBack) {
        et_vin.setText(eb.vin)
        tv_model.text = ""
        bookItems.clear()
        replaces.clear()
        adapter.setNewData(bookItems)
        bookItemAdapter.setNewData(replaces)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}