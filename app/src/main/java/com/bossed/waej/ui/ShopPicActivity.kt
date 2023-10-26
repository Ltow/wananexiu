package com.bossed.waej.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.ShopPicAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBSelPicCallBack
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.KmShopImageVoListBean
import com.bossed.waej.util.OnNoRepeatClickListener
import com.bossed.waej.util.OssUtils
import com.bossed.waej.util.DoubleClicksUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_shop_pic.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ShopPicActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var shopPicAdapter: ShopPicAdapter
    private val shopPics = ArrayList<KmShopImageVoListBean>()
    private val picUrls = ArrayList<String>()
    private var onSuccessNum = 0//上传成功数

    override fun getLayoutId(): Int {
        return R.layout.activity_shop_pic
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_shop_pic)
        rv_shop_pic.layoutManager = GridLayoutManager(this, 3)
        shopPicAdapter = ShopPicAdapter(shopPics, this)
        rv_shop_pic.adapter = shopPicAdapter
        for (s: String in intent.getStringArrayListExtra("urls")!!) {
            val bean = KmShopImageVoListBean()
            bean.imageUrl = s
            bean.imageName = getPhotoName(s)
            shopPics.add(bean)
        }
        shopPicAdapter.notifyDataSetChanged()
    }

    private fun getPhotoName(name: String): String {
        val temp = name.replace("\\\\".toRegex(), "/").split("/".toRegex()).toTypedArray()
        var fileName = ""
        if (temp.size > 1) {
            fileName = temp[temp.size - 1]
        }
        return fileName
    }

    override fun initListener() {
        tb_shop_pic.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        shopPicAdapter.setOnItemClickListener { view, position ->
            when (view.id) {
                R.id.iv_add_pic -> {
                    val intent = Intent(this, SelectPicActivity::class.java)
                    intent.putExtra("selNum", 9 - shopPics.size)
                    intent.putExtra("selMode", PictureConfig.MULTIPLE)
                    startActivity(intent)
                }
                R.id.iv_shop_pic -> {
                    val intent = Intent(this, PhotoViewActivity::class.java)
                    intent.putExtra("url", shopPics[position].imageUrl)
                    startActivity(intent)
                }
                R.id.iv_delete_pic -> {
                    shopPics.removeAt(position)
                    shopPicAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_upload -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                if (shopPics.isEmpty()) return
                for (bean: KmShopImageVoListBean in shopPics) {
                    if (bean.imageUrl.contains("https"))
                        picUrls.add(bean.imageUrl)
                    else {
                        val objectKey = "waej3/" + SPUtils.getInstance()
                            .getInt("shopId") + "/" + System.currentTimeMillis() + bean.imageName
                        picUrls.add(UrlConstants.BaseOssUrl + objectKey)
                        OssUtils.get().asyncPutImage(
                            objectKey,
                            bean.imageUrl,
                            this,
                            object : OssUtils.OnOssCallBackListener {
                                override fun onSuccess(float: Float) {

                                }

                                override fun onFailed(e: String) {
                                    ToastUtils.showShort(e)
                                }

                                override fun onProgress(progress: Int) {
                                }
                            })
                    }
                    onSuccessNum++
                    if (onSuccessNum == shopPics.size) {
                        val intent = Intent()
                        val bundle = Bundle()
                        bundle.putStringArrayList("urls", picUrls)
                        setResult(RESULT_OK, intent.putExtras(bundle))
                        finish()
                    }
                }
            }
        }
    }

    @Subscribe
    fun onSelPicCallBack(eb: EBSelPicCallBack) {
        for (media: LocalMedia in eb.list) {
            val bean = KmShopImageVoListBean()
            bean.imageUrl = media.cutPath
            bean.imageName = media.fileName
            shopPics.add(bean)
        }
        shopPicAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}