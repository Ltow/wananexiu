package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bossed.waej.R
import com.bossed.waej.adapter.CarYearsAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBFinish
import com.bossed.waej.javebean.DataXX
import com.bossed.waej.javebean.DataXXX
import com.bossed.waej.util.GlideUtils
import com.bossed.waej.util.OnNoRepeatItemClickListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_car_years.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class CarYearsActivity : BaseActivity() {
    private val yearsBeans = ArrayList<DataXXX>()
    private lateinit var yearsAdapter: CarYearsAdapter
    private var position = -1

    override fun getLayoutId(): Int {
        return R.layout.activity_car_years
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_car_years)
        GlideUtils.get().loadCarLogo(this, intent.getStringExtra("logo")!!, iv_car_logo)
        tv_car_brand.text = intent.getStringExtra("brand")
        rv_car_years.layoutManager = LinearLayoutManager(this)
        yearsAdapter = CarYearsAdapter(yearsBeans)
        yearsAdapter.bindToRecyclerView(rv_car_years)
    }

    override fun initListener() {
        tb_car_years.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {

            }

            override fun onRightClick(view: View?) {

            }
        })
        yearsAdapter.onItemClickListener = object : OnNoRepeatItemClickListener {
            override fun onNoRepeatItemItemClick(
                adapter: BaseQuickAdapter<*, *>?,
                view: View?,
                position: Int
            ) {
                this@CarYearsActivity.position = position
                val intent = Intent(this@CarYearsActivity, CarModelActivity::class.java)
                intent.putExtra("brand", getIntent().getStringExtra("brand"))
                intent.putExtra("logo", getIntent().getStringExtra("logo"))
                intent.putExtra("brandId", getIntent().getIntExtra("brandId",-1))
                intent.putExtra("pl", tv_pl.text)
                startActivity(intent)
            }
        }
    }

    @Subscribe
    fun onSelDisplacementBack(eb: DataXX) {
        tv_pl.text = eb.name
        yearsBeans.addAll(eb.data)
        yearsAdapter.setNewData(yearsBeans)
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
        EventBus.getDefault().post(DataXXX(yearsBeans[position].data, yearsBeans[position].name))
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}