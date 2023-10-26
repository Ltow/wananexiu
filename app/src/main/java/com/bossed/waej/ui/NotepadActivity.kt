package com.bossed.waej.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.NotepadGridAdapter
import com.bossed.waej.adapter.NotepadListAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.customview.DragRecyclerView
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.NotepadBean
import com.bossed.waej.javebean.NotepadRow
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_notepad.*

class NotepadActivity : BaseActivity(), OnNoRepeatClickListener {
    private val list = ArrayList<NotepadRow>()
    private lateinit var listAdapter: NotepadListAdapter
    private lateinit var gridAdapter: NotepadGridAdapter
    private var showType = 0//展示类型 0：列表 1：表格
    private var moveY = 0//移动的 Y坐标

    override fun getLayoutId(): Int {
        return R.layout.activity_notepad
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_notepad)
        showType = SPUtils.getInstance().getInt("showType", 0)
    }

    override fun initListener() {
        tb_notepad.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        drv_notepad.setOnItemMoveListener(object : DragRecyclerView.OnItemMoveListener {
            override fun onMove(x: Int, y: Int) {
                rl_delete.visibility = View.VISIBLE
                moveY = y
            }

            override fun onUp(position: Int) {
                rl_delete.visibility = View.GONE
                if (moveY > rl_delete.top) {
                    PopupWindowUtils.get().showConfirmPop(this@NotepadActivity, "确定删除此记录？") {
                        deleteNotepad(list[position].id)
                    }
                }
            }

            override fun onItemClick(position: Int) {
            }
        })
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.iv_switch -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                showType = if (showType == 0) {
                    1
                } else
                    0
                SPUtils.getInstance().put("showType", showType)
                switch(showType)
            }
            R.id.iv_edit_notepad -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, NotepadEditActivity::class.java)
                intent.putExtra("type", "new")
                startActivity(intent)
            }
        }
    }

    private fun switch(type: Int) {
        when (type) {
            0 -> {
                drv_notepad.setBackgroundColor(Color.parseColor("#FFFFFF"))
                iv_switch.setImageResource(R.mipmap.icon_tyoe_list)
                drv_notepad.layoutManager = LinearLayoutManager(this)
                listAdapter = NotepadListAdapter(R.layout.layout_item_notepad_list, list)
                listAdapter.bindToRecyclerView(drv_notepad)
                listAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
                listAdapter.setOnItemClickListener { adapter, view, position ->
                    val intent = Intent(this, NotepadEditActivity::class.java)
                    intent.putExtra("type", "update")
                    intent.putExtra("id", list[position].id)
                    intent.putExtra("title", list[position].title)
                    intent.putExtra("content", list[position].content)
                    intent.putExtra("updateTime", list[position].updateTime)
                    startActivity(intent)
                }
            }
            1 -> {
                drv_notepad.setBackgroundColor(Color.parseColor("#F6F6F6"))
                iv_switch.setImageResource(R.mipmap.icon_type_grid)
                drv_notepad.layoutManager = GridLayoutManager(this, 2)
                gridAdapter = NotepadGridAdapter(R.layout.layout_item_notepad_grid, list)
                gridAdapter.bindToRecyclerView(drv_notepad)
                gridAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
                gridAdapter.setOnItemClickListener { adapter, view, position ->
                    val intent = Intent(this, NotepadEditActivity::class.java)
                    intent.putExtra("type", "update")
                    intent.putExtra("id", list[position].id)
                    intent.putExtra("title", list[position].title)
                    intent.putExtra("content", list[position].content)
                    intent.putExtra("updateTime", list[position].updateTime)
                    startActivity(intent)
                }
            }
        }
    }

    private fun getNotepadList() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService().getNotepadList(SPUtils.getInstance().getInt("userId"), "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<NotepadBean>() {
                override fun onSubscribe(d: Disposable) {
                    LoadingUtils.dismissLoading()
                }

                override fun onNext(t: NotepadBean) {
                    when (t.code) {
                        200 -> {
                            tv_total.text = "${t.total}个备忘录"
                            list.clear()
                            list.addAll(t.rows)
                            switch(showType)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@NotepadActivity)
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

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                }
            })
    }

    private fun deleteNotepad(id: Int) {
        LoadingUtils.showLoading(this, "加载中")
        Api.getInstance().getApiService()
            .deleteNotepad(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<BaseResponse>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: BaseResponse) {
                    when (t.code) {
                        200 -> {
                            getNotepadList()
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@NotepadActivity)
                        }
                        else -> {
                            ToastUtils.showShort(t.msg)
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        getNotepadList()
    }
}