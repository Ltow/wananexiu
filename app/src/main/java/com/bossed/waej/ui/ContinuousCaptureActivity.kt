package com.bossed.waej.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import com.bossed.waej.R
import com.bossed.waej.base.BaseActivity
import com.google.zxing.BarcodeFormat
import com.gyf.immersionbar.ImmersionBar
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import kotlinx.android.synthetic.main.activity_continuous_capture.*

import com.journeyapps.barcodescanner.CaptureManager

/**
 * 摄像头扫码
 */
class ContinuousCaptureActivity : BaseActivity() {
    /**
     * 条形码扫描管理器
     */
    private var mCaptureManager: CaptureManager? = null

    private var lastText = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_continuous_capture
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        //码格式
        val formats: Collection<BarcodeFormat> =
            listOf(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39, BarcodeFormat.CODABAR)
        barcode_scanner.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        //初始化barcodeView
        barcode_scanner.initializeFromIntent(intent)
        //监听扫描结果
        barcode_scanner.decodeContinuous(callBack)
        mCaptureManager = CaptureManager(this, barcode_scanner)
        mCaptureManager!!.initializeFromIntent(intent, savedInstanceState)
        mCaptureManager!!.decode()
    }

    private val callBack: BarcodeCallback = object : BarcodeCallback {
        @SuppressLint("SetTextI18n")
        override fun barcodeResult(result: BarcodeResult?) {
            result?.let {
                //扫描结果为空或者两次扫描的结果相同
                if (it.text.isNullOrEmpty() || it.text == lastText) {
                    return
                }
                lastText = it.text
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mCaptureManager!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mCaptureManager!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCaptureManager!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mCaptureManager!!.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mCaptureManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}