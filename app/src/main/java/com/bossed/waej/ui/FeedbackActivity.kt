package com.bossed.waej.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.FeedBackAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.eventbus.EBSelPicCallBack
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.http.UrlConstants.NewFeedBack
import com.bossed.waej.javebean.FeedBackPicBean
import com.bossed.waej.util.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_feedback.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.StringBuilder

class FeedbackActivity : BaseActivity(), OnNoRepeatClickListener {
    private lateinit var feedBackAdapter: FeedBackAdapter
    private val beans = ArrayList<FeedBackPicBean>()
    private var type = -1
    private var typeName = ""
    private var onSuccessNum = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_feedback
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_feedback)
        rv_feedback.layoutManager = GridLayoutManager(this, 3)
        feedBackAdapter = FeedBackAdapter(beans, this)
        rv_feedback.adapter = feedBackAdapter
        switch(1)
    }

    override fun initListener() {
        feedBackAdapter.setOnItemClickListener { view, position ->
            when (view.id) {
                R.id.iv_add_pic -> {
                    val intent = Intent(this, SelectPicActivity::class.java)
                    intent.putExtra("selNum", 3 - beans.size)
                    intent.putExtra("selMode", PictureConfig.MULTIPLE)
                    startActivity(intent)
                }
                R.id.iv_shop_pic -> {
                    val intent = Intent(this, PhotoViewActivity::class.java)
                    intent.putExtra("url", beans[position].url)
                    startActivity(intent)
                }
                R.id.iv_delete_pic -> {
                    beans.removeAt(position)
                    feedBackAdapter.notifyDataSetChanged()
                }
            }
        }
        et_proposal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                tv_count.text = "${et_proposal.text.length}/500"
            }
        })
        tb_feedback.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                startActivity(Intent(this@FeedbackActivity, FeedHistoryActivity::class.java))
            }
        })
    }

    override fun onRepeatClick(v: View?) {
        when (v?.id) {
            R.id.tv_gn_yc -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(1)
            }
            R.id.tv_ty_wt -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(2)
            }
            R.id.tv_jy_wt -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(3)
            }
            R.id.tv_dd_wt -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                switch(4)
            }
            R.id.tv_confirm -> {
                if (DoubleClicksUtils.get().isFastDoubleClick)
                    return
                LoadingUtils.showLoading(this, "加载中...")
                val stringBuilder = StringBuilder()
                for (bean: FeedBackPicBean in beans) {
                    val objectKey = "waej3/" + SPUtils.getInstance()
                        .getInt("shopId") + "/" + System.currentTimeMillis() + bean.name
                    if (stringBuilder.isNotEmpty())
                        stringBuilder.append(",")
                    stringBuilder.append(UrlConstants.BaseOssUrl + objectKey)
                    OssUtils.get().asyncPutImage(
                        objectKey,
                        bean.name,
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
                    onSuccessNum++
                }
                if (onSuccessNum == beans.size) {
                    confirm(stringBuilder.toString())
                }
            }
        }
    }

    private fun confirm(img: String) {
        val params = HashMap<String, Any>()
        params["userId"] = SPUtils.getInstance().getInt("userId")
        params["type"] = type
        params["typeName"] = typeName
        params["problem"] = et_proposal.text.toString()
        params["img"] = img
        RetrofitUtils.get()
            .postJson(NewFeedBack, params, this, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    ToastUtils.showShort("反馈成功")
                    finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    private fun switch(type: Int) {
        clearChecked()
        this.type = type
        when (type) {
            1 -> {
                tv_gn_yc.setTextColor(Color.parseColor("#3477FC"))
                tv_gn_yc.setBackgroundResource(R.drawable.shape_stroke_3477fc_corners_dp11)
                typeName = tv_gn_yc.text.toString()
            }
            2 -> {
                tv_ty_wt.setTextColor(Color.parseColor("#3477FC"))
                tv_ty_wt.setBackgroundResource(R.drawable.shape_stroke_3477fc_corners_dp11)
                typeName = tv_ty_wt.text.toString()
            }
            3 -> {
                tv_jy_wt.setTextColor(Color.parseColor("#3477FC"))
                tv_jy_wt.setBackgroundResource(R.drawable.shape_stroke_3477fc_corners_dp11)
                typeName = tv_jy_wt.text.toString()
            }
            4 -> {
                tv_dd_wt.setTextColor(Color.parseColor("#3477FC"))
                tv_dd_wt.setBackgroundResource(R.drawable.shape_stroke_3477fc_corners_dp11)
                typeName = tv_dd_wt.text.toString()
            }
        }
    }

    private fun clearChecked() {
        tv_gn_yc.setTextColor(Color.parseColor("#CACACA"))
        tv_gn_yc.setBackgroundResource(R.drawable.shape_stroke_cacaca_corners_dp11)
        tv_ty_wt.setTextColor(Color.parseColor("#CACACA"))
        tv_ty_wt.setBackgroundResource(R.drawable.shape_stroke_cacaca_corners_dp11)
        tv_jy_wt.setTextColor(Color.parseColor("#CACACA"))
        tv_jy_wt.setBackgroundResource(R.drawable.shape_stroke_cacaca_corners_dp11)
        tv_dd_wt.setTextColor(Color.parseColor("#CACACA"))
        tv_dd_wt.setBackgroundResource(R.drawable.shape_stroke_cacaca_corners_dp11)
    }

    @Subscribe
    fun onSelPicCallBack(eb: EBSelPicCallBack) {
        for (media: LocalMedia in eb.list) {
            beans.add(FeedBackPicBean(media.cutPath, media.fileName))
        }
        feedBackAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}