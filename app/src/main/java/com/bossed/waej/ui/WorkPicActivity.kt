package com.bossed.waej.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.*
import com.bossed.waej.R
import com.bossed.waej.adapter.WorkPicAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.WorkPicResponse
import com.bossed.waej.javebean.WorkPicRow
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.luck.picture.lib.config.PictureConfig
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_work_pic.*

class WorkPicActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var sgqAdapter: WorkPicAdapter
    private val sgqList = ArrayList<WorkPicRow>()
    private lateinit var sgzAdapter: WorkPicAdapter
    private val sgzList = ArrayList<WorkPicRow>()
    private lateinit var sghAdapter: WorkPicAdapter
    private val sghList = ArrayList<WorkPicRow>()
    private var imgUrl = ""
    private var status = -1

    override fun getLayoutId(): Int {
        return R.layout.activity_work_pic
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        BarUtils.setStatusBarLightMode(window, true)
        setMarginTop(tb_pic)
        rv_sgq.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        sgqAdapter = WorkPicAdapter(sgqList, intent.getStringExtra("type"))
        sgqAdapter.bindToRecyclerView(rv_sgq)
        rv_sgz.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        sgzAdapter = WorkPicAdapter(sgzList, intent.getStringExtra("type"))
        sgzAdapter.bindToRecyclerView(rv_sgz)
        rv_sgh.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        sghAdapter = WorkPicAdapter(sghList, intent.getStringExtra("type"))
        sghAdapter.bindToRecyclerView(rv_sgh)
        getPicList("1")
    }

    override fun initListener() {
        tb_pic.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
            }
        })
        sgqAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_delete_pic -> {
                    PopupWindowUtils.get()
                        .showConfirmPop(this, "确定删除此图片？") {
                            status = 1
                            delete(position)
                        }
                }

                R.id.iv_pic -> {
                    PopupWindowUtils.get().showPhotoPop(sgqList[position].img, this, rl_content)
//                    showPicPop(sgqList[position].img)
                }
            }
        }
        sgzAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_delete_pic -> {
                    PopupWindowUtils.get()
                        .showConfirmPop(this, "确定删除此图片？") {
                            status = 2
                            delete(position)
                        }
                }

                R.id.iv_pic -> {
                    PopupWindowUtils.get().showPhotoPop(sgzList[position].img, this, rl_content)
//                    showPicPop(sgzList[position].img)
                }
            }
        }
        sghAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_delete_pic -> {
                    PopupWindowUtils.get()
                        .showConfirmPop(this, "确定删除此图片？") {
                            status = 3
                            delete(position)
                        }
                }

                R.id.iv_pic -> {
                    PopupWindowUtils.get().showPhotoPop(sghList[position].img, this, rl_content)
//                    showPicPop(sghList[position].img)
                }
            }
        }
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.iv_upload_sgq -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                status = 1
                val intent = Intent(this, SelectPicActivity::class.java)
                intent.putExtra("selNum", 1)
                intent.putExtra("selMode", PictureConfig.SINGLE)
                launcher.launch(intent)
            }

            R.id.iv_upload_sgz -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                status = 2
                val intent = Intent(this, SelectPicActivity::class.java)
                intent.putExtra("selNum", 1)
                intent.putExtra("selMode", PictureConfig.SINGLE)
                launcher.launch(intent)
            }

            R.id.iv_upload_sgh -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                status = 3
                val intent = Intent(this, SelectPicActivity::class.java)
                intent.putExtra("selNum", 1)
                intent.putExtra("selMode", PictureConfig.SINGLE)
                launcher.launch(intent)
            }
        }
    }

//    private val showPicPop = { url: String ->
//        val popWindow = PopWindow.Builder(this).setView(R.layout.layout_pop_pic)
//            .setWidthAndHeight(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
//            ).setOutsideTouchable(true)
//            .setBackGroundLevel(0.5f)
//            .setChildrenView { contentView, pop ->
//                val photoView = contentView.findViewById<PhotoView>(R.id.pv_pic)
//                GlideUtils.get().loadShopPic(this, url, photoView)
//                photoView.setOnClickListener {
//                    pop.dismiss()
//                }
//            }.create()
//        popWindow.isClippingEnabled = false
//        popWindow.isFocusable = true
//        popWindow.showAtLocation(rl_content, Gravity.CENTER, 0, 0)
//    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                RESULT_OK -> {
                    val bundle = it.data?.extras
                    upLoadPic(bundle!!.getString("name")!!, bundle.getString("path")!!)
                }
            }
        }

    private val checkListSize = {
        if (intent.getStringExtra("type") == "history") {
            iv_upload_sgq.visibility = View.GONE
            iv_upload_sgz.visibility = View.GONE
            iv_upload_sgh.visibility = View.GONE
        } else {
            iv_upload_sgq.visibility = if (sgqList.size == 3) View.GONE else View.VISIBLE
            iv_upload_sgz.visibility = if (sgzList.size == 3) View.GONE else View.VISIBLE
            iv_upload_sgh.visibility = if (sghList.size == 3) View.GONE else View.VISIBLE
        }
    }


    private val upLoadPic = { imgName: String, imgLocalPath: String ->
        val dialog = Dialog(this, R.style.Dialog)
        val contentView = View.inflate(this, R.layout.layout_progress_dialog, null)
        dialog.setContentView(contentView)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val progressBar = contentView.findViewById<ProgressBar>(R.id.pb_progress)
        dialog.show()
        val objectKey = "waej3/" + SPUtils.getInstance()
            .getInt("shopId") + "/" + System.currentTimeMillis() + imgName
        imgUrl = UrlConstants.BaseOssUrl + objectKey
        OssUtils.get().asyncPutImage(objectKey, imgLocalPath, this,
            object : OssUtils.OnOssCallBackListener {
                override fun onSuccess(float: Float) {
                    dialog.dismiss()
                    ToastUtils.showShort("上传成功，用时：${float}秒")
                    saveUrl(imgUrl)
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

    private val saveUrl = { url: String ->
        val params = HashMap<String, Any>()
        params["orderId"] = intent.getIntExtra("id", -1)
        params["type"] = status
        params["img"] = url
        RetrofitUtils.get().postJson(
            UrlConstants.WorkPicUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BaseResponse::class.java)
                    if (t.code == 200)
                        getPicList("1")
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun getPicList(status: String) {
        val params = HashMap<String, String>()
        params["orderId"] = intent.getIntExtra("id", -1).toString()
        params["type"] = status
        RetrofitUtils.get().getJson(
            UrlConstants.WorkPicListUrl, params, this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, WorkPicResponse::class.java)
                    if (t.code == 200) {
                        when (status) {
                            "1" -> {
                                sgqList.clear()
                                sgqList.addAll(t.rows)
                                sgqAdapter.setNewData(sgqList)
                                getPicList("2")
                            }

                            "2" -> {
                                sgzList.clear()
                                sgzList.addAll(t.rows)
                                sgzAdapter.setNewData(sgzList)
                                getPicList("3")
                            }

                            "3" -> {
                                sghList.clear()
                                sghList.addAll(t.rows)
                                sghAdapter.setNewData(sghList)
                            }
                        }
                        checkListSize()
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private val delete = { position: Int ->
        LoadingUtils.showLoading(this, "加载中")
        val id = when (status) {
            1 -> sgqList[position].id
            2 -> sgzList[position].id
            3 -> sghList[position].id
            else -> -1
        }
        Api.getInstance().getApiService()
            .deletePic(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseResourceObserver<BaseResponse>() {
                override fun onComplete() {
                    LoadingUtils.dismissLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: BaseResponse) {
                    ToastUtils.showShort(t.msg)
                    when (t.code) {
                        200 -> {
                            when (status) {
                                1 -> {
                                    sgqList.removeAt(position)
                                    sgqAdapter.setNewData(sgqList)
                                }

                                2 -> {
                                    sgzList.removeAt(position)
                                    sgzAdapter.setNewData(sgzList)
                                }

                                3 -> {
                                    sghList.removeAt(position)
                                    sghAdapter.setNewData(sghList)
                                }
                            }
                            checkListSize()
                        }

                        401 -> {
                            PopupWindowUtils.get()
                                .showLoginOutTimePop(this@WorkPicActivity)
                        }

                        703 -> {
                            PopupWindowUtils.get()
                                .showSetConfirmAlertDialog(mContext, t.msg!!, "去购买", "") {
                                    mContext.startActivity(
                                        Intent(
                                            mContext,
                                            BuyProductActivity::class.java
                                        )
                                    )
                                }
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort(throwable.message)
                    LoadingUtils.dismissLoading()
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}