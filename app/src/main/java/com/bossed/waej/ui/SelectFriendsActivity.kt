package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.SelectFriendsAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.eventbus.EBFinish
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.FriendsData
import com.bossed.waej.javebean.SelectFriendsBean
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_select_friends.*
import kotlinx.android.synthetic.main.layout_special_title.*
import org.greenrobot.eventbus.EventBus

class SelectFriendsActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var selectFriendsAdapter: SelectFriendsAdapter
    private val friendsBean = ArrayList<FriendsData>()
    private var itemIds: ArrayList<Int>? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_select_friends
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(rl_special_title)
        rv_select_friends.layoutManager = LinearLayoutManager(this)
        selectFriendsAdapter = SelectFriendsAdapter(friendsBean)
        selectFriendsAdapter.bindToRecyclerView(rv_select_friends)
        selectFriendsAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        itemIds = intent.getSerializableExtra("itemIds") as ArrayList<Int>
        getList()
    }

    override fun initListener() {
        selectFriendsAdapter.setOnItemClickListener { adapter, view, position ->
            for (bean: FriendsData in friendsBean) {
                if (bean == friendsBean[position])
                    bean.isSelect = !bean.isSelect
            }
            adapter.setNewData(friendsBean)
        }
        cb_select_all.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                for (bean: FriendsData in friendsBean) {
                    bean.isSelect = true
                }
                selectFriendsAdapter.setNewData(friendsBean)
            } else {
                for (bean: FriendsData in friendsBean) {
                    bean.isSelect = false
                }
                selectFriendsAdapter.setNewData(friendsBean)
            }
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                friendsBean.clear()
                getList()
            }
            R.id.tv_go_inquiry -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val supplierIds = ArrayList<Int>()
                for (bean: FriendsData in friendsBean) {
                    if (bean.isSelect) {
                        supplierIds.add(bean.id)
                    }
                }
                if (supplierIds.isEmpty()) {
                    ToastUtils.showShort("请至少选择一项内容")
                    return
                }
                inquiry(supplierIds)
            }
        }
    }

    private fun inquiry(supplierIds: ArrayList<Int>) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["id"] = intent.getIntExtra("id", 1)
        params["itemIds"] = itemIds!!
        params["supplierIds"] = supplierIds
        RetrofitUtils.get().postJson(
            UrlConstants.InquiryUrl,
            params,
            this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    ToastUtils.showShort(t.msg)
                    EventBus.getDefault().post(EBFinish(true))
                    finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getSupplierList(et_search_special.text.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<SelectFriendsBean>() {
                override fun onSubscribe(d: Disposable) {
                    LoadingUtils.dismissLoading()
                }

                override fun onNext(t: SelectFriendsBean) {
                    when (t.code) {
                        200 -> {
                            friendsBean.addAll(t.data)
                            selectFriendsAdapter.setNewData(friendsBean)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@SelectFriendsActivity)
                        }
                        703 -> {
                            PopupWindowUtils.get()
                                .showSetConfirmAlertDialog(mContext, t.msg, "去购买", "") {
                                    mContext.startActivity(
                                        Intent(
                                            mContext,
                                            BuyProductActivity::class.java
                                        )
                                    )
                                }
                        }
                        else -> {
                            ToastUtils.showShort(t.msg)
                        }
                    }
                }
            })
    }
}