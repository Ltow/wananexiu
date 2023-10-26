package com.bossed.waej.ui.amap

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.UiSettings
import com.amap.api.maps2d.model.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.core.ServiceSettings
import com.amap.api.services.core.SuggestionCity
import com.amap.api.services.geocoder.*
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener
import com.amap.api.services.help.Tip
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_amap.*
import com.amap.api.maps2d.model.LatLng


class AMapActivity : BaseActivity(), AMap.OnMarkerClickListener, AMap.InfoWindowAdapter,
    PoiSearch.OnPoiSearchListener {
    private var aMap: AMap? = null
    private var myLocationStyle: MyLocationStyle? = null
    private var progDialog: ProgressDialog? = null
    private var longitude = 0.0 //经度
    private var latitude = 0.0//纬度
    private var latLng: LatLng? = null
    private var mUiSettings: UiSettings? = null //定义一个UiSettings对象（控件交互）
    private var marker: Marker? = null
    private var geocodeSearch: GeocodeSearch? = null
    private var query: PoiSearch.Query? = null
    private var poiSearch: PoiSearch? = null
    private var poiResult: PoiResult? = null
    private var mPoiMarker: Marker? = null
    private var province = ""//省
    private var city = ""//市
    private var county = ""//区
    private var address = ""//详细地址
    private val mKeyWords = ""
    private var currentPage = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_amap
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_amap)
        mapView.onCreate(savedInstanceState)
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mapView.map
//            val latLng = LatLng(
//                intent.getDoubleExtra("longitude", 0.0),
//                intent.getDoubleExtra("latitude", 0.0)
//            ) //构造一个位置
//            aMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
        }
        //初始化定位蓝点样式类
        myLocationStyle = MyLocationStyle()
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle!!.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle!!.interval(2000)
        //设置定位蓝点的Style
        aMap!!.setMyLocationStyle(myLocationStyle)
        //设置默认定位按钮是否显示，非必需设置。
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap!!.isMyLocationEnabled = true
        //设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        myLocationStyle!!.showMyLocation(true)
        mUiSettings = aMap!!.uiSettings
        mUiSettings!!.isZoomControlsEnabled = true
        mUiSettings!!.isCompassEnabled = true
        mUiSettings!!.isMyLocationButtonEnabled = true
        mUiSettings!!.isScaleControlsEnabled = true
        mUiSettings!!.isZoomGesturesEnabled = true
        mUiSettings!!.isScrollGesturesEnabled = true
        ServiceSettings.updatePrivacyShow(this, true, true)//隐私合规必写
        ServiceSettings.updatePrivacyAgree(this, true)//隐私合规必写
        //地理搜索类
        geocodeSearch = GeocodeSearch(this)
    }

    override fun initListener() {
        aMap!!.setOnMarkerClickListener(this) // 添加点击marker监听事件
        aMap!!.setInfoWindowAdapter(this) // 添加显示infowindow监听事件
        tb_amap.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                val intent = Intent()
                val bundle = Bundle()
                bundle.putString("province", province)
                bundle.putString("city", city)
                bundle.putString("county", county)
                bundle.putString("address", address)
                bundle.putString("longitude", longitude.toString() + "")
                bundle.putString("latitude", latitude.toString() + "")
                setResult(RESULT_OK, intent.putExtras(bundle))
                finish()
            }
        })
        //点击地图,添加Marker
        aMap!!.setOnMapClickListener { latLng ->
            aMap!!.clear()
            marker = aMap!!.addMarker(MarkerOptions().position(latLng).title("").snippet(""))
            //                //可在其中解析aMapLocation获取相应内容。
            //                mineAsyncTask = new MineAsyncTaskUtil(latLng);
            //                mineAsyncTask.execute();
            longitude = latLng.longitude
            latitude = latLng.latitude
            getAddressByLatlng(latLng)
        }
        aMap!!.setOnMyLocationChangeListener { location ->
            /**
             * 从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取（获取地址描述数据章节有介绍）
             * @param location
             */
            longitude = location.longitude
            latitude = location.latitude
            latLng = LatLng(latitude, longitude)
            marker = aMap!!.addMarker(
                MarkerOptions().position(latLng).title(location.provider)
                    .snippet("DefaultMarker")
            )
            getAddressByLatlng(latLng!!)
        }
        geocodeSearch!!.setOnGeocodeSearchListener(object : OnGeocodeSearchListener {
            override fun onRegeocodeSearched(regeocodeResult: RegeocodeResult, i: Int) {
                val regeocodeAddress = regeocodeResult.regeocodeAddress
                val formatAddress = regeocodeAddress.formatAddress
                address = formatAddress
                province = regeocodeAddress.province
                city = regeocodeAddress.city
                county = regeocodeAddress.district
                tev_address.text = address
            }

            override fun onGeocodeSearched(geocodeResult: GeocodeResult, i: Int) {}
        })
        tv_search.setOnClickListener {
            startActivityForResult(Intent(this, MapSearchActivity::class.java), 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> {
                if (resultCode == 101 && data != null) {
                    aMap!!.clear()
                    val tip: Tip = data.getParcelableExtra("tip")!!
                    if (tip.poiID == null || tip.poiID == "") {
                        ToastUtils.showShort(tip.name, false)
                        doSearchQuery(tip.name)
                    } else {
                        addTipMarker(tip)
                    }
                    tv_search.text = tip.name
                }
            }
        }
    }

    /**
     * 用marker展示输入提示list选中数据
     *
     * @param tip
     */
    private fun addTipMarker(tip: Tip?) {
        if (tip == null) {
            return
        }
        mPoiMarker = aMap!!.addMarker(MarkerOptions())
        val point = tip.point
        if (point != null) {
            ToastUtils.showShort("检索中...", false)
            longitude = point.longitude
            latitude = point.latitude
            val markerPosition = LatLng(latitude, longitude)
            getAddressByLatlng(markerPosition)
            mPoiMarker!!.position = markerPosition
            //            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 17));
            aMap!!.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition(
                        markerPosition,
                        17f,
                        0f,
                        0f
                    )
                )
            )
        } else {
            ToastUtils.showShort(tip.name, false)
            doSearchQuery(tip.name)
        }
        mPoiMarker!!.title = tip.name
        mPoiMarker!!.snippet = tip.address
    }

    /**
     * 开始进行poi搜索
     */
    private fun doSearchQuery(keywords: String?) {
        showProgressDialog() // 显示进度框
        currentPage = 1
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = PoiSearch.Query(keywords, "", "")
        // 设置每页最多返回多少条poiitem
        query!!.pageSize = 10
        // 设置查第一页
        query!!.pageNum = currentPage
        poiSearch = PoiSearch(this, query)
        poiSearch!!.setOnPoiSearchListener(this)
        poiSearch!!.searchPOIAsyn()
    }

    /**
     * 显示进度框
     */
    private fun showProgressDialog() {
        if (progDialog == null) progDialog = ProgressDialog(this)
        progDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progDialog!!.isIndeterminate = false
        progDialog!!.setCancelable(false)
        progDialog!!.setMessage("正在搜索:\n$mKeyWords")
        progDialog!!.show()
    }

    /**
     * 隐藏进度框
     */
    private fun dismissProgressDialog() {
        if (progDialog != null) {
            progDialog!!.dismiss()
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        p0?.showInfoWindow()
        return false
    }

    override fun getInfoWindow(p0: Marker?): View {
        val view: View = layoutInflater.inflate(
            R.layout.poikeywordsearch_uri,
            null
        )
        val title = view.findViewById<View>(R.id.title) as TextView
        title.text = p0?.title
        val snippet = view.findViewById<View>(R.id.snippet) as TextView
        snippet.text = p0?.snippet
        return view
    }

    override fun getInfoContents(p0: Marker?): View? {
        return null
    }

    private fun getAddressByLatlng(latLng: LatLng) {
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        val latLonPoint = LatLonPoint(latLng.latitude, latLng.longitude)
        val query = RegeocodeQuery(latLonPoint, 200f, GeocodeSearch.AMAP)
        //异步查询
        geocodeSearch!!.getFromLocationAsyn(query)
    }

    override fun onPoiSearched(p0: PoiResult?, p1: Int) {
        dismissProgressDialog() // 隐藏对话框
        if (p1 == 1000) {
            if (p0?.query != null) { // 搜索poi的结果
                if (p0.query == query) { // 是否是同一条
                    poiResult = p0
                    // 取得搜索到的poiitems有多少页
                    val poiItems: List<PoiItem>? = poiResult!!.pois // 取得第一页的poiitem数据，页数从数字0开始
                    val suggestionCities = poiResult!!
                        .searchSuggestionCitys // 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (poiItems != null && poiItems.isNotEmpty()) {
                        aMap!!.clear() // 清理之前的图标
                        val poiOverlay = PoiOverlay(aMap, poiItems)
                        poiOverlay.removeFromMap()
                        poiOverlay.addToMap()
                        poiOverlay.zoomToSpan()
                    } else if (suggestionCities != null && suggestionCities.size > 0) {
                        showSuggestCity(suggestionCities)
                    } else {
                        ToastUtils.showShort("没有检索到相关数据", true)
                    }
                }
            } else {
                ToastUtils.showShort("没有检索到相关数据", true)
            }
        } else {
            ToastUtils.showShort(p1.toString() + "", true)
        }
    }

    override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {

    }

    private fun showSuggestCity(cities: List<SuggestionCity>) {
        var infomation = "推荐城市\n"
        for (i in cities.indices) {
            infomation += """
            城市名称:${cities[i].cityName}城市区号:${cities[i].cityCode}城市编码:${cities[i].adCode}
            
            """.trimIndent()
        }
        ToastUtils.showShort(infomation, true)
    }
}