package com.bossed.waej.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.util.LoadingUtils
import com.bossed.waej.util.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import kotlinx.android.synthetic.main.activity_edit_notepad.*

class NotepadEditActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_edit_notepad
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        setMarginTop(tb_notepad_edit)
        when (intent.getStringExtra("type")) {
            "new" -> {
                tv_date.text = TimeUtils.getNowString()
            }
            "update" -> {
                tv_date.text = intent.getStringExtra("updateTime")
                et_content.setText(intent.getStringExtra("content"))
                et_title.setText(intent.getStringExtra("title"))
            }
        }
    }

    override fun initListener() {
        tb_notepad_edit.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(view: View?) {
                onBackPressed()
            }

            override fun onTitleClick(view: View?) {
            }

            override fun onRightClick(view: View?) {
                when {
                    TextUtils.isEmpty(et_title.text.toString()) -> ToastUtils.showShort("标题不能为空")
                    TextUtils.isEmpty(et_content.text.toString()) -> ToastUtils.showShort("内容不能为空")
                    else -> when (intent.getStringExtra("type")) {
                        "new" -> {
                            postData()
                        }
                        "update" -> {
                            putData()
                        }
                    }
                }
            }
        })
    }

    private fun postData() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["content"] = et_content.text.toString()
        params["userId"] = SPUtils.getInstance().getInt("userId")
        params["title"] = et_title.text.toString()
        RetrofitUtils.get().postJson(
            UrlConstants.NewNotepadUrl,
            params,this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }

            })
    }

    private fun putData() {
        LoadingUtils.showLoading(this, "加载中...")
        val params = HashMap<String, Any>()
        params["id"] = intent.getIntExtra("id", 0)
        params["content"] = et_content.text.toString()
        params["userId"] = SPUtils.getInstance().getInt("userId")
        params["title"] = et_title.text.toString()
        RetrofitUtils.get().putJson(
            UrlConstants.NewNotepadUrl,
            params,this,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    finish()
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }

            })
    }
}