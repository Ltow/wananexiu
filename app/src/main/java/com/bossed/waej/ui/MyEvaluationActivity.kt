package com.bossed.waej.ui

import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.MyEvaluationAdapter
import com.bossed.waej.base.BaseActivity
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_my_evaluation.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.UrlConstants.EvaluationListUrl
import com.bossed.waej.http.UrlConstants.ReplyEvaluationUrl
import com.bossed.waej.javebean.EvaluationResponse
import com.bossed.waej.javebean.EvaluationRow
import com.bossed.waej.javebean.ReplyList
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class MyEvaluationActivity : BaseActivity(), OnRefreshLoadMoreListener {
    private lateinit var adapter: MyEvaluationAdapter
    private val list = ArrayList<EvaluationRow>()
    private var pageNum = 1
    private var position = -1

    override fun getLayoutId(): Int {
        return R.layout.activity_my_evaluation
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_my_evaluation)
        rv_evaluation.layoutManager = LinearLayoutManager(this)
        adapter = MyEvaluationAdapter(list)
        adapter.bindToRecyclerView(rv_evaluation)
        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_view, null)
        rb_evaluation.rating = intent.getFloatExtra("score", 5f)
    }

    override fun initListener() {
        srl_evaluation.setOnRefreshLoadMoreListener(this)
        tb_my_evaluation.setOnTitleBarListener(object : OnTitleBarListener {
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
                R.id.iv_reply -> {
                    KeyboardUtils.showSoftInput()
                    this.position = position
                }
            }
        }
        // 监听软键盘的弹出和隐藏事件
        val decorView = window.decorView
        decorView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            decorView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = decorView.height
            val keyboardHeight: Int = screenHeight - rect.bottom
            if (keyboardHeight > 0) {
                // 软键盘弹出，将PopupWindow上移
                et_input.visibility = View.VISIBLE
                et_input.requestFocus()
                val layoutParams = et_input.layoutParams as RelativeLayout.LayoutParams
                layoutParams.bottomMargin = keyboardHeight
                et_input.layoutParams = layoutParams
                et_input.setOnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED && event.action == KeyEvent.ACTION_DOWN) {
                        if (TextUtils.isEmpty(et_input.text.toString()) || et_input.text.toString()
                                .contains("\n") || et_input.text.toString().contains("\r")
                        ) {
                            ToastUtils.showShort("回复不能为空")
                            return@setOnEditorActionListener false
                        }
                        reply(et_input.text.toString())
                        KeyboardUtils.hideSoftInput(v)
                        et_input.setText("")
                    }
                    false
                }
            } else {
                // 软键盘隐藏，将PopupWindow下移
                et_input.visibility = View.GONE
                et_input.setText("")
            }
        }
    }

    private fun reply(content: String) {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["orderKeyId"] = list[position].orderId!!
        params["cId"] = list[position].id!!
        params["content"] = content
        RetrofitUtils.get()
            .post(ReplyEvaluationUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    ToastUtils.showShort(t.msg)
                    if (t.code == 200) {
                        val replyItem = ArrayList<ReplyList>()
                        val replyList = ReplyList()
                        replyList.content = content
                        replyItem.add(replyList)
                        list[position].replyList = replyItem
                        adapter.setNewData(list)
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["pageSize"] = "10"
        params["pageNum"] = pageNum.toString()
        params["orderByColumn"] = "createTime"
        RetrofitUtils.get().getJson(
            EvaluationListUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, EvaluationResponse::class.java)
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
        onRefresh(srl_evaluation)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageNum = 1
        list.clear()
        getList()
        refreshLayout.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getList()
        refreshLayout.finishLoadMore()
    }
}