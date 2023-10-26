package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.BrandSortAdapter
import com.bossed.waej.adapter.CarSeriesAdapter
import com.bossed.waej.adapter.HotBrandAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBFinish
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.javebean.*
import com.bossed.waej.util.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_car_brand.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class CarBrandActivity : BaseActivity() {
    private lateinit var hotBrandAdapter: HotBrandAdapter
    private val hotBeans = ArrayList<Hot>()
    private lateinit var brandAdapter: BrandSortAdapter
    private var brands: MutableList<Brand>? = null
    private lateinit var seriesAdapter: CarSeriesAdapter
    private val seriesBeans = ArrayList<CarModelData>()
    private var logo = ""
    private var parentPosition = -1
    private var position = -1
    private var brandId = -1

    override fun getLayoutId(): Int {
        return R.layout.activity_car_brand
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_sel_car_model)
        crv_all_brand.getHotRecycler().layoutManager = GridLayoutManager(this, 5)
        hotBrandAdapter = HotBrandAdapter(hotBeans)
        hotBrandAdapter.bindToRecyclerView(crv_all_brand.getHotRecycler())
        brandAdapter = BrandSortAdapter()
        RecyclerViewUtil.initNoDecoration(this, crv_all_brand.getRecycler(), brandAdapter)
        rv_series_car.layoutManager = LinearLayoutManager(this)
        seriesAdapter = CarSeriesAdapter(seriesBeans)
        seriesAdapter.bindToRecyclerView(rv_series_car)
        dl_car_model.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        getBrandList()
    }

    override fun initListener() {
        tb_sel_car_model.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {

            }

            override fun onRightClick(view: View?) {

            }
        })
        hotBrandAdapter.onItemClickListener = object : OnNoRepeatItemClickListener {
            override fun onNoRepeatItemItemClick(
                adapter: BaseQuickAdapter<*, *>?,
                view: View?,
                position: Int
            ) {
                GlideUtils.get()
                    .loadCarLogo(this@CarBrandActivity, hotBeans[position].logo, iv_car_logo)
                logo = hotBeans[position].logo
                brandId = hotBeans[position].id
                tv_car_brand.text = hotBeans[position].name
                getCarInfo(hotBeans[position].name)
            }
        }
        brandAdapter.setOnItemClickListener(object : BrandSortAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
                GlideUtils.get()
                    .loadCarLogo(this@CarBrandActivity, brands!![position].logo, iv_car_logo)
                logo = brands!![position].logo
                brandId = brands!![position].id
                tv_car_brand.text = brands!![position].name
                getCarInfo(brands!![position].name)
            }
        })
        seriesAdapter.setOnClickItemListener { position, parentPosition ->
            this.parentPosition = parentPosition
            this.position = position
            val intent = Intent(this, CarDisplacementActivity::class.java)
            intent.putExtra("brand", tv_car_brand.text.toString())
            intent.putExtra("logo", logo)
            intent.putExtra("brandId", brandId)
            startActivity(intent)
        }
    }

    private val getBrandList = {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getBrandList(et_search_brand.text.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<CarBrandBean>() {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: CarBrandBean) {
                    when (t.code) {
                        200 -> {
                            hotBeans.addAll(t.data.hotList)
                            hotBrandAdapter.setNewData(hotBeans)
                            val data = mutableListOf<String>()
                            for (brand: Brand in t.data.brandList) {
                                data.add(brand.name)
                            }
                            brands = crv_all_brand.sortData(t.data.brandList as MutableList)
                            crv_all_brand.initData(brands!!)
                            brandAdapter.initData(brands)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@CarBrandActivity)
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

    private val getCarInfo = { brand: String ->
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getCarModelMsgList(brand)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<CarModelsBean>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: CarModelsBean) {
                    when (t.code) {
                        200 -> {
                            seriesBeans.clear()
                            seriesBeans.addAll(t.data)
                            seriesAdapter.setNewData(seriesBeans)
                            dl_car_model.openDrawer(Gravity.RIGHT)
                        }
                        401 -> {
                            PopupWindowUtils.get().showLoginOutTimePop(this@CarBrandActivity)
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

    @Subscribe
    fun onFinished(eb: EBFinish) {
        if (eb.finished)
            finish()
    }

    override fun onStop() {
        super.onStop()
        if (parentPosition == -1 || position == -1)
            return
        EventBus.getDefault().post(
            CarModelDataX(
                seriesBeans[parentPosition].data[position].data,
                seriesBeans[parentPosition].data[position].name
            )
        )
    }

    override fun onBackPressed() {
        if (dl_car_model.isDrawerOpen(Gravity.RIGHT))
            dl_car_model.closeDrawer(Gravity.RIGHT)
        else
            super.onBackPressed()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}