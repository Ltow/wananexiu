package com.bossed.waej.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.SelfItemTypeAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.customview.BottomDialog
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.http.UrlConstants.FreeItemSortListUrl
import com.bossed.waej.http.UrlConstants.FreeItemUrl
import com.bossed.waej.javebean.SelfPackageInfoResponse
import com.bossed.waej.javebean.ShopItemSortResponse
import com.bossed.waej.util.DoubleClicksUtils
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.OssUtils
import com.bossed.waej.util.PopupWindowUtils
import com.bossed.waej.util.RetrofitUtils
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.luck.picture.lib.config.PictureConfig
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_shop_item.*

class AddShopItemActivity : BaseActivity(), OnClickListener {
    private var selectPicType = -1
    private var mainPicture = ""
    private var picture = ""
    private var status = -1
    private var cateId = -1

    override fun getLayoutId(): Int {
        return R.layout.activity_add_shop_item
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_add_shop_item)
        if (!TextUtils.isEmpty(intent.getStringExtra("id"))) {
            getInfo()
            btn_commit.text = "保存"
        }
    }

    override fun initListener() {
        tb_add_shop_item.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {

            }
        })
        iv_pic.setOnLongClickListener {
            PopupWindowUtils.get().showConfirmPop(this, "是否更换当前图片？") {
                onClick(tv_pic)
            }
            false
        }
        iv_pic_info.setOnLongClickListener {
            PopupWindowUtils.get().showConfirmPop(this, "是否更换当前图片？") {
                onClick(tv_pic_info)
            }
            false
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_commit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(et_name.text.toString()) -> ToastUtils.showShort("请输入项目名称")
                    TextUtils.isEmpty(tv_sort.text.toString()) -> ToastUtils.showShort("请选择项目分类")
                    TextUtils.isEmpty(et_marketPrice.text.toString()) -> ToastUtils.showShort("请输入虚拟售价")
                    TextUtils.isEmpty(et_platformSettlePrice.text.toString()) -> ToastUtils.showShort(
                        "请输入实际售价"
                    )

//                    TextUtils.isEmpty(et_memberPrice.text.toString()) -> ToastUtils.showShort("请输入会员价")
                    TextUtils.isEmpty(et_briefIntroduction.text.toString()) -> ToastUtils.showShort(
                        "请输入项目简介"
                    )

                    TextUtils.isEmpty(mainPicture) -> ToastUtils.showShort("请上传项目图片")
                    TextUtils.isEmpty(picture) -> ToastUtils.showShort("请上传项目详情图片")
                    TextUtils.isEmpty(et_details.text.toString()) -> ToastUtils.showShort("请输入项目详情")
                    else -> if (TextUtils.isEmpty(intent.getStringExtra("id")))
                        newItem()
                    else
                        upDate()
                }
            }

            R.id.tv_sort -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                getSortList()
            }

            R.id.tv_pic -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, SelectPicActivity::class.java)
                intent.putExtra("selNum", 1)
                intent.putExtra("selMode", PictureConfig.SINGLE)
                picLauncher.launch(intent)
                selectPicType = 0
            }

            R.id.tv_pic_info -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                val intent = Intent(this, SelectPicActivity::class.java)
                intent.putExtra("selNum", 1)
                intent.putExtra("selMode", PictureConfig.SINGLE)
                picLauncher.launch(intent)
                selectPicType = 1
            }

            R.id.iv_pic -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showPhotoPop(mainPicture, this, ll_content)
            }

            R.id.iv_pic_info -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                PopupWindowUtils.get().showPhotoPop(picture, this, ll_content)
            }
        }
    }

    private val picLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                RESULT_OK -> {
                    val bundle = it.data!!.extras
                    ossPost(bundle!!.getString("name")!!, bundle.getString("path")!!)
                }
            }
        }

    private fun ossPost(imgName: String, imgLocalPath: String) {
        val dialog = Dialog(this, R.style.Dialog)
        val contentView = View.inflate(this, R.layout.layout_progress_dialog, null)
        dialog.setContentView(contentView)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val progressBar = contentView.findViewById<ProgressBar>(R.id.pb_progress)
        dialog.show()
        val objectKey = "waej3/" + SPUtils.getInstance()
            .getInt("shopId") + "/" + System.currentTimeMillis() + imgName
        val imgUrl = UrlConstants.BaseOssUrl + objectKey
        when (selectPicType) {
            0 -> {
                Glide.with(this).load(imgLocalPath).into(iv_pic)
                mainPicture = imgUrl
                tv_pic.visibility = View.GONE
            }

            1 -> {
                Glide.with(this).load(imgLocalPath).into(iv_pic_info)
                picture = imgUrl
                tv_pic_info.visibility = View.GONE
            }
        }
        OssUtils.get().asyncPutImage(objectKey, imgLocalPath, this,
            object : OssUtils.OnOssCallBackListener {
                override fun onSuccess(float: Float) {
                    dialog.dismiss()
                    ToastUtils.showShort("上传成功，用时：${float}秒")
                }

                override fun onFailed(e: String) {
                    dialog.dismiss()
                    ToastUtils.showShort(e)
                }

                override fun onProgress(progress: Int) {
                    progressBar.progress = progress
                }
            })
    }

    private fun newItem() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["name"] = et_name.text.toString()
        params["virtualPrice"] = et_marketPrice.text.toString()
        params["marketPrice"] = et_platformSettlePrice.text.toString()
        params["memberPrice"] = et_memberPrice.text.toString()
        params["briefIntroduction"] = et_briefIntroduction.text.toString()
        params["mainPicture"] = mainPicture//主图
        params["picture"] = picture//图片
        params["status"] = 1
        if (cateId != -1)
            params["cateId"] = cateId//类别id
        params["details"] = et_details.text.toString()
        RetrofitUtils.get()
            .postJson(FreeItemUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    if (t.code == 200)
                        finish()
                    ToastUtils.showShort(t.msg)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun upDate() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["id"] = intent.getStringExtra("id")
        params["name"] = et_name.text.toString()
        params["virtualPrice"] = et_marketPrice.text.toString()
        params["marketPrice"] = et_platformSettlePrice.text.toString()
        params["memberPrice"] = et_memberPrice.text.toString()
        params["briefIntroduction"] = et_briefIntroduction.text.toString()
        params["mainPicture"] = mainPicture//主图
        params["picture"] = picture//图片
        if (cateId != -1)
            params["cateId"] = cateId//类别id
        params["status"] = status
        params["details"] = et_details.text.toString()
        RetrofitUtils.get()
            .putJson(FreeItemUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    if (t.code == 200)
                        finish()
                    ToastUtils.showShort(t.msg)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getSelfPackageInfo(intent.getStringExtra("id").toInt())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, SelfPackageInfoResponse::class.java)
                    when (t.code) {
                        200 -> {
                            et_name.setText(t.data.name)
                            et_marketPrice.setText(t.data.virtualPrice)
                            et_platformSettlePrice.setText(t.data.marketPrice)
                            et_memberPrice.setText(t.data.memberPrice)
                            et_briefIntroduction.setText(t.data.briefIntroduction)
                            if (!TextUtils.isEmpty(t.data.mainPicture))
                                mainPicture = t.data.mainPicture!!
                            tv_pic.visibility =
                                if (TextUtils.isEmpty(mainPicture)) View.VISIBLE else View.GONE
                            Glide.with(this@AddShopItemActivity).load(mainPicture).into(iv_pic)
                            if (!TextUtils.isEmpty(t.data.picture))
                            picture = t.data.picture!!
                            tv_pic_info.visibility =
                                if (TextUtils.isEmpty(picture)) View.VISIBLE else View.GONE
                            Glide.with(this@AddShopItemActivity).load(picture).into(iv_pic_info)
                            et_details.setText(t.data.details)
                            tv_sort.text = t.data.cateName
                            status = t.data.status!!
                            if (t.data.cateId != null)
                                cateId = t.data.cateId!!
                        }

                        401 -> PopupWindowUtils.get().showLoginOutTimePop(this@AddShopItemActivity)
                        else ->
                            if (t.msg != null)
                                ToastUtils.showShort(t.msg)
                            else
                                ToastUtils.showShort("异常（代码：${t.code}）")
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    private fun getSortList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["status"] = "1"
        RetrofitUtils.get().getJson(
            FreeItemSortListUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, ShopItemSortResponse::class.java)
                    BottomDialog(this@AddShopItemActivity).create(R.layout.layout_pop_select_personnel)
                        .setCanceledOnTouchOutside(false)
                        .setViewInterface { view, dialog ->
                            view.findViewById<TextView>(R.id.tv_title).text = "项目分类"
                            view.findViewById<TextView>(R.id.tv_new).text = "分类管理"
                            val recyclerView = view.findViewById<RecyclerView>(R.id.rv_proposal)
                            recyclerView.layoutManager =
                                LinearLayoutManager(this@AddShopItemActivity)
                            val adapter = SelfItemTypeAdapter(t.data as ArrayList)
                            adapter.bindToRecyclerView(recyclerView)
                            adapter.emptyView =
                                layoutInflater.inflate(R.layout.layout_empty_view, null)
                            adapter.setOnItemClickListener { adapter, view, position ->
                                t.data.forEach {
                                    it.isSelected = false
                                }
                                t.data[position].isSelected = true
                                adapter.setNewData(t.data)
                            }
                            view.findViewById<TextView>(R.id.tv_new).setOnClickListener {
                                if (DoubleClicksUtils.get().isFastDoubleClick)
                                    return@setOnClickListener
                                startActivity(
                                    Intent(
                                        this@AddShopItemActivity,
                                        ShopItemSortActivity::class.java
                                    )
                                )
                                dialog.dismiss()
                            }
                            view.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                                dialog.dismiss()
                            }
                            view.findViewById<View>(R.id.tv_confirm).setOnClickListener {
                                t.data.forEach {
                                    if (it.isSelected) {
                                        tv_sort.text = it.name
                                        cateId = it.id!!
                                    }
                                }
                                dialog.dismiss()
                            }
                        }
                        .show()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }
}