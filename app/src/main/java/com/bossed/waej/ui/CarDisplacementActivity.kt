package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.CarDisplacementAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBFinish
import com.bossed.waej.javebean.CarModelDataX
import com.bossed.waej.javebean.DataXX
import com.bossed.waej.util.GlideUtils
import com.bossed.waej.util.OnNoRepeatItemClickListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_car_displacement.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class CarDisplacementActivity : BaseActivity() {
    private lateinit var displacementAdapter: CarDisplacementAdapter
    private val displacementBeans = ArrayList<DataXX>()
    private var position = -1

    override fun getLayoutId(): Int {
        return R.layout.activity_car_displacement
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_car_displacement)
        GlideUtils.get().loadCarLogo(this, intent.getStringExtra("logo")!!, iv_car_logo)
        rv_car_displacement.layoutManager = LinearLayoutManager(this)
        displacementAdapter =
            CarDisplacementAdapter(displacementBeans)
        displacementAdapter.bindToRecyclerView(rv_car_displacement)
        displacementAdapter.emptyView=layoutInflater.inflate(R.layout.layout_empty_view, null)
    }

    override fun initListener() {
        tb_car_displacement.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        displacementAdapter.onItemClickListener = object : OnNoRepeatItemClickListener {
            override fun onNoRepeatItemItemClick(
                adapter: BaseQuickAdapter<*, *>?,
                view: View?,
                position: Int
            ) {
                this@CarDisplacementActivity.position = position
                val intent = Intent(this@CarDisplacementActivity, CarYearsActivity::class.java)
                intent.putExtra("logo", getIntent().getStringExtra("logo"))
                intent.putExtra("brandId", getIntent().getIntExtra("brandId", -1))
                intent.putExtra("brand", tv_car_brand.text.toString())
                startActivity(intent)
            }
        }
    }

    @Subscribe
    fun onSelSeriesBack(eb: CarModelDataX) {
        tv_car_brand.text = intent.getStringExtra("brand") + "  " + eb.name
        displacementBeans.addAll(eb.data)
        displacementAdapter.setNewData(displacementBeans)
    }

    @Subscribe
    fun onFinished(eb: EBFinish) {
        if (eb.finished)
            finish()
    }

    override fun onStop() {
        super.onStop()
        if (position == -1)
            return
        EventBus.getDefault()
            .post(DataXX(displacementBeans[position].data, displacementBeans[position].name))

    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}