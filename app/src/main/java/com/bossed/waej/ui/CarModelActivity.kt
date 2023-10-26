package com.bossed.waej.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.CarModelAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBFinish
import com.bossed.waej.javebean.DataXXX
import com.bossed.waej.javebean.DataXXXX
import com.bossed.waej.util.GlideUtils
import com.bossed.waej.util.OnNoRepeatItemClickListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_car_model.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class CarModelActivity : BaseActivity() {
    private val models = ArrayList<DataXXXX>()
    private lateinit var modelAdapter: CarModelAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_car_model
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_car_model)
        GlideUtils.get().loadCarLogo(this, intent.getStringExtra("logo")!!, iv_car_logo)
        tv_car_brand.text = intent.getStringExtra("brand")
        rv_car_model.layoutManager = LinearLayoutManager(this)
        modelAdapter = CarModelAdapter(models)
        modelAdapter.bindToRecyclerView(rv_car_model)
        modelAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
        tv_pl.text = intent.getStringExtra("pl")
    }

    override fun initListener() {
        tb_car_model.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })

        modelAdapter.onItemClickListener = object : OnNoRepeatItemClickListener {
            override fun onNoRepeatItemItemClick(
                adapter: BaseQuickAdapter<*, *>?,
                view: View?,
                position: Int
            ) {
                EventBus.getDefault().post(
                    DataXXXX(
                        models[position].data,
                        models[position].name,
                        intent.getStringExtra("logo")!!,
                        intent.getIntExtra("brandId", -1)
                    )
                )
                EventBus.getDefault().post(EBFinish(true))
                finish()
            }
        }
    }

    @Subscribe
    fun onSelYearsBack(eb: DataXXX) {
        tv_nf.text = eb.name
        models.addAll(eb.data)
        modelAdapter.setNewData(models)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}