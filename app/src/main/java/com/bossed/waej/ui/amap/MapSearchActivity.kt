package com.bossed.waej.ui.amap

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.util.OnNoRepeatClickListener
import com.bossed.waej.util.DoubleClicksUtils
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_map_search.*
import kotlinx.android.synthetic.main.layout_special_title.*

class MapSearchActivity : BaseActivity(), OnNoRepeatClickListener, Inputtips.InputtipsListener {
    private lateinit var mapSearchAdapter: MapSearchAdapter
    private val beans = ArrayList<Tip>()

    override fun getLayoutId(): Int {
        return R.layout.activity_map_search
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(rl_special_title)
        et_search_special.hint = "店铺详细位置"
        rv_map_search.layoutManager = LinearLayoutManager(this)
        mapSearchAdapter = MapSearchAdapter(beans)
        mapSearchAdapter.bindToRecyclerView(rv_map_search)
    }

    override fun initListener() {
        et_search_special.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!TextUtils.isEmpty(s)) {
                    val inputquery = InputtipsQuery(s.toString(), "")
                    val inputTips = Inputtips(this@MapSearchActivity.applicationContext, inputquery)
                    inputTips.setInputtipsListener(this@MapSearchActivity)
                    inputTips.requestInputtipsAsyn()
                } else {
                    beans.clear()
                    mapSearchAdapter.setNewData(beans)
                }
            }
        })
        mapSearchAdapter.setOnItemClickListener { adapter, view, position ->
            val tip: Tip = beans[position]
            val intent = Intent()
            intent.putExtra("tip", tip)
            setResult(101, intent)
            finish()
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                onBackPressed()
            }
            R.id.tv_search -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (!TextUtils.isEmpty(et_search_special.text.toString())) {
                    val inputquery = InputtipsQuery(et_search_special.text.toString(), "")
                    val inputTips = Inputtips(this@MapSearchActivity.applicationContext, inputquery)
                    inputTips.setInputtipsListener(this@MapSearchActivity)
                    inputTips.requestInputtipsAsyn()
                } else {
                    beans.clear()
                    mapSearchAdapter.setNewData(beans)
                }
            }
        }
    }

    override fun onGetInputtips(p0: MutableList<Tip>?, p1: Int) {
        if (p1 == 1000) { // 正确返回
            beans.clear()
            beans.addAll(p0!!)
            mapSearchAdapter.setNewData(beans)
        } else {
            ToastUtils.showShort(p1.toString())
        }
    }
}