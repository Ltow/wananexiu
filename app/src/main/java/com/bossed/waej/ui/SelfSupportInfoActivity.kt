package com.bossed.waej.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
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
import com.bossed.waej.adapter.SelfSupportTypeAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.customview.BottomDialog
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.http.UrlConstants.GoodsUrl
import com.bossed.waej.javebean.GoodsSortResponse
import com.bossed.waej.javebean.SelfSupportInfoResponse
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
import kotlinx.android.synthetic.main.activity_self_support_info.*

class SelfSupportInfoActivity : BaseActivity(), View.OnClickListener {
    private var categoryId: Int? = null
    private var status = -1
    private var selectPicType = -1
    private var mainPicture = ""
    private var picture = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_self_support_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_support)
        if (intent.getIntExtra("id", -1) != -1) {
            getInfo()
            tb_support.title = "商品信息"
            btn_commit.text = "保存"
        }
    }

    override fun initListener() {
        tb_support.setOnTitleBarListener(object : OnTitleBarListener {
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
                onClick(tv_add_pic)
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
            R.id.tv_sort -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                getSortList()
            }

            R.id.tv_association -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                startActivity(Intent(this, AssociationPartActivity::class.java))
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

            R.id.tv_add_pic -> {
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

            R.id.btn_commit -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                when {
                    TextUtils.isEmpty(et_name.text.toString()) -> ToastUtils.showShort("商品名称不能为空")
                    TextUtils.isEmpty(tv_sort.text.toString()) -> ToastUtils.showShort("商品类别不能为空")
                    TextUtils.isEmpty(et_virtualPrice.text.toString()) -> ToastUtils.showShort("请输入虚拟售价")
                    TextUtils.isEmpty(et_marketPrice.text.toString()) -> ToastUtils.showShort("请输入实际售价")
//                    TextUtils.isEmpty(et_memberPrice.text.toString()) -> ToastUtils.showShort("请输入会员价")
                    TextUtils.isEmpty(et_summary.text.toString()) -> ToastUtils.showShort("简介不能为空")
                    TextUtils.isEmpty(mainPicture) -> ToastUtils.showShort("请上传商品图片")
                    TextUtils.isEmpty(picture) -> ToastUtils.showShort("请上传商品详细图片")
                    TextUtils.isEmpty(et_description.text.toString()) -> ToastUtils.showShort("详细内容不能为空")
                    else -> if (intent.getIntExtra("id", -1) != -1) update() else commit()
                }
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
                tv_add_pic.visibility = View.GONE
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

    private fun getInfo() {
        LoadingUtils.showLoading(this, "加载中...")
        Api.getInstance().getApiService()
            .getGoodsInfo(intent.getIntExtra("id", -1))
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<String>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, SelfSupportInfoResponse::class.java)
                    when (t.code) {
                        200 -> {
                            et_name.setText(t.data.name)
                            tv_sort.text = t.data.categoryName
                            categoryId = t.data.categoryId
                            et_virtualPrice.setText(t.data.virtualPrice)
                            et_marketPrice.setText(t.data.marketPrice)
                            et_memberPrice.setText(t.data.memberPrice)
                            et_summary.setText(t.data.summary)
                            et_description.setText(t.data.description)
                            if (t.data.status != null)
                                status = t.data.status!!
                            mainPicture = t.data.mainPicture
                            Glide.with(this@SelfSupportInfoActivity).load(mainPicture).into(iv_pic)
                            tv_add_pic.visibility =
                                if (TextUtils.isEmpty(mainPicture)) View.VISIBLE else View.GONE
                            picture = t.data.picture
                            Glide.with(this@SelfSupportInfoActivity).load(picture).into(iv_pic_info)
                            tv_pic_info.visibility =
                                if (TextUtils.isEmpty(picture)) View.VISIBLE else View.GONE
                        }

                        401 -> PopupWindowUtils.get()
                            .showLoginOutTimePop(this@SelfSupportInfoActivity)

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

    /**
     * 新建商品
     */
    private fun commit() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["name"] = et_name.text.toString()
        params["productType"] = "1"
        params["categoryId"] = categoryId!!
        params["status"] = 6
        params["mainPicture"] = mainPicture
        params["picture"] = picture
        params["virtualPrice"] = et_virtualPrice.text.toString()
        params["marketPrice"] = et_marketPrice.text.toString()
        params["memberPrice"] = et_memberPrice.text.toString()
        params["summary"] = et_summary.text.toString()
        params["description"] = et_description.text.toString()
        RetrofitUtils.get()
            .postJson(GoodsUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    ToastUtils.showShort(t.msg)
                    if (t.code == 200)
                        finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    /**
     * 修改
     */
    private fun update() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["id"] = intent.getIntExtra("id", -1)
        params["name"] = et_name.text.toString()
        params["productType"] = "1"
        if (status != -1)
            params["status"] = status
        params["mainPicture"] = mainPicture
        params["picture"] = picture
        params["categoryId"] = categoryId!!
        params["virtualPrice"] = et_virtualPrice.text.toString()
        params["marketPrice"] = et_marketPrice.text.toString()
        params["memberPrice"] = et_memberPrice.text.toString()
        params["summary"] = et_summary.text.toString()
        params["description"] = et_description.text.toString()
        RetrofitUtils.get()
            .putJson(GoodsUrl, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    ToastUtils.showShort(t.msg)
                    if (t.code == 200)
                        finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    /**
     * 分类列表
     */
    private fun getSortList() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, String>()
        params["status"] = "1"
        RetrofitUtils.get()
            .getJson(
                UrlConstants.GoodsSortListUrl, params, this,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        LogUtils.d("tag", s)
                        val t = GsonUtils.fromJson(s, GoodsSortResponse::class.java)
                        BottomDialog(this@SelfSupportInfoActivity).create(R.layout.layout_pop_select_personnel)
                            .setCanceledOnTouchOutside(false)
                            .setViewInterface { view, dialog ->
                                view.findViewById<TextView>(R.id.tv_title).text = "商品分类"
                                view.findViewById<TextView>(R.id.tv_new).text = "分类管理"
                                val recyclerView = view.findViewById<RecyclerView>(R.id.rv_proposal)
                                recyclerView.layoutManager =
                                    LinearLayoutManager(this@SelfSupportInfoActivity)
                                val adapter = SelfSupportTypeAdapter(t.data as ArrayList)
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
                                    val intent = Intent(
                                        this@SelfSupportInfoActivity,
                                        SupportSortActivity::class.java
                                    )
                                    intent.putExtra("shopId", getIntent().getStringExtra("shopId"))
                                    startActivity(intent)
                                    dialog.dismiss()
                                }
                                view.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                                    dialog.dismiss()
                                }
                                view.findViewById<View>(R.id.tv_confirm).setOnClickListener {
                                    t.data.forEach {
                                        if (it.isSelected) {
                                            tv_sort.text = it.name
                                            categoryId = it.id
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